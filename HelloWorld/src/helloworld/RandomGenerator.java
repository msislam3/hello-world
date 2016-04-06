/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helloworld;

import java.util.Random;

/**
 *
 * @author Mohammad Saiful Islam
 */
public class RandomGenerator {
    
    private Random rand;
    
    public RandomGenerator(){
        
        rand = new Random();
        int num =  rand.nextInt(4);
        
        System.out.println(num);
    }
    
    /**
     * Method that returns a pseudo random number from 0 (inclusive) to upper bound (exclusive) 
     * 
     * @param upperBound The upper bound (exclusive). Must be positive
     * @return A pseudo random number from 0 to upperBound (exclusive) 
     * @throws IllegalArgumentException if upperBound is not positive 
    **/
    public int NextInt(int upperBound)
    {
        if (upperBound <= 0) {
            throw new IllegalArgumentException("upper bound has to be positive");
        }
        
        return rand.nextInt(upperBound);
    }
    
    public int NextInt()
    {
        return rand.nextInt();
    }
}