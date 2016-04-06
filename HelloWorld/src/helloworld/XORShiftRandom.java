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
public class XORShiftRandom extends Random {
    
    private long seed = System.nanoTime();
    
    public XORShiftRandom(){
    }
    
    protected int next(int nbits){
        long x = seed;
        x ^= (x<<21);
        x ^= (x>>> 35);
        x ^= (x<< 4);
        seed = x;
        x &= ((1L << nbits) - 1);
        return (int)x;
    }
}
