package pl.edu.agh.random;

import java.util.Random;

public class GaussGen implements IDistGenerator {

    @Override
    public Double generate(Double paramValue, Double range) {
        return (new Random().nextGaussian() * range) + paramValue - (range / 2);
    }

}
