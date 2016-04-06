/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helloworld;

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
        
        //RandomGenerator rand = new RandomGenerator();
        XORShiftRandom rand = new XORShiftRandom();
        
        int i;
        
         i = rand.nextInt();
         System.out.println(i);
        
         i = rand.nextInt(100);
         System.out.println(i); 
    }
    
}
