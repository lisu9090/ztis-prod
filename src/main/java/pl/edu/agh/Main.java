package pl.edu.agh;

import jade.core.AID;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.random.MainContainer;

public class Main extends Application {

    private final static Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) {
        logger.info("Kocham Agenty");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            
            AgentController rma = MainContainer.cc.createNewAgent("rma", "jade.tools.rma.rma", null);
            rma.start();

            Object [] args = {new AID("UI-agent", AID.ISLOCALNAME), new AID("Process-agent", AID.ISLOCALNAME), new AID("Database-agent", AID.ISLOCALNAME), 
                new AID("ProcessInput-agent", AID.ISLOCALNAME), new AID("SecondStage-agent", AID.ISLOCALNAME)};

            //initialize UI agent
            
            AgentController ui = MainContainer.cc.createNewAgent("UI-agent", "pl.edu.agh.agents.UIAgent", args);
            ui.start();

            AgentController processAgent = MainContainer.cc.createNewAgent("Process-agent", "pl.edu.agh.agents.ProcessAgent", args);
            processAgent.start();
            
            //initialize database agent
            AgentController database = MainContainer.cc.createNewAgent("Database-agent", "pl.edu.agh.agents.DatabaseAgent", args);
            database.start();
            
            //initialize ML agents
            AgentController ml1 = MainContainer.cc.createNewAgent("ProcessInput-agent", "pl.edu.agh.agents.ProcessInputAgent", args);
            ml1.start();
            
            AgentController ml2 = MainContainer.cc.createNewAgent("SecondStage-agent", "pl.edu.agh.agents.SecondStageAgent", args);
            ml2.start();

        } catch (StaleProxyException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        System.out.println("Agents initialized");

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));

        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(true);
        primaryStage.show();
    }

}