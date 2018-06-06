package pl.edu.agh.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.edu.agh.parameter.GeneratorRange;
import pl.edu.agh.productionmodel.ProductionProcess;
import pl.edu.agh.random.NomiGen;

public class ProcessAgent extends Agent {
    protected void setup()
    {
        final int CHECK_AGENT = 100;
        final int START_PROCESS = 101;

        Object [] args = getArguments();

        addBehaviour(new CyclicBehaviour(this)
        {
            public void action()
            {
                MessageTemplate checkState = MessageTemplate.MatchPerformative(CHECK_AGENT);
                MessageTemplate startProcess = MessageTemplate.MatchPerformative(START_PROCESS);

                ACLMessage checkMsg = receive(checkState);
                ACLMessage startMsg = receive(startProcess);
                if (checkMsg!=null){
                    ACLMessage reply = new ACLMessage(CHECK_AGENT);
                    reply.setContent("success");
                    reply.addReceiver(new AID( args[0].toString(), AID.ISLOCALNAME));
                    send(reply);
                }

                if(startMsg != null) {
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
                }
                block();
            }
        });
    }
}
