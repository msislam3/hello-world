/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helloworld;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 *
 * @author Mohammad Saiful Islam
 */
public class HelloWorld {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Hello World");
        
        JUnit(); 
        Random();
    }
    
    private static void JUnit(){
        Result result = JUnitCore.runClasses(TestJUnit.class);
        for (Failure failure : result.getFailures()){
            System.out.println(failure.toString());
        }
        System.out.println(result.wasSuccessful());
    }
    
    private static void Random(){
        XORShiftRandom rand = new XORShiftRandom();
        
        int i;
        
         i = rand.nextInt();
         System.out.println(i);
        
         i = rand.nextInt(100);
         System.out.println(i);
    
         RandomGenerator rand1 = new RandomGenerator();
         rand1.NextInt(10);
    }
}
