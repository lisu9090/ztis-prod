/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.agh.random;

import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;

/**
 *
 * @author Tomek
 */
public class PoissonGen implements IDistGenerator{
    private Double mean;
    
    public PoissonGen(Double mean) {
        this.mean = mean;
    }
    
    @Override
    public Double generate(Double paramValue, Double range) {
        if(mean > range || mean < 0)
        {
            System.out.println("PoissonGen: mean is out of range!");
            return paramValue;
        }
        PoissonDistribution poisson = new PoissonDistribution(mean, range);
        Double offset = paramValue - (range/2);
        
        return (poisson.sample() + offset);
    }


}
