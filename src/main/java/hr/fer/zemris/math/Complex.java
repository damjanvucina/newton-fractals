package hr.fer.zemris.math;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.sqrt;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.PI;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.abs;

/**
 * The class that represents a complex number and provides user with methods for
 * performing operations over it. These operations include calculating module of
 * the complex number, addition, subtraction, multiplication, division, negation
 * as well as calculating complex number roots and its power. *
 * 
 * @author Damjan Vučina
 */
public class Complex {

	/**
	 * The Constant ZERO defining a complex number with real component set to zero
	 * and imaginary component set to zero.
	 */
	public static final Complex ZERO = new Complex(0, 0);

	/**
	 * The Constant ONE defining a complex number with real component set to one and
	 * imaginary component set to zero.
	 */
	public static final Complex ONE = new Complex(1, 0);

	/**
	 * The Constant ONE_NEG defining a complex number with real component set to
	 * minus one and imaginary component set to zero.
	 */
	public static final Complex ONE_NEG = new Complex(-1, 0);

	/**
	 * The Constant IM defining a complex number with real component set to zero and
	 * imaginary component set to one.
	 */
	public static final Complex IM = new Complex(0, 1);

	/**
	 * The Constant IM_NEG defining a complex number with real component set to zero
	 * and imaginary component set to minus one.
	 */
	public static final Complex IM_NEG = new Complex(0, -1);

	/**
	 * The Constant DELTA used for checking whether complex numbers, i.e. their
	 * components are equal. Two complex numbers are considered equal if their real
	 * and imaginary components are equal down to the sixth decimal.
	 */
	public static final double DELTA = 1e-6;

	/** The real component of the complex number. */
	private double re;

	/** The imaginary component of the complex number. */
	private double im;

	/**
	 * Instantiates a new complex.
	 *
	 * @param re
	 *            The real component of the complex number.
	 * @param im
	 *            The imaginary component of the complex number.
	 */
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}

	/**
	 * Gets the real component of the complex number.
	 *
	 * @return the real component of the complex number.
	 */
	public double getRe() {
		return re;
	}

	/**
	 * Gets the imaginary component of the complex number.
	 *
	 * @return the imaginary component of the complex number.
	 */
	public double getIm() {
		return im;
	}

	/**
	 * Calculates the module of this complex number
	 *
	 * @return the double value of the module of the complex number
	 */
	public double module() {
		return sqrt(pow(re, 2) + pow(im, 2));
	}

	/**
	 * Multiplies this complex number and the one provided via arguments.
	 *
	 * @param c
	 *            the complex number provided via arguments
	 * @return the complex number which is the result of multiplication operation
	 * @throws NullPointerException
	 *             if the complex number provided via arguments is null
	 */
	public Complex multiply(Complex c) {
		Objects.requireNonNull(c, "Other complex number cannot be null.");

		return new Complex(re * c.re - im * c.im, im * c.re + re * c.im);
	}

	/**
	 * Divides this complex number and the one provided via arguments.
	 *
	 * @param c
	 *            the complex number provided via arguments
	 * @return the complex number which is the result of division operation
	 * @throws NullPointerException
	 *             if the complex number provided via arguments is null
	 */
	public Complex divide(Complex c) {
		Objects.requireNonNull(c, "Other complex number cannot be null.");

		double denominator = pow(c.re, 2) + pow(c.im, 2);
		if (denominator == 0) {
			throw new ArithmeticException("Dividing by zero occured. Enter different complex numbers.");
		}

		double firstNumerator = re * c.re + im * c.im;
		double secondNumerator = im * c.re - re * c.im;

		return new Complex(firstNumerator / denominator, secondNumerator / denominator);
	}

	/**
	 * Adds this complex number and the one provided via arguments.
	 *
	 * @param c
	 *            the complex number provided via arguments
	 * @return the complex number which is the result of addition operation
	 * @throws NullPointerException
	 *             if the complex number provided via arguments is null
	 */
	public Complex add(Complex c) {
		Objects.requireNonNull(c, "Other complex number cannot be null.");

		return new Complex(re + c.re, im + c.im);
	}

	/**
	 * Subtracts this complex number and the one provided via arguments.
	 *
	 * @param c
	 *            the complex number provided via arguments
	 * @return the complex number which is the result of subtraction operation
	 * @throws NullPointerException
	 *             if the complex number provided via arguments is null
	 */
	public Complex sub(Complex c) {
		Objects.requireNonNull(c, "Other complex number cannot be null.");

		return new Complex(re - c.re, im - c.im);
	}

	/**
	 * Negates this complex number.
	 *
	 * @return the complex number as the result of the negation operation
	 */
	public Complex negate() {
		return new Complex(-re, -im);
	}

	/**
	 * Calculates the given power of this complex number. Powers of complex numbers
	 * are just special cases of products when the power is a positive whole number.
	 *
	 * @param n
	 *            the given power of this complex number
	 * @return the complex number which is the result of power operation
	 * @throws IllegalArgumentException
	 *             if the given exponent is negative
	 */
	public Complex power(int n) {
		if (n < 0) {
			throw new IllegalArgumentException("Exponent must be non-negative, was:" + n);
		}

		double magnitudePowered = pow(module(), n);
		double argument = n * getAngle();

		return new Complex(magnitudePowered * cos(argument), magnitudePowered * sin(argument));

	}

	/**
	 * Helper method that calculates the angle of the complex number.
	 *
	 * @return the angle of the complex number
	 */
	double getAngle() {
		double result = atan2(im, re);
		return (result < 0) ? result + 2 * PI : result;
	}

	/**
	 * Calculates the roots of this complex number
	 *
	 * @param n
	 *            the number of roots to be calculated
	 * @return the list of the roots of this complex number
	 */
	public List<Complex> root(int n) {
		if (n <= 0) {
			throw new IllegalArgumentException("Exponent in root caculation must be positive, was: " + n);
		}

		List<Complex> roots = new LinkedList<>();
		double moduleRooted = pow(module(), 1. / n);

		for (int i = 0; i < n; i++) {
			double argument = (getAngle() + 2 * i * PI) / n;
			roots.add(new Complex(moduleRooted * cos(argument), moduleRooted * sin(argument)));
		}

		return roots;
	}

	/**
	 * Prints a String representation of this complex number to the console.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		boolean realComponentExists = false;

		if (Double.compare(re, 0.0) != 0) {
			if (re == (int) re) {
				sb.append((int) re);
			} else {
				sb.append(re);
			}
			realComponentExists = true;
		}

		if (Double.compare(im, 0.0) != 0) {
			if (realComponentExists && im > 0) {
				sb.append("+");
			}
			if (im == (int) im) {
				sb.append((int) im);
			} else {
				sb.append(im);
			}
			sb.append("i");
		}

		if (sb.toString().equals("")) {
			return "0";
		} else {
			return sb.toString();
		}
	}

	/**
	 * Checks if two instances of Complex number class are equal by calculating
	 * their hash. Two instances of Complex class are considered equal if their
	 * corresponding coordinates are equal down to the sixth decimal point.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(im);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(re);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * Checks if two instances of Complex number class are equal. Two instances of
	 * Complex class are considered equal if their corresponding coordinates are
	 * equal down to the sixth decimal point.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Complex other = (Complex) obj;
		if (abs(re - other.re) > DELTA)
			// if (Double.doubleToLongBits(im) != Double.doubleToLongBits(other.im))
			return false;
		if (abs(im - other.im) > DELTA)
			// if (Double.doubleToLongBits(re) != Double.doubleToLongBits(other.re))
			return false;
		return true;
	}

}
