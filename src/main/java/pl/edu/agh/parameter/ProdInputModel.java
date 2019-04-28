/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.agh.parameter;

/**
 *
 * @author Tomek
 */
public class ProdInputModel {
    public ProdInputModel(int noSim, String generator, Double targetMaxTemp, Double targetFlex, Double targetSurface){
        this.noSim = noSim;
        this.generator = generator;
        this.targetMaxTemp = targetMaxTemp;
        this.targetFlex = targetFlex;
        this.targetSurface = targetSurface;
    }
    
    public int noSim;
    public String generator;
    
    public Double targetMaxTemp;
    public Double targetFlex;
    public Double targetSurface;
    
    public Double inputTemperature;
    public Double inputMass;
    public Double inputVolume;
    
    public Double deltaTemp;
    public Double bucketSize;  
}
