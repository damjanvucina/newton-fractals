package hr.fer.zemris.java.raytracer.model;

import static java.lang.Math.sqrt;
import static java.lang.Math.min;

/**
 * The class used for the purpose of implementing a Sphere graphical object that
 * exists within the scene.
 * 
 * @author Damjan Vučina
 */
public class Sphere extends GraphicalObject {

	/** The center of the sphere. */
	private Point3D center;

	/** The radius of the sphere. */
	private double radius;

	/** The red diffusive component. */
	private double kdr;

	/** The green diffusive component. */
	private double kdg;

	/** The blue diffusive component. */
	private double kdb;

	/** The red reflective component */
	private double krr;

	/** The green reflective component */
	private double krg;

	/** The blue reflective component */
	private double krb;

	/** The power */
	private double krn;

	/**
	 * Instantiates a new sphere with the specified parameters.
	 *
	 * @param center
	 *            The center of the sphere.
	 * @param radius
	 *            the radius of the sphere.
	 * @param kdr
	 *            The red diffusive component.
	 * @param kdg
	 *            The green diffusive component.
	 * @param kdb
	 *            The blue diffusive component.
	 * @param krr
	 *            The red reflective component
	 * @param krg
	 *            The green reflective component
	 * @param krb
	 *            The blue reflective component
	 * @param krn
	 *            the power
	 */
	//@formatter:off
	public Sphere(Point3D center, double radius,
				  double kdr, double kdg, double kdb,
				  double krr, double krg, double krb,
				  double krn) {
	//@formatter:on

		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;
	}

	/**
	 * Method used for the purpose of finding the closest intersection with the ray
	 * if one exists.
	 */
	// algortihm and formulas as described in ray-sphere intersection page at
	// https://www.scratchapixel.com;
	// |P−C^2−R^2=0
	// |O+tD−C|^2−R^2=0.
	// a=1
	// b=2D(O−C)
	// c=|O−C|^2−R^2
	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		Point3D rayOC = ray.start.sub(center);

		double a = 1;
		double b = ray.direction.scalarMultiply(2).scalarProduct(rayOC);
		double c = rayOC.scalarProduct(rayOC) - radius * radius;

		double discriminant = b * b - 4 * a * c;
		if (discriminant < 0) {
			return null;// when Δ < 0, there is not root at (which means that the ray doesn't intersect
						// the sphere)

		}
		
		double nominatorArgument = sqrt(discriminant);
		double firstSolution = (-b + nominatorArgument) / (2 * a);
		double secondSolution = (-b - nominatorArgument) / (2 * a);

		if (firstSolution < 0 && secondSolution < 0) {
			return null;// negative roots mean that the ray intersects the sphere but behind the origin
		}
		
		Point3D firstIntersection = ray.start.add(ray.direction.scalarMultiply(firstSolution));
		Point3D secondIntersection = ray.start.add(ray.direction.scalarMultiply(secondSolution));
		
		double firstDistance = ray.start.sub(firstIntersection).norm();
		double secondDistance = ray.start.sub(secondIntersection).norm();
		
		
		Point3D intersection = (firstDistance < secondDistance) ? firstIntersection : secondIntersection;
		double intersectionDistance = min(firstDistance, secondDistance);
		boolean isOuter = center.sub(intersection).norm() - radius > 0;
		
		return new RayIntersection(intersection, intersectionDistance, isOuter) {
			
			@Override
			public Point3D getNormal() {
				return getPoint().sub(center).normalize();
			}
			
			@Override
			public double getKrr() {
				return krr;
			}
			
			@Override
			public double getKrn() {
				return krn;
			}
			
			@Override
			public double getKrg() {
				return krg;
			}
			
			@Override
			public double getKrb() {
				return krb;
			}
			
			@Override
			public double getKdr() {
				return kdr;
			}
			
			@Override
			public double getKdg() {
				return kdg;
			}
			
			@Override
			public double getKdb() {
				return kdb;
			}
		};
	}
}
