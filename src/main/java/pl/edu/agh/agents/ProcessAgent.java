package pl.edu.agh.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.edu.agh.parameter.GeneratorRange;
import pl.edu.agh.productionmodel.ProductionProcess;
import pl.edu.agh.random.*;

import java.util.ArrayList;

public class ProcessAgent extends Agent {


    String [] processParameters;
    protected void startProcess(){

    }

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

                /*
                ProductionProcess process = new ProductionProcess(400.0, 1000.0, 270.0); //instancja processu z zainicjalizowanymi docelowymi parametrami

                //Ustawaimy zakresy przedzialow losowania
                process.setGeneratorRange(new GeneratorRange.Builder()
                        .temperature(60.0)
                        .volume(0.05)
                        .mass(100.0)
                        .stiffness(0.1)
                        .amount(1.0)
                        .surface(1.0)
                        .build());
                process.setGenerator(new NomiGen());

                try {
                    Double wjp = process.runProcess(1900.0, 2.0, 16000.0);
                } catch (Exception e) {
                    e.printStackTrace();

                }
                */
                block();
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
