/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.agh.random;

import java.util.Random;

/**
 *
 * @author Tomek
 */
public class NomiGen implements IDistGenerator{

    @Override
    public Double generate(Double paramValue, Double range) {
        Random rnd = new Random();
        Double offset = paramValue - (range/2);
        
        return (rnd.nextDouble() * range) + offset;
    }


}
