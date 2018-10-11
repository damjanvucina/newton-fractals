package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * The class used to draw the fractals derived from Newton-Raphson iteration.
 * 
 * @author Damjan Vučina
 */
public class FractalProducer implements IFractalProducer {

	/**
	 * The Constant WORKER_FACTOR that is used to specify the effort to be put into
	 * the process of calculating the fractals.
	 */
	public static final int WORKER_FACTOR = 8;
	public static final int MAX_ITERATIONS = 16 * 16 * 16;

	/** The regular form polynomial. */
	ComplexPolynomial polynomial;

	/** The rooted form polynomial. */
	ComplexRootedPolynomial rootedPolynomial;

	/** The thread pool used to perform tasks. */
	ExecutorService threadPool;

	/** The num of processors currently available. */
	int numOfProcessors;

	/**
	 * Instantiates a new fractal producer.
	 *
	 * @param rootedPolynomial
	 *            the rooted polynomial
	 */
	public FractalProducer(ComplexRootedPolynomial rootedPolynomial) {
		this.rootedPolynomial = rootedPolynomial;
		this.polynomial = rootedPolynomial.toComplexPolynom();

		numOfProcessors = Runtime.getRuntime().availableProcessors();
		threadPool = Executors.newFixedThreadPool(numOfProcessors, (custom) -> {

			Thread thread = new Thread(custom);
			thread.setDaemon(true);
			return thread;
		});

	}

	/**
	 * Method used for the purpose of calculating the fractals using Newton-Raphson
	 * iteration.
	 */
	//@formatter:off
	@Override
	public void produce(double reMin, double reMax,
						double imMin, double imMax,
						int width, int height,
						long requestNo,
						IFractalResultObserver observer) {
	//@formatter:on

		System.out.println("Entered produce method, starting calculation process");

		int yRange = WORKER_FACTOR * numOfProcessors;
		int yPerRange = height / yRange;

		short[] data = new short[width * height];
		List<Future<Void>> results = new ArrayList<>();

		for (int i = 0; i < yRange; i++) {
			int yMin = i * yPerRange;
			int yMax = (i + 1) * yPerRange - 1;
			if (i == yRange - 1) {
				yMax = height - 1;
			}
			//@formatter:off
			Calculation job = new Calculation(reMin, reMax, imMin, imMax,
											  width, height, yMin, yMax,
											  polynomial, rootedPolynomial,
											  yMin * width, data);
			//@formatter:on
			results.add(threadPool.submit(job));
		}

		for (Future<Void> posao : results) {
			try {

				posao.get();
			} catch (InterruptedException | ExecutionException e) {
			}
		}

		System.out.println("Finished calculation process. Notifiying GUI!");
		observer.acceptResult(data, (short) (polynomial.order() + 1), requestNo);

	}
}

/**
 * Class implementing a task whose instances are to by executed.
 * 
 * @author Damjan Vučina
 */
class Calculation implements Callable<Void> {
	private static final double CONVERGENCE_TRESHOLD = 1e-3;
	private static final int MAX_ITERATIONS = 64;

	private double reMin;
	private double reMax;
	private double imMin;
	private double imMax;
	private int width;
	private int height;
	private int yMin;
	private int yMax;
	private ComplexPolynomial polynomial;
	private ComplexRootedPolynomial rootedPolynomial;
	int offset;
	private short[] data;

	//@formatter:off
	public Calculation(double reMin, double reMax, 
					   double imMin, double imMax,
					   int width, int height,
					   int yMin, int yMax,
					   ComplexPolynomial polynomial,
					   ComplexRootedPolynomial rootedPolynomial,
					   int offset, short[] data) {
	//@formatter:on

		this.reMin = reMin;
		this.reMax = reMax;
		this.imMin = imMin;
		this.imMax = imMax;
		this.width = width;
		this.height = height;
		this.yMin = yMin;
		this.yMax = yMax;
		this.polynomial = polynomial;
		this.rootedPolynomial = rootedPolynomial;
		this.offset = offset;
		this.data = data;
	}

	/**
	 * Method invoked by the objects who are to execute specified tasks.
	 */
	@Override
	public Void call() throws Exception {
		ComplexPolynomial derivedPolynomial = polynomial.derive();

		for (int y = yMin; y <= yMax; y++) {
			for (int x = 0; x < width; x++) {
				double cre = x / (width - 1.0) * (reMax - reMin) + reMin;
				double cim = (height - 1.0 - y) / (height - 1) * (imMax - imMin) + imMin;

				int iter = 0;
				Complex zn = new Complex(cre, cim);
				Complex zn1;
				Complex numerator;
				Complex denominator;
				Complex fraction;
				double module;
				short index;

				do {
					numerator = polynomial.apply(zn);
					denominator = derivedPolynomial.apply(zn);
					fraction = numerator.divide(denominator);
					zn1 = zn.sub(fraction);
					iter++;
					module = zn1.sub(zn).module();
					zn = zn1;

				} while (module > CONVERGENCE_TRESHOLD && iter < MAX_ITERATIONS);

				index = (short) rootedPolynomial.indexOfClosestRootFor(zn1, CONVERGENCE_TRESHOLD);

				if (index == -1) {
					data[offset++] = 0;
				} else {
					data[offset++] = (short) (index + 1);
				}
			}
		}
		return null;
	}
}
