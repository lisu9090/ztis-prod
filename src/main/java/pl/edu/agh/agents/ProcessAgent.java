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
import org.json.JSONObject;

public class ProcessAgent extends Agent {
    String [] processParameters;

    protected void setup()
    {
        Object [] args = getArguments();

        addBehaviour(new CyclicBehaviour(this)
        {
            public void action()
            {
                ACLMessage reply;

                ArrayList<MessageTemplate> templates = new ArrayList<>();
                templates.add(MessageTemplate.MatchPerformative(AgentMessages.CHECK_AGENT));
                templates.add(MessageTemplate.MatchPerformative(AgentMessages.START_PROCESS));
                templates.add(MessageTemplate.MatchPerformative(AgentMessages.SET_PROCESS_VALUES));

                ACLMessage [] checkMsg = new ACLMessage[templates.size()];
                int counter = 0;
                for(MessageTemplate checkState: templates){
                    checkMsg[counter++] = receive(checkState);
                }
                for(ACLMessage msg: checkMsg) {
                    if (msg != null) {
                        switch(msg.getPerformative()) {
                            case (AgentMessages.CHECK_AGENT):
                                reply = new ACLMessage(AgentMessages.CHECK_AGENT);
                                reply.setContent("success");
                                reply.addReceiver(new AID( args[0].toString(), AID.ISLOCALNAME));
                                send(reply);
                                break;
                            case (AgentMessages.SET_PROCESS_VALUES):
                                processParameters = msg.getContent().split("\\s+");
                                reply = new ACLMessage(AgentMessages.SET_PROCESS_VALUES_ACK);
                                reply.setContent("success");
                                reply.addReceiver(new AID( args[0].toString(), AID.ISLOCALNAME));
                                send(reply);
                                break;
                            case (AgentMessages.START_PROCESS):
                                String result = "";
                                ProductionProcess process = new ProductionProcess(Double.parseDouble(processParameters[0]),
                                                Double.parseDouble(processParameters[1]),
                                                Double.parseDouble(processParameters[2])); //instancja processu z zainicjalizowanymi docelowymi parametrami
                                process.setGeneratorRange(new GeneratorRange.Builder()
                                        .temperature(60.0)
                                        .volume(0.05)
                                        .mass(100.0)
                                        .stiffness(0.1)
                                        .amount(1.0)
                                        .surface(1.0)
                                        .build());
                                process.setGenerator(resolveFromName(processParameters[3]));
                                try {
                                    Double wjp = process.runProcess(Double.parseDouble(processParameters[4]),
                                            Double.parseDouble(processParameters[5]),
                                            Double.parseDouble(processParameters[6]));
                                    result = Double.toString(wjp);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                reply = new ACLMessage(AgentMessages.RECEIVE_RESULT);
                                reply.addReceiver(new AID( args[0].toString(), AID.ISLOCALNAME));
                                reply.setContent(result);
                                send(reply);
                                doDelete();
                        }
                    }
                }
                block();
            }
        });
        
        addBehaviour(new CyclicBehaviour(this)
        {
            public void action()
            {
                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                ACLMessage req = myAgent.receive(mt);
                JSONObject params = new JSONObject(req.getContent());
                runSimulations(params);
                send(req.createReply());
                block();
            }
        });
    }
    
    private void runSimulations(JSONObject params){
    
        //parse params from json
        //add behaviour which will run simulations
        addBehaviour(new Behaviour() {
            private int step = 0;
            private boolean stop = false;
            @Override
            public void action() {
                //get pause || stop messages
                //if stop set stop var and return
                //run if !pause
                //runProcess(params);
                step++;
            }

            @Override
            public boolean done() {
                return true; //step >=params.noSim || stop
            }
        });
    }

    private IDistGenerator resolveFromName(String typeName) {
        if (typeName.equals(GenNames.NOMINAL.name()))
            return new NomiGen();
        else
            return new GaussGen();

    }
}
