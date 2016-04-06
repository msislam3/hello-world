/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helloworld;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Shubho
 */
public class RandomGeneratorTest {
    
    public RandomGeneratorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of NextInt method, of class RandomGenerator.
     */
    @Test
    public void testNextInt_int() {
        System.out.println("RandomGeneratorTest: testNextInt_int");
        int upperBound;
        RandomGenerator instance = new RandomGenerator();
        
        try{
            upperBound = 0;
            instance.NextInt(upperBound);
            fail("upperBound = 0. IllegalArgumentException expected");
            
            upperBound = -1;
            instance.NextInt(upperBound);
            fail("upperBound = -1. IllegalArgumentException expected");
        }catch(IllegalArgumentException ex)
        {
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void textNextIntShouldThrowException(){
        RandomGenerator rand = new RandomGenerator();
        rand.NextInt(-1);
    }
            
    
    @Test
    public void testNextIntUpperBoundRandomNumberShouldLessThanBound()
    {
        System.out.println("RandomGeneratorTest: testNextIntUpperBoundRandomNumberShouldLessThanBound");
        
        RandomGenerator rand = new RandomGenerator();
        int upperBound = 10;
        int result = rand.NextInt(upperBound);
        
        assertTrue("Random number should be less than upper bound",upperBound >= result);
    }
}
