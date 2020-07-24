package NeuralCircuitSim;

import Controllers.visualizerSceneController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//todo: Simplify the creation of Neurons. (It would seem there is unecissary memory usage).

public class Main extends Application {
    private static final float WIDTH = 1200;
    private static final float HEIGHT = 700;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../Resources/visualizerScene.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setTitle("Neural Circuit Simulator");
        primaryStage.setScene(scene);
        visualizerSceneController.initMouseControl(scene, primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

