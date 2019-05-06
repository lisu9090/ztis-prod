package pl.edu.agh.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.edu.agh.parameter.GeneratorRange;
import pl.edu.agh.productionmodel.ProductionProcess;
import pl.edu.agh.random.*;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import pl.edu.agh.parameter.Parameter;
import pl.edu.agh.parameter.ProdInputModel;
import pl.edu.agh.parameter.Temperature;

public class ProcessAgent extends Agent {
    String [] processParameters;
    private ProdInputModel prodModel;
    private List<AID> processResultReceivers = new ArrayList();
    private Object [] args;
    
    protected void setup()
    {
        args = getArguments();
        
        addBehaviour(new CyclicBehaviour(this)
        {
            public void action()
            {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                ACLMessage req = myAgent.receive(mt);
                if(req != null){
                    JSONObject params = new JSONObject(req.getContent());
                    runSimulations(params);
                    ACLMessage res = req.createReply();
                    res.setContent("START ACK");
                    send(res);   
                }
                else{
                    block();
                }
            }
        });
    }
    
    private void runSimulations(JSONObject params){       
        prodModel = new ProdInputModel(params.getInt("noSim"), 
                GenNames.valueOf(params.getString("generator")),
                params.getString("param1").equals("") ? null : params.getDouble("param1"), 
                params.getString("param2").equals("") ? null : params.getDouble("param2"),
                params.getDouble("targetMaxTemperature"), 
                params.getDouble("targetFlex"), 
                params.getDouble("targetSurface"));
        
        prodModel.inputTemperature = params.getString("temperature").equals("") ? null : params.getDouble("temperature");
        prodModel.inputMass = params.getString("mass").equals("") ? null : params.getDouble("mass");
        prodModel.inputVolume = params.getString("volume").equals("") ? null : params.getDouble("volume");
        prodModel.deltaTemp = params.getString("deltaTemperature").equals("") ? null : params.getDouble("deltaTemperature");
        prodModel.bucketSize = params.getString("bucketSize").equals("") ? null : Parameter.Size.valueOf(params.getString("bucketSize"));
        
        addBehaviour(new Behaviour(this) {
            private int step = 0;
            private boolean stop = false;
            private MessageTemplate stopTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchContent("STOP"));
            private MessageTemplate pauseTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchContent("PAUSE"));
            private List<JSONObject> processesJsonList = new ArrayList();
                    
            @Override
            public void action() {
                ACLMessage shouldStop = receive(stopTemplate);
                ACLMessage shouldPause = receive(pauseTemplate);
                if(shouldStop != null){
                    ACLMessage res = shouldStop.createReply();
                    res.setContent("STOP ACK");
                    send(res);
                    stop = true;
                    return;
                }
                if(shouldPause != null){
                    ACLMessage res = shouldPause.createReply();
                    res.setContent("PASUE ACK");
                    send(res);
                    block();
                    //do poprawienia
                }
                
                ProductionProcess result = runProcess();
                processesJsonList.add(result.paramsToJson());
                ACLMessage processResult = new ACLMessage(ACLMessage.INFORM);
                processResult.addReceiver((AID)args[0]);
                try{
                    processResult.setContent("Simulation no: " + step + " - " + result.computeWJP().toString());
                }
                catch(Exception e){
                     processResult.setContent("Failure: " + e);
                }
                send(processResult);
                step++;
            }

            @Override
            public boolean done() {
                if(step >= prodModel.noSim || stop){
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.addReceiver((AID)args[2]);
                    JSONObject data = new JSONObject();
                    processesJsonList.forEach((result) -> {
                        data.put(result.get("pid").toString(), result);
                    });
                    msg.setContent(data.toString());
                    myAgent.send(msg);
                    
                    msg = new ACLMessage(ACLMessage.INFORM);
                    msg.addReceiver((AID)args[0]);
                    msg.setContent("SIMULATION DONE");
                    myAgent.send(msg);
                    
                    return true;
                }
                return false;
            }
        });
    }
 
    private ProductionProcess runProcess(){
        ProductionProcess process = new ProductionProcess(prodModel.targetMaxTemp, prodModel.targetFlex, prodModel.targetSurface);
        process.setGenerator(prodModel.generator);
        try{
            if(prodModel.inputTemperature == null || prodModel.inputVolume == null || prodModel.inputMass == null){
                JSONObject content = process.paramsToJson();
                content.put("user_temperature", prodModel.inputTemperature == null ? "" : prodModel.inputTemperature.toString());
                content.put("user_mass", prodModel.inputMass == null ? "" : prodModel.inputMass.toString());
                content.put("user_volume", prodModel.inputVolume == null ? "" : prodModel.inputVolume.toString());
                
                JSONObject help = blockingSendReciveHelpRequest((AID)args[3], content.toString());
                
                if(prodModel.inputTemperature == null)
                    prodModel.inputTemperature = help.getDouble("temperature");
                
                if(prodModel.inputMass == null)
                    prodModel.inputMass = help.getDouble("mass");
                
                if(prodModel.inputVolume == null)
                    prodModel.inputVolume = help.getDouble("volume");
            }
            process.firstStep(prodModel.inputTemperature, prodModel.inputVolume, prodModel.inputMass);
            
            if(prodModel.deltaTemp == null || prodModel.bucketSize == null){
                JSONObject content = process.paramsToJson();
                content.put("user_deltaTemp", prodModel.deltaTemp == null ? "" : prodModel.deltaTemp.toString());
                content.put("user_bucketSize", prodModel.bucketSize == null ? "" : prodModel.bucketSize.toString());
                
                JSONObject help = blockingSendReciveHelpRequest((AID)args[4], content.toString());
                
                if(prodModel.deltaTemp == null)
                    prodModel.deltaTemp = help.getDouble("deltaTemp");
                
                if(prodModel.bucketSize == null)
                    prodModel.bucketSize = Parameter.Size.valueOf(help.getString("bucketSize"));
            }
            process.secondStep(prodModel.deltaTemp, prodModel.bucketSize);
            
            process.thirdStep();
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        } 
        return process;
    }
    
    private JSONObject blockingSendReciveHelpRequest(AID id, String content){
        ACLMessage helpRequest = new ACLMessage(ACLMessage.CFP);
        helpRequest.addReceiver(id);
        helpRequest.setContent(content);
        send(helpRequest);
        MessageTemplate respTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.PROPOSE), MessageTemplate.MatchSender(id));
        ACLMessage resopnse = blockingReceive();
        return new JSONObject(resopnse.getContent());
    }
}
