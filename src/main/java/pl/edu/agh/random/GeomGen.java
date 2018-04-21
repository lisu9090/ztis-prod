/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.agh.random;

import org.apache.commons.math3.distribution.GeometricDistribution;
/**
 *
 * @author Tomek
 */
public class GeomGen implements IDistGenerator{
    private Double prob;

    public GeomGen() {
        this.prob = 0.5;
    }

    public GeomGen(Double prob) {
        this.prob = prob;
    }   
    
    @Override
    public Double generate(Double paramValue, Double range) {
        Double offset = paramValue - (range/2);
        GeometricDistribution gd = new GeometricDistribution(prob);
        
        return (gd.inverseCumulativeProbability(Math.random() * range)) + offset;
    }


}
