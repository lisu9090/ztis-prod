package pl.edu.agh;

public final class GeneticOptimization {
    static double [] fitnessParams = {0.0, 1.0, 2.0, 3.0};

    public final void setFitnessParams(double [] fitnessParams){
        this.fitnessParams = fitnessParams;
        FitnessCalc.setParams(this.fitnessParams);
    }

    public static Observation runGeneticAlgorithm(){
        Population myPop = new Population(50, true);
        FitnessCalc.setParams(fitnessParams);

        int generationCount = 0;
        int maxGenerations = 3000;
        while (myPop.getFittest().getFitness() > FitnessCalc.getMinFitness()&& generationCount < maxGenerations) {
            generationCount++;
//            System.out.println("Generation: " + generationCount + " Fittest: " + myPop.getFittest().getFitness());
            myPop = Algorithm.evolvePopulation(myPop);
        }
        System.out.println("Solution found!");
        System.out.println("Generation: " + generationCount);
        System.out.println("Genes:");
        Individual result = myPop.getFittest();
        System.out.println(myPop.getFittest());
        System.out.println(myPop.getFittest().getFitness());
        Observation o = new Observation(result.getGene(0), result.getGene(1), result.getGene(2),result.getFitness());
        return o;
    }

    public static void main(String[] args) {
        runGeneticAlgorithm();


    }
}


class Algorithm {

    /* GA parameters */
    private static final double uniformRate = 0.5;
    private static final double mutationRate = 0.015;
    private static final int tournamentSize = 5;
    private static final boolean elitism = true;

    /* Public methods */

    // Evolve a population
    public static Population evolvePopulation(Population pop) {
        Population newPopulation = new Population(pop.size(), false);

        // Keep our best individual
        if (elitism) {
            newPopulation.saveIndividual(0, pop.getFittest());
        }

        // Crossover population
        int elitismOffset;
        if (elitism) {
            elitismOffset = 1;
        } else {
            elitismOffset = 0;
        }
        for (int i = elitismOffset; i < pop.size(); i++) {
            Individual indiv1 = tournamentSelection(pop);
            Individual indiv2 = tournamentSelection(pop);
            Individual newIndiv = crossover(indiv1, indiv2);
            newPopulation.saveIndividual(i, newIndiv);
        }

        // Mutate population
        for (int i = elitismOffset; i < newPopulation.size(); i++) {
            mutate(newPopulation.getIndividual(i));
        }

        return newPopulation;
    }

    // Crossover individuals
    private static Individual crossover(Individual indiv1, Individual indiv2) {
        Individual newSol = new Individual();
        // Loop through genes
        for (int i = 0; i < indiv1.size(); i++) {
            // Crossover
            if (Math.random() <= uniformRate) {
                newSol.setGene(i, indiv1.getGene(i));
            } else {
                newSol.setGene(i, indiv2.getGene(i));
            }
        }
        return newSol;
    }

    // Mutate an individual
    private static void mutate(Individual indiv) {
        // Loop through genes
        for (int i = 0; i < indiv.size(); i++) {
            if (Math.random() <= mutationRate) {
                // Create random gene
                double gene = Math.round(Math.random());
                indiv.setGene(i, gene);
            }
        }
    }

    // Select individuals for crossover
    private static Individual tournamentSelection(Population pop) {
        // Create a tournament population
        Population tournament = new Population(tournamentSize, false);
        // For each place in the tournament get a random individual
        for (int i = 0; i < tournamentSize; i++) {
            int randomId = (int) (Math.random() * pop.size());
            tournament.saveIndividual(i, pop.getIndividual(randomId));
        }
        // Get the fittest
        Individual fittest = tournament.getFittest();
        return fittest;
    }
}

class Individual {

    static int defaultGeneLength = 3;
    private double[] genes = new double[defaultGeneLength];
    private double fitness = 0;

    public void generateIndividual() {
        for (int i = 0; i < size(); i++) {
            double gene = Math.random()*30.0;
            genes[i] = gene;
        }
    }

    public static void setDefaultGeneLength(int length) {
        defaultGeneLength = length;
    }

    public double getGene(int index) {
        return genes[index];
    }

    public void setGene(int index, double value) {
        genes[index] = value;
        fitness = 0;
    }

    public int size() {
        return genes.length;
    }

    public double getFitness() {
        if (fitness == 0) {
            fitness = FitnessCalc.getFitness(this);
        }
        return fitness;
    }

    public void evaluate(){
        if (this.genes[0] <= 0.) this.genes[0] = 0;
        if (this.genes[0] > 5000.) this.genes[0] = 5000.;
        if (this.genes[1] <= 0.) this.genes[1] = 0;
        if (this.genes[1] <= 20.) this.genes[1] = 20.;
        if (this.genes[2] <= 0.) this.genes[2] = 0;
    }

    @Override
    public String toString() {
        String geneString = "";
        for (int i = 0; i < size(); i++) {
            geneString += " " + getGene(i);
        }
        return geneString;
    }
}

class Population {

    Individual[] individuals;
    public Population(int populationSize, boolean initialise) {
        individuals = new Individual[populationSize];
        if (initialise) {
            for (int i = 0; i < size(); i++) {
                Individual newIndividual = new Individual();
                newIndividual.generateIndividual();
                saveIndividual(i, newIndividual);
            }
        }
    }

    public Individual getIndividual(int index) {
        return individuals[index];
    }

    public Individual getFittest() {
        Individual fittest = individuals[0];
        for (int i = 0; i < size(); i++) {
            if (fittest.getFitness() > getIndividual(i).getFitness()) {
                fittest = getIndividual(i);
            }
        }
        return fittest;
    }

    public int size() {
        return individuals.length;
    }

    public void saveIndividual(int index, Individual indiv) {
        indiv.evaluate();
        individuals[index] = indiv;
    }
}

class FitnessCalc {

    static double[] params = new double[3];

    public static void setParams(double[] newParams) {
        params = newParams;
    }


    static double getFitness(Individual individual) {
        double fitness = params[0] + individual.getGene(0)*params[1] +
                individual.getGene(1)*params[2] + individual.getGene(2)*params[3];
        return Math.abs(fitness);
    }

    static double getMinFitness(){
        return 0.0;
    }
}
/*
class Observation   {
    public double in_temperature;
    public double in_volume;
    public double in_mass;
    public double wjp;

    Observation(double in_temperature,double in_volume, double in_mass, double wjp){
        this.in_temperature = in_temperature;
        this.in_volume = in_volume;
        this.in_mass = in_mass;
        this.wjp = wjp;
    }

    @Override
    public String toString() {
        return "in_temperature:" + this.in_temperature + " in_volume: " + this.in_volume
                + " in_mass: " + this.in_mass + "  wjp: " + this.wjp;
    }


    public Observation newInstance() {
        final Random random = RandomRegistry.getRandom();
        double temp = random.nextDouble()*1000;
        double volume = random.nextDouble()*2;
        double mass = random.nextDouble()*1000;
        double wjp = 0;
        return new Observation(temp, volume, mass, wjp);
    }

    public Observation newInstance(Object value) {
        final Random random = RandomRegistry.getRandom();
        double temp = random.nextDouble()*1000;
        double volume = random.nextDouble()*2;
        double mass = random.nextDouble()*1000;
        double wjp = 0;
        return new Observation(temp, volume, mass, wjp);
    }
    public boolean isValid() {
        return this.in_temperature > 0 && this.in_mass > 0. && this.in_volume >0
                && this.in_temperature < 3000 && this.in_mass < 10 && this.in_volume < 100
                && this.wjp > 0;
    }

    public Object get() {
        return newInstance();
    }
}*/