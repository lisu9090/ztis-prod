package pl.edu.agh.agents;

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.json.JSONObject;
import pl.edu.agh.GeneticOptimization;
import pl.edu.agh.Observation;

import java.util.ArrayList;
import java.util.Iterator;

public class ProcessInputAgent extends MLAgent {
    private double best_temp = 1900;
    private double best_mass = 1600;
    private double best_volume = 2;
    private double min_wjp = Double.MAX_VALUE;
    private JSONObject recieved;

    private ArrayList<Observation> observations = new ArrayList<>();
    double[] regressionParameters;

    private void setBest(double temp, double volume, double mass){
        this.best_temp = temp;
        this.best_volume = volume;
        this.best_mass = mass;

    }

    @Override
    protected void loadRecentConfig() {

    }

    @Override
    protected void learn() {
        blockingSendReceiveForData(null);
        parseDataAndFindCurrentBest(recieved);
        System.out.println("Learned: mass "+best_mass+" volume: "+best_volume+" temp: "+best_temp);
        learnRegession();
        GeneticOptimization optimization = new GeneticOptimization();
        optimization.setFitnessParams(regressionParameters);
        Observation o = optimization.runGeneticAlgorithm();
        if(o.wjp < min_wjp){
            min_wjp = o.wjp;
            setBest(o.in_temperature, o.in_volume, o.in_mass);
            System.out.println("Saving: " + o);
        }

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
        regressionParameters = regression.estimateRegressionParameters();

    }


    private void parseDataAndFindCurrentBest(JSONObject jsonObject){
        Iterator<String> keys = jsonObject.keys();

        while(keys.hasNext()) {
            String key = keys.next();
            if (jsonObject.get(key) instanceof JSONObject) {
                JSONObject one = (JSONObject) jsonObject.get(key);

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
        };
    }

    @Override
    protected void receiveData(String data) {
        //dostaję wynik w stringu
        JSONObject jsonObject = new JSONObject(data);
        recieved = jsonObject;
//        System.out.println(jsonObject);


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

