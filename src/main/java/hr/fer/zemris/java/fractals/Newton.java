package hr.fer.zemris.java.fractals;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * The main program which is responsible for obtaining complex roots from the
 * user, parsing them and storing them appropriately and lastly invoking the
 * method for viewing the fractal.
 * 
 * @author Damjan Vučina
 */
public class Newton {

	/**
	 * The Constant DONE which is used for ending the process of requesting further
	 * inputs from the user.
	 */
	public static final String DONE = "done";

	/**
	 * The list containing the Complex roots enterd by the user and parsed by this
	 * class.
	 */
	public static List<Complex> list = new LinkedList<>();

	/**
	 * The main method. Invoked when the program is run.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");

		String input;
		int currentNumber = 1;

		while (true) {
			System.out.print("Root " + String.valueOf(currentNumber) + "> ");
			if (sc.hasNextLine()) {
				input = sc.nextLine().trim();

				if (input.equalsIgnoreCase(DONE)) {
					if (list.size() >= 2) {
						break;
					} else {
						System.out.println("You must enter at least two roots, roots entered: " + list.size());
						continue;
					}

				} else {
					input = input.replace(" ", "");
					try {
						list.add(parse(input));
					} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
						System.out.println("Invalid input.");
						continue;
					}

				}
			}
			currentNumber++;
		}

		sc.close();
		System.out.println("Image of fractal will appear shortly. Thank you.");

		ComplexRootedPolynomial rootedPolynomial = new ComplexRootedPolynomial(list.toArray(new Complex[list.size()]));
		FractalViewer.show(new FractalProducer(rootedPolynomial));
	}

	/**
	 * Method used for parsing users input of complex roots.General syntax for
	 * complex numbers is of form a+ib or a-ib where parts that are zero can be
	 * dropped, but not both (empty string is not legal complex number); for
	 * example, zero can be given as 0, i0, 0+i0, 0-i0. If there is 'i' present but
	 * no b is given, it is assumed that b is 1.
	 *
	 * @param input
	 *            the user's input
	 * @return the parsed complex number
	 */
	public static Complex parse(String input) {
		if (input.endsWith("i")) {
			input += "1";
		}

		int currentIndex = 0;
		input = input.replace(" ", "");
		int length = input.length();
		char[] array = input.toCharArray();

		StringBuilder real = null;
		if (!input.startsWith("i")) {
			real = new StringBuilder();
			while (currentIndex < length && (array[currentIndex] != '+' && array[currentIndex] != '-')
					|| currentIndex == 0) {
				real.append(array[currentIndex++]);
			}
		}

		StringBuilder imaginary = null;
		char imaginaryOperator = 0;
		if (input.contains("i")) {
			imaginaryOperator = (input.indexOf("i") == 0) ? '+' : array[input.indexOf("i") - 1];
			currentIndex = input.indexOf("i") + 1;
			imaginary = new StringBuilder();
			while (currentIndex < length) {
				imaginary.append(array[currentIndex++]);
			}
		}
		String sReal = (real == null ? "0" : real.toString());

		String sImaginary = null;
		if (imaginary == null) {
			sImaginary = "0";
		} else {
			sImaginary = (imaginaryOperator == '-' ? "-" + imaginary.toString() : imaginary.toString());
		}

		return new Complex(Double.parseDouble(sReal), Double.parseDouble(sImaginary));
	}
}
