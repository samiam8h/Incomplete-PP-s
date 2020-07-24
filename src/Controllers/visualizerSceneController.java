package Controllers;

import NeuralCircuitSim.Axon;
import NeuralCircuitSim.GeneticAlgorithm;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class visualizerSceneController implements Initializable {
    //todo: make it so that the 3D viewer only responds to actions that are within the subScene window. (ie you cant move the neurons around by left clicking on the buttons)

    //In order to create a reural net you must define the neuronPop, axonPop, and the input array(in order to create start & end neurons).
    private byte[] input = {1, 0, 1};
    //todo: the UI should restrict the user inputs for the neuron population, where the neuronPopulation is AT LEAST 2*inputLength
    private GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(60, 16, 32, input);
    private byte[] expectedOutput = new byte[3];
    private int currentRNet = 0;
    private Group neuronGroup;
    private Group axonGroup;
    private static Group superGroup = new Group();

    @FXML
    private SubScene subScene;

    @FXML
    private ListView listView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        neuronGroup = geneticAlgorithm.prepareNeuronGroup(currentRNet);
        axonGroup = geneticAlgorithm.prepareAxonGroup(currentRNet);
        superGroup.getChildren().add(neuronGroup);
        superGroup.getChildren().add(axonGroup);

        subScene.setRoot(superGroup);
        subScene.setFill(Color.BLACK);
        subScene.setCamera(cameraSettings());
//        //Make the currently activated neurons yellow and enlarged.3
//        for (byte i = 0; i < geneticAlgorithm.getPopulationList().get(currentRNet).getActivationArr().length; i++) {
//            if(geneticAlgorithm.getPopulationList().get(currentRNet).getActivationArr()[i] == 1)
//            {
//                geneticAlgorithm.getPopulationList().get(currentRNet).getNeuronList().get(i).setColor(1,1,0);
//                geneticAlgorithm.getPopulationList().get(currentRNet).getNeuronList().get(i).setRadius(30);
//            }
//        }
        listView.getItems().setAll(RnetList());
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        for (int i = 0; i < geneticAlgorithm.getPopulationList().size(); i++) {
            showActivated(i);
        }
        showActivated(currentRNet);
        //______________________________________________________________________________________________________________________________________
//        for ()
        //______________________________________________________________________________________________________________________________________
    }

    private void showActivated(int currentRNet) {
        for(int i = 0; i < geneticAlgorithm.getPopulationList().get(currentRNet).getActivationArr().length; i++){
            if(geneticAlgorithm.getPopulationList().get(currentRNet).getActivationArr()[i] == 1) {
                geneticAlgorithm.getPopulationList().get(currentRNet).getNeuronList().get(i).setColor(1,1,0);
                geneticAlgorithm.getPopulationList().get(currentRNet).getNeuronList().get(i).setRadius(30);
                for (int j: geneticAlgorithm.getPopulationList().get(currentRNet).getNeuronList().get(i).getTerminalAxons()) {
                    PhongMaterial axonMaterial = new PhongMaterial();
                    axonMaterial.setDiffuseColor(Color.color(1,1,0));
                    axonGroup.getChildren().get(j);
                }
            }
        }
    }

    private void setNeuronDefaultProperties(){
        for(int i = 0; i < geneticAlgorithm.getPopulationList().get(currentRNet).getNeuronList().size(); i++){
            geneticAlgorithm.defaultNeuronProperties(i, currentRNet);
        }
    }

    private List<String> RnetList(){
        List res = new ArrayList<String>();
        for (int i = 0; i < geneticAlgorithm.getPopulationList().size(); i++) {
            res.add(i+"");
        }
        return res;
    }

    private Camera cameraSettings(){
        Camera camera = new PerspectiveCamera(true);
        camera.setNearClip(1);
        camera.setFarClip(30000);
        camera.translateZProperty().set(-3000);
        return camera;
    }

    private static double anchorX;
    private static double anchorY;
    private static double anchorAngleX = 0;
    private static double anchorAngleY = 0;
    private static final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private static final DoubleProperty angleY = new SimpleDoubleProperty(0);

    public static void initMouseControl(Scene scene, Stage stage){
        Rotate xRotate;
        Rotate yRotate;
        superGroup.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });

        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            superGroup.translateZProperty().set(superGroup.getTranslateZ() + delta);
        });
    }

    public void stepButtonClicked(MouseEvent mouseEvent) {
        setNeuronDefaultProperties();
        byte tempArr[] = geneticAlgorithm.getPopulationList().get(currentRNet).getActivationArr();
        //__________________________________________________________________________________________________________________________
//        for (int i = 0; i < geneticAlgorithm.getPopulationList().get(currentRNet).getIncidMatrix().length; i++) {
//            System.out.print("[" + geneticAlgorithm.getPopulationList().get(currentRNet).getIncidMatrix()[i][0] + ", " + geneticAlgorithm.getPopulationList().get(currentRNet).getIncidMatrix()[i][1] + "], ");
//        }
//        System.out.println();
//        for(byte i : tempArr){
//            System.out.print(i + " ");
//        }
//        System.out.println();
        //__________________________________________________________________________________________________________________________
        geneticAlgorithm.step(currentRNet);
        //Make the currently activated neurons yellow, and glow yellow.
        showActivated(currentRNet);

    }

    public void listViewClicked(MouseEvent mouseEvent){
        if (mouseEvent.getClickCount() == 2 && !mouseEvent.isConsumed()) { //handle double click event.
            mouseEvent.consume();
            currentRNet = listView.getSelectionModel().getSelectedIndex();
            neuronGroup = geneticAlgorithm.prepareNeuronGroup(currentRNet);
            axonGroup = geneticAlgorithm.prepareAxonGroup(currentRNet);
            superGroup.getChildren().clear();
            superGroup.getChildren().add(neuronGroup);
            superGroup.getChildren().add(axonGroup);
        }
    }



}
