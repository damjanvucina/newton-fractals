package hr.fer.zemris.math;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * The class that represents a complex polynomial in regular(non-rooted) form,
 * i.e. f(z) = z(n)*z^(n)+z(n-1)*z^(n-1)+...+z(1)*z+z(0), z(i) being factors
 * that user must provide. It provides user with method for calculating the
 * value of the polynomial in the specified abscissa coordinate, a method for
 * calculating the order of this polynomial, a method for multiplying
 * polynomials, deriving this polynomial and acquiring String representation of
 * this polynomial.
 * 
 * @author Damjan Vučina
 */
public class ComplexPolynomial {

	/** The factors of this Complex polynomial. */
	List<Complex> factors;

	/**
	 * Instantiates a new complex polynomial.
	 *
	 * @param factors
	 *            the factors of this Complex polynomial.
	 */
	public ComplexPolynomial(Complex... factors) {
		Objects.requireNonNull(factors, "This complex polynomial's factors cannot be null.");
		this.factors = new LinkedList<>();

		for (Complex factor : factors) {
			this.factors.add(factor);
		}
	}

	/**
	 * Calculates the order of this ComplexPolyomial.
	 *
	 * @return the order of this ComplexPolyomial.
	 */
	public short order() {
		return (short) (factors.size() - 1);
	}

	/**
	 * Multiplies this Complex polynomial with the one provided via arguments.
	 *
	 * @param p
	 *            the given complex polynomial is null
	 * @return the complex polynomial as the result of the multiplication
	 * @throws NullPointerException
	 *             if given complex polynomial is null
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		Objects.requireNonNull(p, "Given complex polynomial cannot be null.");

		Complex[] productFactors = new Complex[factors.size() + p.factors.size() - 1];

		for (int i = 0, firstSize = factors.size(); i < firstSize; i++) {
			for (int j = 0, secondSize = p.factors.size(); j < secondSize; j++) {

				int resultIndex = i + j;
				Complex currentResult = factors.get(i).multiply(p.factors.get(j));

				if (productFactors[resultIndex] == null) {
					productFactors[resultIndex] = currentResult;

				} else {
					productFactors[resultIndex] = productFactors[resultIndex].add(currentResult);
				}

			}
		}

		return new ComplexPolynomial(productFactors);
	}

	/**
	 * Derives this Complex polynomial.
	 *
	 * @return the complex polynomial which is the result of this
	 */
	public ComplexPolynomial derive() {
		Complex derivative[] = new Complex[factors.size() - 1];
		for (int i = 1, length = factors.size(); i < length; i++) {
			derivative[i - 1] = factors.get(i).multiply(new Complex(i, 0));
		}

		return new ComplexPolynomial(derivative);
	}

	/**
	 * Calculates the value of the polynomial in the specified abscissa coordinate
	 *
	 * @param z
	 *            the z complex number defining the abscissa coordinate
	 * @return the complex number calculated as the value of the polynomial in the
	 *         specified abscissa coordinate
	 */
	public Complex apply(Complex z) {
		Objects.requireNonNull(z, "Given complex polynomial cannot be null.");

		Complex result = Complex.ZERO;

		for (int i = 0, size = factors.size(); i < size; i++) {
			result = result.add(factors.get(i).multiply(z.power(i)));
		}

		return result;
	}

	/**
	 * Prints a String representation of this ComplexPolynomial to the console.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("f(z) = ");

		if (factors.size() == 0) {
			sb.append("0");
		} else {

			for (int i = factors.size() - 1; i >= 0; i--) {
				sb.append("(").append(factors.get(i)).append(")");
				sb.append(i > 0 ? "z" : "");
				sb.append(i > 1 ? "^" + String.valueOf(i) : "");
				sb.append("+");
			}
			sb.delete(sb.lastIndexOf("+"), sb.length());
		}

		return sb.toString();
	}
}

