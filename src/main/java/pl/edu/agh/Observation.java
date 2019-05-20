package pl.edu.agh;


public class Observation{
    public double in_temperature;
    public double in_volume;
    public double in_mass;
    public double wjp;

    public Observation(double in_temperature,double in_volume, double in_mass, double wjp){
        this.in_temperature = in_temperature;
        this.in_volume = in_volume;
        this.in_mass = in_mass;
        this.wjp = wjp;
    }

    @Override
    public String toString() {
        return "in_temperature:" + this.in_temperature + " in_voulume: " + this.in_volume
                + " in_mass: " + this.in_mass + "  wjp: " + this.wjp;
    }
}