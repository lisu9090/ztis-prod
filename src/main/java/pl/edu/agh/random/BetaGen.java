package pl.edu.agh.random;

import org.apache.commons.math3.distribution.BetaDistribution;

public class BetaGen implements IDistGenerator {
    private Double alpha, beta;

    public BetaGen() {
        this.alpha = 0.5;
        this.beta = 0.5;
    }

    public BetaGen(Double alpha, Double beta) {
        if(alpha < 0){
            this.alpha = Math.abs(alpha);
        }
        else
            this.alpha = alpha;
        if(beta < 0){
            this.beta = Math.abs(beta);
        }
        else
            this.beta = beta;
    }

    @Override
    public Double generate(Double paramValue, Double range) {
        return (new BetaDistribution(alpha, beta).inverseCumulativeProbability(Math.random()) * range) +
                paramValue - (range / 2);
    }
}
