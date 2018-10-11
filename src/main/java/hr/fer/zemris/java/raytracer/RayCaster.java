package hr.fer.zemris.java.raytracer;

import java.util.List;
import java.util.Objects;

import hr.fer.zemris.java.raytracer.model.GraphicalObject;
import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

import static java.lang.Math.pow;

/**
 * A class representing a simplified raytracer, i.e. ray caster used for the
 * purpose of rendering 3-dimensional scenes.
 * 
 * @author Damjan Vučina
 */
public class RayCaster {
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

				System.out.println("Započinjem izračune...");

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

				short[] rgb = new short[3];
				int offset = 0;
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						Point3D screenPoint = screenCorner
								.add(xAxis.scalarMultiply((double) horizontal * x / (width - 1)))
								.sub(yAxis.scalarMultiply((double) vertical * y / (height - 1)));

						Ray ray = Ray.fromPoints(eye, screenPoint);
						tracer(scene, ray, rgb);
						red[offset] = rgb[0] > 255 ? 255 : rgb[0];
						green[offset] = rgb[1] > 255 ? 255 : rgb[1];
						blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
						offset++;
					}
				}
				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
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
		};
	}
}
