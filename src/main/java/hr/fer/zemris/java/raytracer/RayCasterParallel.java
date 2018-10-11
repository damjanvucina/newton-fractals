package hr.fer.zemris.java.raytracer;

import static java.lang.Math.pow;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * A class representing a simplified raytracer, i.e. ray caster used for the
 * purpose of rendering 3-dimensional scenes. Unlinke RayCaster class, this
 * class uses ForkJoin framework and RecursiveAction for paralellizing the
 * tasks. The fork/join framework is an implementation of the ExecutorService
 * interface that helps take advantage of multiple processors. It is designed
 * for work that can be broken into smaller pieces recursively. The goal is to
 * use all the available processing power to enhance the performance of
 * application.
 * 
 * @author Damjan Vučina
 */
public class RayCasterParallel {
	private static final int DEFAULT_AMBIENT_VALUE = 15;
	private static final double DELTA = 1e-5;

	/**
	 * The main method. Invoked when the program is run.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10, 0, 0), new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 20, 20);
	}

	/**
	 * Method that is used for the purpose of acquiring an object charged with the
	 * task of tracing the ray in the defined scene.
	 *
	 * @return the new object charged with the task of tracing the ray in the
	 *         defined scene.
	 */
	private static IRayTracerProducer getIRayTracerProducer() {

		return new IRayTracerProducer() {
			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical,
					int width, int height, long requestNo, IRayTracerResultObserver observer) {

				System.out.println("Započinjem paralelne izračune...");

				int numOfPixels = width * height;
				short[] red = new short[numOfPixels];
				short[] green = new short[numOfPixels];
				short[] blue = new short[numOfPixels];

				Point3D vectorOG = view.sub(eye).normalize();
				Point3D vectorVUV = viewUp.normalize();
				Point3D vectorJ = vectorVUV.sub(vectorOG.scalarMultiply(vectorOG.scalarProduct(vectorVUV)));
				Point3D vectorJnormed = vectorJ.normalize();
				Point3D vectorI = vectorOG.vectorProduct(vectorJnormed);
				Point3D vectorInormed = vectorI.normalize();

				Point3D yAxis = vectorJnormed;
				Point3D xAxis = vectorInormed;

				Point3D screenCorner = view.sub(xAxis.scalarMultiply(horizontal / 2.0))
						.add(yAxis.scalarMultiply(vertical / 2.0));

				Scene scene = RayTracerViewer.createPredefinedScene();

				ForkJoinPool fjPool = new ForkJoinPool();
				ParalelPixelCalculation baseTask = new ParalelPixelCalculation(eye, horizontal, vertical, width,
						height, requestNo, observer, red, green, blue, 0, height - 1, xAxis, yAxis, screenCorner,
						scene);
				fjPool.invoke(baseTask);

				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}

		};
	}

	private static class ParalelPixelCalculation extends RecursiveAction {
		private static final long serialVersionUID = 1L;

		private static final int THRESHOLD = 20;

		private Point3D eye;
		private double horizontal;
		private double vertical;
		private int width;
		private int height;
		private long requestNo;
		private IRayTracerResultObserver observer;
		private short[] red;
		private short[] green;
		private short[] blue;

		private int ordinateMin;
		private int ordinateMax;

		private Point3D xAxis;
		private Point3D yAxis;

		private Point3D screenCorner;
		private Scene scene;
		
		@SuppressWarnings("unused")
		private static int instanceCounter = 1;

		public ParalelPixelCalculation(Point3D eye, double horizontal, double vertical, int width, int height,
				long requestNo, IRayTracerResultObserver observer, short[] red, short[] green, short[] blue,
				int ordinateMin, int ordinateMax, Point3D xAxis, Point3D yAxis, Point3D screenCorner, Scene scene) {

			super();
			this.eye = eye;
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.width = width;
			this.height = height;
			this.requestNo = requestNo;
			this.observer = observer;
			this.red = red;
			this.green = green;
			this.blue = blue;
			this.ordinateMin = ordinateMin;
			this.ordinateMax = ordinateMax;
			this.xAxis = xAxis;
			this.yAxis = yAxis;
			this.screenCorner = screenCorner;
			this.scene = scene;
		}

		@Override
		protected void compute() {
			if (divisionHitThreshold()) {
				performCalculation();
				//System.out.println("Instance num: " + instanceCounter++);

			} else {
				int limit = (ordinateMin + ordinateMax) / 2;

				ParalelPixelCalculation firstTask = new ParalelPixelCalculation(eye, horizontal, vertical, width, height,
						requestNo, observer, red, green, blue, ordinateMin, limit, xAxis, yAxis, screenCorner, scene);
				ParalelPixelCalculation secondTask = new ParalelPixelCalculation(eye, horizontal, vertical, width,
						height, requestNo, observer, red, green, blue, limit + 1, ordinateMax, xAxis, yAxis,
						screenCorner, scene);
				
				invokeAll(firstTask, secondTask);
			}
		}

		private void performCalculation() {
			short[] rgb = new short[3];
			int offset = 0;
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					Point3D screenPoint = screenCorner.add(xAxis.scalarMultiply((double) horizontal * x / (width - 1)))
							.sub(yAxis.scalarMultiply((double) vertical * y / (height - 1)));

					Ray ray = Ray.fromPoints(eye, screenPoint);
					tracer(scene, ray, rgb);
					red[offset] = rgb[0] > 255 ? 255 : rgb[0];
					green[offset] = rgb[1] > 255 ? 255 : rgb[1];
					blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
					offset++;
				}
			}
		}

		protected void tracer(Scene scene, Ray ray, short[] rgb) {
			RayIntersection closestIntersection = findClosestIntersection(scene, ray);

			if (closestIntersection == null) {
				rgb[0] = 0;
				rgb[1] = 0;
				rgb[2] = 0;

			} else {
				determineColorFor(scene, ray, closestIntersection, rgb);
			}
		}

		private void determineColorFor(Scene scene, Ray ray, RayIntersection objectIntersection, short[] rgb) {
			rgb[0] = DEFAULT_AMBIENT_VALUE;
			rgb[1] = DEFAULT_AMBIENT_VALUE;
			rgb[2] = DEFAULT_AMBIENT_VALUE;

			for (LightSource ls : scene.getLights()) {
				Ray lightRay = Ray.fromPoints(ls.getPoint(), objectIntersection.getPoint());
				RayIntersection lightObstacle = findClosestIntersection(scene, lightRay);

				if (lightObstacle == null) {
					throw new IllegalArgumentException("If no obstacles exist between light "
							+ "source and object, method must return intersection with the object.");
				}

				if (lightSourceVisible(objectIntersection, lightObstacle, ls)) {
					Point3D normal = objectIntersection.getNormal();
					Point3D light = ls.getPoint().sub(objectIntersection.getPoint()).normalize();

					diffuseComponentEffect(ls, rgb, objectIntersection, normal, light);
					reflectiveComponentEffect(ls, rgb, objectIntersection, normal, light, ray);
				}
			}
		}

		private void reflectiveComponentEffect(LightSource ls, short[] rgb, RayIntersection objectIntersection,
				Point3D normal, Point3D light, Ray ray) {

			Point3D r = normal.scalarMultiply(2 * light.scalarProduct(normal)).modifySub(light).normalize();
			Point3D v = ray.start.sub(objectIntersection.getPoint()).normalize();
			double cosine = r.scalarProduct(v);
			double cosinePowered = pow(cosine, objectIntersection.getKrn());

			if (cosine > 0) {
				rgb[0] += ls.getR() * objectIntersection.getKrr() * cosinePowered;
				rgb[1] += ls.getG() * objectIntersection.getKrg() * cosinePowered;
				rgb[2] += ls.getB() * objectIntersection.getKrb() * cosinePowered;
			}
		}

		private void diffuseComponentEffect(LightSource ls, short[] rgb, RayIntersection objectIntersection,
				Point3D normal, Point3D light) {

			double angle = light.scalarProduct(normal);
			if (angle < 0) {
				angle = 0;// since it is not visible from the source
			}

			rgb[0] += ls.getR() * objectIntersection.getKdr() * angle;
			rgb[1] += ls.getG() * objectIntersection.getKdg() * angle;
			rgb[2] += ls.getB() * objectIntersection.getKdb() * angle;
		}

		private boolean lightSourceVisible(RayIntersection objectIntersection, RayIntersection lightObstacle,
				LightSource ls) {

			double objectDistance = pointsDistance(objectIntersection, ls);
			double obstacleDistance = pointsDistance(lightObstacle, ls);

			return objectDistance < obstacleDistance + DELTA;
		}

		private double pointsDistance(RayIntersection objectIntersection, LightSource ls) {
			return ls.getPoint().sub(objectIntersection.getPoint()).norm();
		}

		private RayIntersection findClosestIntersection(Scene scene, Ray ray) {
			Objects.requireNonNull(scene, "Specified scene cannot be null");
			Objects.requireNonNull(ray, "Specified ray cannot be null");

			List<GraphicalObject> objects = scene.getObjects();
			if (objects.isEmpty()) {
				return null;
			}

			double minimumDistance = Double.MAX_VALUE;
			RayIntersection closestIntersection = null;
			RayIntersection currentIntersection = null;
			double currentDistance;

			for (GraphicalObject object : objects) {
				currentIntersection = object.findClosestRayIntersection(ray);

				if (currentIntersection != null) {
					currentDistance = currentIntersection.getDistance();

					if (currentDistance < minimumDistance) {
						minimumDistance = currentDistance;
						closestIntersection = currentIntersection;
					}
				}
			}

			return closestIntersection;
		}

		private boolean divisionHitThreshold() {
			return ordinateMax - ordinateMin <= THRESHOLD;
		}

	}
}
