package pl.edu.agh.random;

import org.apache.commons.math3.distribution.GeometricDistribution;

public class GeomGen implements IDistGenerator {
    private Double prob;

    public GeomGen() {
        this.prob = 0.5;
    }

    public GeomGen(Double prob) {
        this.prob = prob;
    }

    @Override
    public Double generate(Double paramValue, Double range) {
        return (new GeometricDistribution(prob).inverseCumulativeProbability(Math.random() * range)) +
                paramValue - (range / 2);
    }
}
