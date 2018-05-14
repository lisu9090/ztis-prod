package pl.edu.agh.random;

import java.util.Random;

public class NomiGen implements IDistGenerator{

    @Override
    public Double generate(Double paramValue, Double range) {
        return (new Random().nextDouble() * range) + paramValue - (range/2);
    }
}