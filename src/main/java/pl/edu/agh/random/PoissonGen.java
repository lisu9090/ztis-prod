package pl.edu.agh.random;

import org.apache.commons.math3.distribution.PoissonDistribution;

public class PoissonGen implements IDistGenerator {
    private Double mean;

    public PoissonGen() {
        this.mean = 0.5;
    }

    public PoissonGen(Double mean) { //zostanie skonwertowana do liczby z zakresu 0.0 - 1.0
        mean = mean < 0 ? 0.0 : createMean(mean, 10);
    }

    private Double createMean(Double value, int dim) {
        return value/dim > 1 ? createMean(value, dim * 10) : value / dim;
    }

    @Override
    public Double generate(Double paramValue, Double range) {
        if (mean > 1 || mean < 0) {
            System.out.println("PoissonGen: mean is out of range! " + mean);
            return paramValue;
        }
        return new PoissonDistribution(range * mean, range).sample() + paramValue - (range / 2);
    }
}