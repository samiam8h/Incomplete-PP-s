package NeuralCircuitSim;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

import java.util.ArrayList;
import java.util.Random;

public class Neuron extends Sphere {
    private boolean isOn;
    ArrayList<Integer> terminalAxons = new ArrayList<>();
    private float currentPotential = 0;
    private static Random random = new Random();
    private int positionX = random.nextInt(500) - random.nextInt(500);
    private int positionY = random.nextInt(500) - random.nextInt(500);
    private int positionZ = random.nextInt(500) - random.nextInt(500);
    private double color0, color1, color2;

    public Neuron(double radius, double color0, double color1, double color2) {
        this.color0 = color0;
        this.color1 = color1;
        this.color2 = color2;
        super.translateXProperty().set(positionX);
        super.translateYProperty().set(positionY);
        super.translateZProperty().set(positionZ);
        super.setRadius(radius);
        PhongMaterial neuronMaterial = new PhongMaterial();
        neuronMaterial.setDiffuseColor(Color.color(this.color0,this.color1,this.color2));
        super.setMaterial(neuronMaterial);
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public int getPositionZ() {
        return positionZ;
    }

    public void setColor(double color0, double color1, double color2){
        this.color0 = color0;
        this.color1 = color1;
        this.color2 = color2;
        PhongMaterial neuronMaterial = new PhongMaterial();
        neuronMaterial.setDiffuseColor(Color.color(this.color0,this.color1,this.color2));
        super.setMaterial(neuronMaterial);
    }

    public void activate(){
        isOn = true;
    }

    public void deactivate(){
        isOn = false;
    }
    public ArrayList<Integer> getTerminalAxons() {
        return terminalAxons;
    }

    public void setTerminalAxons(ArrayList<Integer> terminalAxons) {
        this.terminalAxons = terminalAxons;
    }

    public void addTerminalAxon(int axonIndexinAxonList){
        this.terminalAxons.add(axonIndexinAxonList);
    }
}