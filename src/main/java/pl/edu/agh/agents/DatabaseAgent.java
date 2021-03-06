package pl.edu.agh.agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import pl.edu.agh.db.DBManager;
import pl.edu.agh.parameter.ProcessJson;
import pl.edu.agh.random.AgentMessages;

import java.util.List;
import java.util.stream.Stream;
import org.json.JSONObject;

public class DatabaseAgent extends Agent {
    private Object[] args;
    
    protected void setup()
    {
        args = getArguments();

        addBehaviour(new CyclicBehaviour(this)
        {
            public void action()
            {
                MessageTemplate putDataInform = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                ACLMessage request = receive(putDataInform);
                if(request != null){
                    JSONObject payload = new JSONObject(request.getContent());
                    payload.keySet().forEach(item -> {
                        DBManager.getINSTANCE().saveProcess(new ProcessJson(Long.parseLong(item), payload.getJSONObject(item).toString()));
                    });
                }
                else
                    block();
            }
        });
        
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                MessageTemplate putDataInform = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
                ACLMessage request = receive(putDataInform);
                if(request != null){ 
                    ACLMessage response = request.createReply();
                    switch(request.getContent()){
                        case "ALL":
                            JSONObject data = new JSONObject();
                            DBManager.getINSTANCE().findAllProcesses().forEach(item -> {
                                data.put(item.getId().toString(), item.parseToJSONObject());
                            });
                            response.setContent(data.toString());
                            break;
                        default:
                            try{
                                Long id = Long.parseLong(request.getContent());
                                // dokonczyc
                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }
                            break; 
                   }
                    myAgent.send(response);
                }
                else
                    block();
            }
        });
    }
}