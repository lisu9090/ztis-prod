package pl.edu.agh;

import jade.wrapper.AgentController;
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
            AgentController prod = MainContainer.cc.createNewAgent("Production-agent",
                    "pl.edu.agh.agents.ProductionAgent", null);
            prod.start();
            AgentController rma = MainContainer.cc.createNewAgent("rma", "jade.tools.rma.rma", null);
            rma.start();
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }


        Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));

        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(true);
        primaryStage.show();
    }

}