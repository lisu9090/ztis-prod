package pl.edu.agh.random;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DistributionParams {

    private StringProperty paramOne;
    private StringProperty paramTwo;

    public DistributionParams(String paramOne, String paramTwo) {
        this.setParamOne(paramOne);
        this.setParamTwo(paramTwo);
    }

    public void setParamOne(String value) { paramOneProperty().set(value); }
    public String getParamOne() { return paramOneProperty().get(); }
    public StringProperty paramOneProperty() {
        if (paramOne == null) paramOne = new SimpleStringProperty(this, "paramOne");
        return paramOne;
    }

    public void setParamTwo(String value) { paramTwoProperty().set(value); }
    public String getParamTwo() { return paramTwoProperty().get(); }
    public StringProperty paramTwoProperty() {
        if (paramTwo == null) paramTwo = new SimpleStringProperty(this, "paramTwo");
        return paramTwo;
    }



}
