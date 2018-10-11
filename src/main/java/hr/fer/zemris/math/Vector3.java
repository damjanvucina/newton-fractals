package hr.fer.zemris.math;

import static java.lang.Math.pow;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

import java.util.Objects;

/**
 * The class that represents an immutable 3-dimensional vector. All operations
 * performed on an instance of this class return new objects, instead of
 * altering the attributes of the instance they were invoked upon. This class
 * provides user with standard methods for performing basic arithmetic
 * operations on vectors such as normalizing, adding, subtracting, calculating
 * dot product and cross product, scaling and calculating the cosine of the
 * angle between two vectors.
 * 
 * @author Damjan Vučina
 */
public class Vector3 {

	/**
	 * The Constant DELTA, that is used for the purpose of identifying whether the
	 * two vectors, i.e. their coordinates are equal down to the sixth decimal
	 * point.
	 */
	public static final double DELTA = 1e-6;

	/** The x coordinate of the vector, i.e. the abscissa. */
	private double x;

	/** The y coordinate of the vector, i.e. the ordinate. */
	private double y;

	/** The z coordinate of the vector, i.e. the applicate. */
	private double z;

	/**
	 * Instantiates a new 3-dimensinal vector specifing its coordinates in the
	 * space.
	 *
	 * @param x
	 *            The x coordinate of the vector, i.e. the abscissa.
	 * @param y
	 *            The y coordinate of the vector, i.e. the ordinate.
	 * @param z
	 *            The z coordinate of the vector, i.e. the applicate.
	 */
	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Gets the x coordinate of the vector, i.e. the abscissa.
	 *
	 * @return the x coordinate of the vector, i.e. the abscissa.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Gets the y coordinate of the vector, i.e. the ordinate.
	 *
	 * @return the y coordinate of the vector, i.e. the ordinate.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Gets the z coordinate of the vector, i.e. the applicate.
	 *
	 * @return the z coordinate of the vector, i.e. the applicate.
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Calculates the norm of the vector, i.e. its "length" in the 3-dimensional
	 * space
	 *
	 * @return the double value representing the length of the vector
	 */
	public double norm() {
		return sqrt(pow(x, 2) + pow(y, 2) + pow(z, 2));
	}

	/**
	 * Calculates the normalized version of the vector provided via arguments. In
	 * mathematics, a unit vector in a normed vector space is a vector (often a
	 * spatial vector) of length 1.
	 *
	 * @return the normalized version of the vector provided via argument.
	 */
	public Vector3 normalized() {
		double norm = norm();
		if (norm == 0) {
			throw new IllegalArgumentException("Cannot normalize nul-vector.");
		}
		return new Vector3(x / norm, y / norm, z / norm);
	}

	/**
	 * Adds the vector provided via arguments to this vector. All operations
	 * performed on an instance of this class return new objects, instead of
	 * altering the attributes of the instance they were invoked upon.
	 *
	 * @param other
	 *            the other vector
	 * @return the new vector calculated by adding this vector to the one provided
	 *         via arguments
	 */
	public Vector3 add(Vector3 other) {
		Objects.requireNonNull(other, "Other vector cannot be null.");

		return new Vector3(x + other.x, y + other.y, z + other.z);
	}

	/**
	 * Subtracts the vector provided via arguments from this vector. All operations
	 * performed on an instance of this class return new objects, instead of
	 * altering the attributes of the instance they were invoked upon.
	 *
	 * @param other
	 *            the other vector
	 * @return the new vector calculated by subtracting this vector from the one
	 *         provided via arguments
	 */
	public Vector3 sub(Vector3 other) {
		Objects.requireNonNull(other, "Other vector cannot be null.");

		return new Vector3(x - other.x, y - other.y, z - other.z);
	}

	/**
	 * Calculates the dot product between this vector and the vector provided via
	 * arguments. Geometrically, it is the product of the Euclidean magnitudes of
	 * the two vectors and the cosine of the angle between them. These definitions
	 * are equivalent when using Cartesian coordinates. In modern geometry,
	 * Euclidean spaces are often defined by using vector spaces.
	 *
	 * @param other
	 *            the other vector provided via arguments
	 * @return the value calculated as the dot product between this vector and the
	 *         one provided via arguments
	 */
	public double dot(Vector3 other) {
		Objects.requireNonNull(other, "Other vector cannot be null.");

		return x * other.x + y * other.y + z * other.z;
	}

	/**
	 * Calculates the cross product between this vector and the vector provided via
	 * arguments. In mathematics and vector algebra, the cross product or vector
	 * product is a binary operation on two vectors in three-dimensional space and
	 * is denoted by the symbol x. Given two linearly independent vectors a and b,
	 * the cross product, a × b, is a vector that is perpendicular to both a and b
	 * and thus normal to the plane containing them. If two vectors have the same
	 * direction (or have the exact opposite direction from one another, i.e. are
	 * not linearly independent) or if either one has zero length, then their cross
	 * product is zero. More generally, the magnitude of the product equals the area
	 * of a parallelogram with the vectors for sides; in particular, the magnitude
	 * of the product of two perpendicular vectors is the product of their lengths.
	 *
	 * @param other
	 *            the other vector provided via arguments
	 * @return the new vector calculated as the result of cross product between this
	 *         vector and the one provided via arguments
	 */
	public Vector3 cross(Vector3 other) {
		Objects.requireNonNull(other, "Other vector cannot be null.");

		double xComponent = y * other.z - z * other.y;
		double yComponent = z * other.x - x * other.z;
		double zComponent = x * other.y - y * other.x;

		return new Vector3(xComponent, yComponent, zComponent);
	}

	/**
	 * Scales this vector with the factor provided via arguments, i.e. multiplies
	 * every vector coordinate with the provided scaling factor.
	 *
	 * @param s
	 *            the scaling factor
	 * @return the the new vector calculated as the result of scaling this vector
	 */
	public Vector3 scale(double s) {
		return new Vector3(x * s, y * s, z * s);
	}

	/**
	 * Calculates the cosine of the angle between this vector and the vector
	 * provided via arguments.
	 *
	 * @param other
	 *            the other vector provided via arguments
	 * @return the the value calculated as the cosAngle between this vector and the
	 *         one provided via arguments
	 */
	public double cosAngle(Vector3 other) {
		double thisNorm = norm();
		double otherNorm = other.norm();
		if (thisNorm == 0 || other.norm() == 0) {
			throw new IllegalArgumentException("Cannot calculate angle");
		}

		return dot(other) / (thisNorm * otherNorm);
	}

	/**
	 * Returns array representation of this 3-dimensional vector.
	 *
	 * @return the double[] array representation of this 3-dimensional vector.
	 */
	public double[] toArray() {
		return new double[] { x, y, z };
	}

	/**
	 * Prints a String representation of this 3-dimensional vector to the console.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("(");
		sb.append(String.format("%.6f", x)).append(", ");
		sb.append(String.format("%.6f", y)).append(", ");
		sb.append(String.format("%.6f", z));
		sb.append(")");

		return sb.toString();
	}

	/**
	 * Checks if two instances of Vector3 class are equal by calculating their hash.
	 * Two instances of Vector3 class are considered equal if their corresponding
	 * coordinates are equal down to the sixth decimal point.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * Checks if two instances of Vector3 class are equal. Two instances of Vector3
	 * class are considered equal if their corresponding coordinates are equal down
	 * to the sixth decimal point.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector3 other = (Vector3) obj;
		if (abs(x - other.x) > DELTA)
			// if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (abs(y - other.y) > DELTA)
			// if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (abs(z - other.z) > DELTA)
			// if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}

}
