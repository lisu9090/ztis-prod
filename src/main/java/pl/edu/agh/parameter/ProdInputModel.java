/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.agh.parameter;

import pl.edu.agh.random.BetaGen;
import pl.edu.agh.random.GaussGen;
import pl.edu.agh.random.GenNames;
import pl.edu.agh.random.GeomGen;
import pl.edu.agh.random.IDistGenerator;
import pl.edu.agh.random.NomiGen;
import pl.edu.agh.random.PoissonGen;

/**
 *
 * @author Tomek
 */
public class ProdInputModel {
    public ProdInputModel(int noSim, GenNames generator, Double param1, Double param2, Double targetMaxTemp, Double targetFlex, Double targetSurface){
        this.noSim = noSim;      
        this.targetMaxTemp = targetMaxTemp;
        this.targetFlex = targetFlex;
        this.targetSurface = targetSurface;
        
        switch(generator){
            case GAUSS:
                this.generator = new GaussGen();
                break;
            case BETA:
                this.generator = new BetaGen(param1, param2);
            break;
            case POISSON:
                this.generator = new PoissonGen(param1);
            break;
            case GEOMETRICAL:
                this.generator = new GeomGen(param1);
            break;
            default:
                this.generator = new NomiGen();
            break;
        }
    }
    
    public int noSim;
    public IDistGenerator generator;
    
    public Double targetMaxTemp;
    public Double targetFlex;
    public Double targetSurface;
    
    public Double inputTemperature;
    public Double inputMass;
    public Double inputVolume;
    
    public Double deltaTemp;
    public Double bucketSize;  
}
