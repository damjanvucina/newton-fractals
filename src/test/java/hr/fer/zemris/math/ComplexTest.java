package hr.fer.zemris.math;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class ComplexTest {
	public static final double DELTA = 1E-4;

	Complex complexNumber1;
	Complex complexNumber2;
	Complex complexNumber3;
	Complex complexNumber4;
	Complex complexNumber5;

	@Before
	public void initialize() {
		complexNumber1 = new Complex(-1.13, -2.15);
		complexNumber2 = new Complex(0.57, -3.11);
		complexNumber3 = new Complex(13.3, 0);
		complexNumber4 = new Complex(0, 3);
		complexNumber5 = new Complex(0, 0);
	}
	
	@Test
	public void moduleRegularTest() {
		assertEquals(3.1618, complexNumber2.module(), DELTA);
		assertEquals(13.3, complexNumber3.module(), DELTA);
		assertEquals(3, complexNumber4.module(), DELTA);
	}
	
	@Test
	public void moduleBothArgumentsNegativeTest() {
		assertEquals(2.42887, complexNumber1.module(), DELTA);
	}
	
	@Test
	public void moduleBothrgumentsZero() {
		assertEquals(0, complexNumber5.module(), DELTA);
	}
	
	@Test
	public void moduleZeroTest() {
		assertEquals(0, complexNumber5.module(), DELTA);
	}
	
	@Test
	public void getAngleRegular() {
		assertEquals(4.8936, complexNumber2.getAngle(), DELTA);
		assertEquals(1.570796, complexNumber4.getAngle(), DELTA);
	}
	
	@Test
	public void getAngleBothArgumentsNegativeTest() {
		assertEquals(4.228489, complexNumber1.getAngle(), DELTA);
	}
	
	@Test
	public void getAngleZero() {
		assertEquals(0, complexNumber3.getAngle(), DELTA);
		assertEquals(0, complexNumber5.getAngle(), DELTA);
	}
	
	@Test
	public void addTestRegular() {
		assertEquals(new Complex(13.87, -3.11), complexNumber2.add(complexNumber3));
		assertEquals(new Complex(13.3, 3), complexNumber3.add(complexNumber4));
	}
	
	@Test
	public void addBothArgumentsNegativeTest() {
		assertEquals(new Complex(-0.56, -5.26), complexNumber1.add(complexNumber2));
	}
	
	@Test
	public void addArgumentsZeroTest() {
		assertEquals(new Complex(0, 3), complexNumber4.add(complexNumber5));
	}
	
	@Test
	public void subTest() {
		assertEquals(new Complex(-12.73, -3.11), complexNumber2.sub(complexNumber3));
		assertEquals(new Complex(13.3, -3), complexNumber3.sub(complexNumber4));
		assertEquals(new Complex(0, 3), complexNumber4.sub(complexNumber5));
	}
	
	@Test
	public void subBothArgumentsNegativeTest() {
		assertEquals(new Complex(-1.7, 0.96), complexNumber1.sub(complexNumber2));
	}

	@Test
	public void multiplyTestRegular() {
		assertEquals(new Complex(-7.3306, 2.2888), complexNumber1.multiply(complexNumber2));
	}
	
	@Test
	public void multiplyTestZero() {
		assertEquals(new Complex(0, 0), complexNumber4.multiply(complexNumber5));
	}
	
	@Test
	public void divideTestRegular() {
		assertEquals(new Complex(0.6044213, -0.4741222), complexNumber1.divide(complexNumber2));
	}
	
	@Test
	public void divideTestZero() {
		assertEquals(new Complex(-1.0366667, -0.19), complexNumber2.divide(complexNumber4));
	}
	
	@Test
	public void powerTestRegular() {
		assertEquals(new Complex(-16.354098, 27.048914), complexNumber2.power(3));
	}
	
	@Test
	public void powerTestBothArgumentsZero() {
		assertEquals(new Complex(0, 0), complexNumber5.power(2));
	}
	
	@Test
	public void powerTestZeroExponent() {
		assertEquals(new Complex(1, 0), complexNumber2.power(0));
	}

	@Test(expected=IllegalArgumentException.class)
	public void rootTestZeroArgument() {
		assertEquals(new Complex(1.3130833, -0.6557679), complexNumber2.root(0).get(0));
	}
	
	@Test
	public void rootTestRegular() {
		assertEquals(new Complex(-0.0886299, 1.4650474), complexNumber2.root(3).get(0));
	}
	
	@Test
	public void negateTestBothArgumentsNegative() {
		assertEquals(new Complex(1.13, 2.15), complexNumber1.negate());
	}
	
	@Test
	public void negateTestBothArgumentsZero() {
		assertEquals(new Complex(0, 0), complexNumber5.negate());
	}

}
