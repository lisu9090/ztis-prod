package pl.edu.agh.random;

import org.apache.commons.math3.distribution.BetaDistribution;

public class BetaGen implements IDistGenerator {
    private Double alpha, beta;

    public BetaGen() {
        this.alpha = 0.5;
        this.beta = 0.5;
    }

    public BetaGen(Double alpha, Double beta) {
        this.alpha = alpha;
        this.beta = beta;
    }

    @Override
    public Double generate(Double paramValue, Double range) {
        return (new BetaDistribution(alpha, beta).inverseCumulativeProbability(Math.random()) * range) +
                paramValue - (range / 2);
    }
}
