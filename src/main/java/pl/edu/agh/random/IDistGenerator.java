/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.agh.random;

/**
 *
 * @author Tomek
 */
public interface IDistGenerator {
    public Double generate(Double paramValue, Double range); //paramValue - warto�� parametru, range - szeroko�� przedzia�u losowania
}
