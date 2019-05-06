/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.edu.agh.agents;

import org.json.JSONObject;
import pl.edu.agh.parameter.Parameter;

/**
 *
 * @author Tomek
 */
public class SecondStageAgent extends MLAgent{

    @Override
    protected void loadRecentConfig() {
        
    }

    @Override
    protected void learn() {
        
    }

    @Override
    protected void receiveData(String data) {
        
    }

    @Override
    protected JSONObject decision(String params) {
        JSONObject jsonToSend = new JSONObject(params);
        Double deltaTemp = 1200.0;
        
        if(jsonToSend.getString("user_deltaTemp").equals(""))
            jsonToSend.put("deltaTemp", deltaTemp);
        
        if(jsonToSend.getString("user_bucketSize").equals("")){
            Double temp = jsonToSend.getJSONObject("stage_1").getDouble("out_temperature") - deltaTemp;
            if(temp <= 400)
                jsonToSend.put("bucketSize", Parameter.Size.SMALL.name());
            else if(temp <= 600)
                jsonToSend.put("bucketSize", Parameter.Size.MEDIUM.name());
            else
                jsonToSend.put("bucketSize", Parameter.Size.LARGE.name());
        }           
        
        return jsonToSend;
    }
    
}
