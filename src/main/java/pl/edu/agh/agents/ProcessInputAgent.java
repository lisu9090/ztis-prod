package pl.edu.agh.agents;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ProcessInputAgent extends MLAgent {
    private double best_temp = 1900;
    private double best_mass = 1600;
    private double best_volume = 2;
    private JSONObject recieved;
    private double regressionParameters[];

    private ArrayList<Observation> observations = new ArrayList<>();
    @Override
    protected void loadRecentConfig() {

    }

    @Override
    protected void learn() {
        blockingSendReceiveForData(null);
        parseDataAndFindCurrentBest(recieved);
        System.out.println("Learned: mass "+best_mass+" volume: "+best_volume+" temp: "+best_temp);
        learnRegession();
    }

    private void learnRegession(){
        int num_observations = observations.size();
        double [] y = new double[num_observations];
        double [][] x = new double[num_observations][];

        for(int i = 0; i<num_observations; i++){
            Observation observation =  observations.get(i);
            y[i] = observation.wjp;
            x[i] = new double []{observation.in_temperature, observation.in_volume, observation.in_mass};
        }

        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        regression.newSampleData(y, x);

        double[] regressionParameters = regression.estimateRegressionParameters();
    }


    private void parseDataAndFindCurrentBest(JSONObject jsonObject){
        Iterator<String> keys = jsonObject.keys();

        double min_wjp = Double.MAX_VALUE;
        while(keys.hasNext()) {
            String key = keys.next();
            if (jsonObject.get(key) instanceof JSONObject) {
                // do something with jsonObject here
                System.out.println(key);
                System.out.println(jsonObject.get(key));
                JSONObject one = (JSONObject) jsonObject.get(key);
                System.out.println(one);
                System.out.println(one.get("wjp"));
                double wjp = Double.parseDouble((String)one.get("wjp"));
                JSONObject stage_1 = (JSONObject) one.get("stage_1");
                try {
                    double in_temperature = (double)(int) stage_1.get("in_temperature");
                    double in_volume = (double)(int) stage_1.get("in_volume");
                    double in_mass = (double)(int) stage_1.get("in_mass");
                    observations.add(new Observation(in_temperature, in_volume, in_mass, wjp));
                    if (wjp < min_wjp) {
                        min_wjp = wjp;
                        best_mass = in_mass;
                        best_temp = in_temperature;
                        best_volume = in_volume;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        System.out.println(min_wjp);
    }

    @Override
    protected void receiveData(String data) {
        //dostaję wynik w stringu
        JSONObject jsonObject = new JSONObject(data);
        recieved = jsonObject;
        System.out.println(jsonObject);
        //zapisać dane w tablicy i potem odczytać w learn

    }

    @Override
    protected JSONObject decision(String params) {
        JSONObject jsonToSend = new JSONObject(params);
        
        if(jsonToSend.getString("user_temperature").equals(""))
            jsonToSend.put("temperature", best_temp);
        
        if(jsonToSend.getString("user_mass").equals(""))
            jsonToSend.put("mass", best_mass);
        
        if(jsonToSend.getString("user_volume").equals(""))
            jsonToSend.put("volume", best_volume);

        return jsonToSend;
    }
    
}

class Observation{
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

}
