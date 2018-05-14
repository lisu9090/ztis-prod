package pl.edu.agh.parameter;

public class GeneratorRange {
    private Double temperatureRange = 0.0;
    private Double flexibilityRange = 0.0;
    private Double surfaceRange = 0.0;
    private Double amountRange = 0.0;
    private Double stiffnessRange = 0.0;
    private Double volumeRange = 0.0;
    private Double massRange = 0.0;

    public Double getTemperatureRange() {
        return temperatureRange;
    }

    public Double getFlexibilityRange() {
        return flexibilityRange;
    }

    public Double getSurfaceRange() {
        return surfaceRange;
    }

    public Double getAmountRange() {
        return amountRange;
    }

    public Double getStiffnessRange() {
        return stiffnessRange;
    }

    public Double getVolumeRange() {
        return volumeRange;
    }

    public Double getMassRange() {
        return massRange;
    }

    private GeneratorRange() {
    }

    public static class Builder {
        private GeneratorRange result;

        public Builder() {
            result = new GeneratorRange();
        }

        public Builder temperature(Double value) {
            result.temperatureRange = value;
            return this;
        }

        public Builder volume(Double value) {
            result.volumeRange = value;
            return this;
        }

        public Builder mass(Double value) {
            result.massRange = value;
            return this;
        }

        public Builder stiffness(Double value) {
            result.stiffnessRange = value;
            return this;
        }

        public Builder amount(Double value) {
            result.amountRange = value;
            return this;
        }

        public Builder surface(Double value) {
            result.surfaceRange = value;
            return this;
        }

        public Builder flexibility(Double value) {
            result.volumeRange = value;
            return this;
        }

        public GeneratorRange build() {
            return result;
        }
    }
}
