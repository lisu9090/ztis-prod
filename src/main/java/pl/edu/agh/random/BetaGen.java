/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.agh.random;

import org.apache.commons.math3.distribution.BetaDistribution;

/**
 *
 * @author Tomek
 */
public class BetaGen implements IDistGenerator{
    private Double alpha, beta;
    
    public BetaGen(){
        this.alpha = 0.5;
        this.beta = 0.5;
    }
    
    public BetaGen(Double alpha, Double beta){
       this.alpha = alpha;
       this.beta = beta;
    }
    @Override
    public Double generate(Double paramValue, Double range) {
        Double offset = paramValue - (range/2);
        
        BetaDistribution bd = new BetaDistribution(alpha, beta);
           
        return (bd.inverseCumulativeProbability(Math.random()) * range) + offset;   
    } 
}
