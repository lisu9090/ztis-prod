package pl.edu.agh.random;

import org.apache.commons.math3.distribution.GeometricDistribution;

public class GeomGen implements IDistGenerator {
    private Double prob;

    public GeomGen() {
        this.prob = 0.5;
    }

    public GeomGen(Double prob) { //zostanie skonwertowana do liczby z zakresu 0.0 - 1.0
        if( prob < 0.0)
            prob = Math.abs(prob);
        if( prob == 0.0)
            prob = 5.0;
        
        this.prob = prob > 1 ? createProb(prob, 10) : prob;
    }

    private Double createProb(Double value, int dim) {
        return value/dim > 1 ? createProb(value, dim * 10) : value / dim;
    }

    @Override
    public Double generate(Double paramValue, Double range) {
        return (new GeometricDistribution(prob).inverseCumulativeProbability(Math.random())) * range + paramValue - (range / 2);
    }
}
