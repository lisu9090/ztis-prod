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
//        addBehaviour(new CyclicBehaviour(this)
//        {
//            public void action()
//            {
//                ACLMessage reply;
//
//                ArrayList<MessageTemplate> templates = new ArrayList<>();
//                templates.add(MessageTemplate.MatchPerformative(AgentMessages.CHECK_AGENT));
//                templates.add(MessageTemplate.MatchPerformative(AgentMessages.START_PROCESS));
//                templates.add(MessageTemplate.MatchPerformative(AgentMessages.SET_PROCESS_VALUES));
//
//                ACLMessage [] checkMsg = new ACLMessage[templates.size()];
//                int counter = 0;
//                for(MessageTemplate checkState: templates){
//                    checkMsg[counter++] = receive(checkState);
//                }
//                for(ACLMessage msg: checkMsg) {
//                    if (msg != null) {
//                        switch(msg.getPerformative()) {
//                            case (AgentMessages.CHECK_AGENT):
//                                reply = new ACLMessage(AgentMessages.CHECK_AGENT);
//                                reply.setContent("success");
//                                reply.addReceiver(new AID( args[0].toString(), AID.ISLOCALNAME));
//                                send(reply);
//                                break;
//                            case (AgentMessages.SET_PROCESS_VALUES):
//                                processParameters = msg.getContent().split("\\s+");
//                                reply = new ACLMessage(AgentMessages.SET_PROCESS_VALUES_ACK);
//                                reply.setContent("success");
//                                reply.addReceiver(new AID( args[0].toString(), AID.ISLOCALNAME));
//                                send(reply);
//                                break;
//                            case (AgentMessages.START_PROCESS):
//                                String result = "";
//                                ProductionProcess process = new ProductionProcess(Double.parseDouble(processParameters[0]),
//                                                Double.parseDouble(processParameters[1]),
//                                                Double.parseDouble(processParameters[2])); //instancja processu z zainicjalizowanymi docelowymi parametrami
//                                process.setGeneratorRange(new GeneratorRange.Builder()
//                                        .temperature(60.0)
//                                        .volume(0.05)
//                                        .mass(100.0)
//                                        .stiffness(0.1)
//                                        .amount(1.0)
//                                        .surface(1.0)
//                                        .build());
//                                process.setGenerator(resolveFromName(processParameters[3]));
//                                try {
//                                    Double wjp = process.runProcess(Double.parseDouble(processParameters[4]),
//                                            Double.parseDouble(processParameters[5]),
//                                            Double.parseDouble(processParameters[6]));
//                                    result = Double.toString(wjp);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                                reply = new ACLMessage(AgentMessages.RECEIVE_RESULT);
//                                reply.addReceiver(new AID( args[0].toString(), AID.ISLOCALNAME));
//                                reply.setContent(result);
//                                send(reply);
//                                doDelete();
//                        }
//                    }
//                }
//                block();
//            }
//        });
        
        addBehaviour(new CyclicBehaviour(this)
        {
            public void action()
            {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                ACLMessage req = myAgent.receive(mt);
                if(req != null){
                    JSONObject params = new JSONObject(req.getContent());
                    runSimulations(params);
                    send(req.createReply());   
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
        prodModel.bucketSize = params.getString("bucketSize").equals("") ? null : params.getDouble("bucketSize");
        
        addBehaviour(new Behaviour(this) {
            private int step = 0;
            private boolean stop = false;
            private MessageTemplate stopTemplate = MessageTemplate.MatchContent("STOP");
            private MessageTemplate pauseTemplate = MessageTemplate.MatchContent("PAUSE");
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
                    res.setContent("STOP ACK");
                    send(res);
                    block();
                    //do poprawienia
                }
                
                ACLMessage processResult = new ACLMessage(ACLMessage.INFORM);
//                for(AID agent : processResultReceivers){
//                    processResult.addReceiver(agent);
//                }
                processResult.addReceiver((AID)args[0]);
                processResult.setContent("Simulation no: " + step + " - " + runProcess(processesJsonList));
                send(processResult);
                step++;
            }

            @Override
            public boolean done() {
                return step >= prodModel.noSim || stop;
            }
        });
    }
    
    private String runProcess(List<JSONObject> outputParamsLog){
        String outputLog;
        //process object
        ProductionProcess process = new ProductionProcess(prodModel.targetMaxTemp, prodModel.targetFlex, prodModel.targetSurface);
        process.setGenerator(prodModel.generator);
        try{
            //add rest of params (gen, gen params) and ask for help ml agents if some of params are not set.
            outputLog = "Production compleated! WJP = " + process.runProcess(prodModel.inputTemperature, prodModel.inputMass, prodModel.inputVolume);
            if(outputParamsLog != null){
                outputParamsLog.add(process.paramsToJson());
            }
        }
        catch(Exception e){
            outputLog = "Production failure! " + e;
        }
        return outputLog;
    }

//    private IDistGenerator resolveFromName(String typeName) {
//        if (typeName.equals(GenNames.NOMINAL.name()))
//            return new NomiGen();
//        else
//            return new GaussGen();
//
//    }
}
