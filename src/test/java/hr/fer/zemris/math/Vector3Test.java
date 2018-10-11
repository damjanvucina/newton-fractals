package hr.fer.zemris.math;
 
import org.junit.Assert;
import org.junit.Test;

public class Vector3Test {
	
	public static final double DELTA = 1e-5;
	public Vector3 vector1;
	public Vector3 vector2;
	
	@Test
	public void testNormOnes() {
		vector1 = new Vector3(1, 1, 1);
		Assert.assertEquals(1.732050, vector1.norm(), DELTA);
	}
	
	@Test
	public void testNormZeros() {
		vector1 = new Vector3(0, 0, 0);
		Assert.assertEquals(0, vector1.norm(), DELTA);
	}
	
	@Test
	public void testNormNegativeComponents() {
		vector1 = new Vector3(-3.11, -2.32, -0.57);
		Assert.assertEquals(3.92166, vector1.norm(), DELTA);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testNormalizedZero() {
		vector1 = new Vector3(0, 0, 0);
		Assert.assertEquals(new Vector3(0, 0, 0), vector1.normalized());
	}
	
//	@Test
//	public void testNormalizedRegular() {
//		vector1 = new Vector3(1.11, 2.22, 3.33);
//		Assert.assertEquals(new Vector3(0.267261, 0.534522, 0.801704), vector1.normalized());
//	}
	
	@Test
	public void addTestPositiveValues() {
		vector1 = new Vector3(1.11, 2.22, 3.33);
		vector2 = new Vector3(4.44, 5.55, 6.66);
		
		Assert.assertEquals(new Vector3(5.55, 7.77, 9.99), vector1.add(vector2));
	}
	
	@Test
	public void addTestNegativeValues() {
		vector1 = new Vector3(-1.11, 2.22, -3.33);
		vector2 = new Vector3(4.44, -5.55, -6.66);
		
		Assert.assertEquals(new Vector3(3.33, -3.33, -9.99), vector1.add(vector2));
	}
	
	@Test
	public void addTestZeros() {
		vector1 = new Vector3(0, 0, -0.11);
		vector2 = new Vector3(0, 0, 0);
		
		Assert.assertEquals(new Vector3(0, 0, -0.11), vector1.add(vector2));
	}
	
	@Test
	public void subTestPositiveValues() {
		vector1 = new Vector3(1.11, 2.22, 3.33);
		vector2 = new Vector3(4.44, 5.55, 6.66);
		
		Assert.assertEquals(new Vector3(-3.33, -3.33, -3.33), vector1.sub(vector2));
	}
	
	@Test
	public void subTestNegativeValues() {
		vector1 = new Vector3(-1.11, 2.22, -3.33);
		vector2 = new Vector3(4.44, -5.55, -6.66);
		
		Assert.assertEquals(new Vector3(-5.55, 7.77, 3.33), vector1.sub(vector2));
	}
	
	@Test
	public void subTestZeros() {
		vector1 = new Vector3(0, 0, -0.11);
		vector2 = new Vector3(0, 0, 0);
		
		Assert.assertEquals(new Vector3(0, 0, -0.11), vector1.add(vector2));
	}
	
	@Test
	public void dotProductTestPositiveValues() {
		vector1 = new Vector3(1.11, 2.22, 3.33);
		vector2 = new Vector3(4.44, 5.55, 6.66);
		
		Assert.assertEquals(24642./625, vector1.dot(vector2), DELTA);
	}
	
	@Test
	public void dotProductTestNegativeValues() {
		vector1 = new Vector3(-1.11, 2.22, -3.33);
		vector2 = new Vector3(4.44, -5.55, -6.66);
		
		Assert.assertEquals(12321./2500, vector1.dot(vector2), DELTA);
	}
	
	@Test
	public void dotProductsTestZeros() {
		vector1 = new Vector3(0, 0, -0.11);
		vector2 = new Vector3(0, 0, 0);
		
		Assert.assertEquals(0, vector1.dot(vector2), DELTA);
	}
	
	@Test
	public void crossProductTestPositiveValues() {
		vector1 = new Vector3(1.11, 2.22, 3.33);
		vector2 = new Vector3(4.44, 5.55, 6.66);
		
		Assert.assertEquals(new Vector3(-3.6963, 7.3926, -3.6963), vector1.cross(vector2));
	}
	
	@Test
	public void crossProductTestNegativeValues() {
		vector1 = new Vector3(-1.11, 2.22, -3.33);
		vector2 = new Vector3(4.44, -5.55, -6.66);
		
		Assert.assertEquals(new Vector3(-33.2667, -22.1778, -3.6963), vector1.cross(vector2));
	}
	
	@Test
	public void crossProductsTestZeros() {
		vector1 = new Vector3(0, 0, -0.11);
		vector2 = new Vector3(0, 0, 0);
		
		Assert.assertEquals(new Vector3(0, 0, 0), vector1.cross(vector2));
	}
	
	@Test
	public void scalePositiveValuesTest() {
		vector1 = new Vector3(1.11, 2.22, 3.33);
		
		Assert.assertEquals(new Vector3(0.1887, 0.3774, 0.5661), vector1.scale(0.17));
	}
	
	@Test
	public void scaleNegativeValuesNegativeScalerTest() {
		vector1 = new Vector3(-1.11, -2.22, -3.33);
		
		Assert.assertEquals(new Vector3(0.1887, 0.3774, 0.5661), vector1.scale(-0.17));
	}
	
	@Test
	public void scaleWithZeroTest() {
		vector1 = new Vector3(-1.11, -2.22, -3.33);
		
		Assert.assertEquals(new Vector3(0, 0, 0), vector1.scale(0));
	}
	
	@Test
	public void cosAngleTestPositiveValues() {
		vector1 = new Vector3(1.11, 2.22, 3.33);
		vector2 = new Vector3(4.44, 5.55, 6.66);
		
		Assert.assertEquals(0.974631846, vector1.cosAngle(vector2), DELTA);
	}
	
	@Test
	public void cosAngleTestNegativeValues() {
		vector1 = new Vector3(-1.11, 2.22, -3.33);
		vector2 = new Vector3(4.44, -5.55, -6.66);
		
		Assert.assertEquals(0.12182898, vector1.cosAngle(vector2), DELTA);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void cosAngleTestZeros() {
		vector1 = new Vector3(0, 0, -0.11);
		vector2 = new Vector3(0, 0, 0);
		
		Assert.assertEquals(0.974631846, vector1.cosAngle(vector2), DELTA);
	}
	
	@Test
	public void testToArray() {
		vector1 = new Vector3(-1.11, 2.22, -3.33);
		Assert.assertEquals("(-1.110000, 2.220000, -3.330000)", vector1.toString());
	}
	
	
	

}
