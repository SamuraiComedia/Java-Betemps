public class Heater {
    private double temperature;
    private double min;
    private double max;
    private double increment;

    public Heater(double min, double max) {
        this.temperature = 15.0;
        this.increment = 5.0;
        this.min = min;
        this.max = max;
    }

    public void warmer() {
        if (temperature + increment <= max) {
            temperature += increment;
        }
    }

    public void cooler() {
        if (temperature - increment >= min) {
            temperature -= increment;
        }
    }

    public void setIncrement(double inc) {
        if (inc >= 0) {
            this.increment = inc;
        }
    }

    public double getTemp() {
        return temperature;
    }
}