package pl.edu.agh.random;

public enum GenNames {
    NOMINAL("Nominal"),
    GAUSS("Gauss"),
    GEOMETRICAL("Geometrical"),
    POISSON("Poisson"),
    BETA("Beta");

    private String name;

    GenNames(String name) {
        this.name = name;
    }
}
