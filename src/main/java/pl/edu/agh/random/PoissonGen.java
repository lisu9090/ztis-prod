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
    
    public PoissonGen(){
        this.mean = 0.5;
    }
    
    public PoissonGen(Double mean) { //zostanie skonwertowana do liczby z zakresu 0.0 - 1.0
        if(mean<0)
            this.mean = 0.0;
        else
            this.mean = createMean(mean, 10);
    }
    
    private final Double createMean(Double value, int dim){
        if((value/dim)>1)
            return createMean(value, dim * 10);
        else
            return value/dim;
    }
    
    @Override
    public Double generate(Double paramValue, Double range) {
        if(mean > 1 || mean < 0)
        {
            System.out.println("PoissonGen: mean is out of range! " + mean);
            return paramValue;
        }
        PoissonDistribution poisson = new PoissonDistribution(range * mean, range);
        Double offset = paramValue - (range/2);
        
        return (poisson.sample() + offset);
    }


}
