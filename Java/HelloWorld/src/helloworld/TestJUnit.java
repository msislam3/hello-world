/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helloworld;

import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Mohammad Islam
 */
public class TestJUnit {
    
     @Test
    public void testNextIntUpperBoundRandomNumberShouldLessThanBound()
    {
        System.out.println("RandomGeneratorTest: testNextIntUpperBoundRandomNumberShouldLessThanBound");
        
        RandomGenerator rand = new RandomGenerator();
        int upperBound = 10;
        int result = rand.NextInt(upperBound);
        
        assertTrue("Random number should be less than upper bound",upperBound >= result);
        
        assertEquals("Upper bound should be 10", 10, upperBound);
        
        assertNotNull("generator should not be null", rand);
    }
}
