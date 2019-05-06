package pl.edu.agh.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import org.json.JSONObject;

public class ProcessInputAgent extends MLAgent {

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
        
        if(jsonToSend.getString("user_temperature").equals(""))
            jsonToSend.put("temperature", 1900.0);
        
        if(jsonToSend.getString("user_mass").equals(""))
            jsonToSend.put("mass", 16000.0);
        
        if(jsonToSend.getString("user_volume").equals(""))
            jsonToSend.put("volume", 2.0);
        
        return jsonToSend;
    }
    
}
