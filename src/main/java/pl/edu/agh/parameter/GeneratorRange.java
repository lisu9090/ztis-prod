package pl.edu.agh.parameter;

public class GeneratorRange {
    private Double temperatureRange = 0.0;
    private Double flexibilityRange = 0.0;
    private Double surfaceRange = 0.0;
    private Double amountRange = 0.0;
    private Double stiffnessRange = 0.0;
    private Double volumeRange = 0.0;
    private Double massRange = 0.0;
    private Double percentage;

    public Double getTemperatureRange() {
        return percentage != null ? temperatureRange * percentage : temperatureRange;
    }

    public Double getFlexibilityRange() {
        return percentage != null ? flexibilityRange * percentage : flexibilityRange;
    }

    public Double getSurfaceRange() {
        return percentage != null ? surfaceRange * percentage : surfaceRange;
    }

    public Double getAmountRange() {
        return percentage != null ? amountRange * percentage : amountRange;
    }

    public Double getStiffnessRange() {
        return percentage != null ? stiffnessRange * percentage : stiffnessRange;
    }

    public Double getVolumeRange() {
        return percentage != null ? volumeRange * percentage : volumeRange;
    }

    public Double getMassRange() {
        return percentage != null ? massRange * percentage : massRange;
    }

    public Double getPercentage() {
        return percentage;
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
            result.flexibilityRange = value;
            return this;
        }
        
        public Builder percentage(Double value) {
            result.percentage = value;
            return this;
        }

        public GeneratorRange build() {
            return result;
        }
    }
}
