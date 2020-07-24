package NeuralCircuitSim;

import javafx.scene.Group;

import java.util.ArrayList;

public class GeneticAlgorithm {
    private ArrayList<ReuralNet> populationList;
    private int neuronPopulation, axonPopulation;
    private int reuralNetPopulation;
    private byte[] input = new byte[0]; //the default input is {0}
    private int inputLength;
    private boolean[] expectedOutput;
    private int[] currentNeuronStates;

    public GeneticAlgorithm(int RNetPopulation, int neuronPopulation, int axonPopulation, byte[] input){
        this.neuronPopulation = neuronPopulation;
        this.axonPopulation = axonPopulation;
        this.reuralNetPopulation = RNetPopulation;
        this.populationList = new ArrayList<>(reuralNetPopulation);
        this.input = input;
        this.inputLength = input.length;
        this.currentNeuronStates = new int[neuronPopulation];

        //filling populationList
        ReuralNet Rnet;
        for(int i = 0; i < RNetPopulation; i++){
            populationList.add(Rnet = new ReuralNet(this.neuronPopulation, this.axonPopulation, input));
        }
    }

    public void step(int neuronPopulationListIndex){
        ReuralNet tempRNet = populationList.get(neuronPopulationListIndex);
        byte[] tempActivArr = tempRNet.getActivationArr();
        float[] tempPotentialsArr = tempRNet.getPotentialsArr();

        //  For each neuron in the activationArray, if the neuron is active: for each terminalAxon of said neuron, computeSynapse and add the result to the proper position in
        //tempPotentialArr (tempPotentialArr index = terminal neuron in incidence matrix(column 1) where the inital neuron is the said neuron).
        for (int i = 0; i < tempActivArr.length; i++) {
            if(tempActivArr[i] == 1)
            {
                for (int j: tempRNet.getNeuronList().get(i).getTerminalAxons()) {
                    Axon tempAx = tempRNet.getAxonList().get(j);
                    tempPotentialsArr[tempRNet.getIncidMatrix()[j][1]] += tempAx.getSynapse() * tempAx.getWeight();
                }
            }
        }
        byte[] newActiveArr = thresholdEvaluation(tempPotentialsArr, neuronPopulation);
        tempRNet.setActivationArr(newActiveArr);
    }

    private byte[] thresholdEvaluation(float[] tempPotentialsArr, int population) {
        byte[] res = new byte[population];
        for(int i = 0; i < tempPotentialsArr.length; i++){
            if(tempPotentialsArr[i] >= 1) res[i] = 1;
        }
        return res;
    }

    public ArrayList<ReuralNet> getPopulationList() { return populationList; }

    public Group prepareAxonGroup(int rnetPopulationListPosition){
        Group axonGroup = new Group();
        for(int i = 0; i < populationList.get(rnetPopulationListPosition).getAxonList().size(); i++){
            axonGroup.getChildren().add(populationList.get(rnetPopulationListPosition).getAxonList().get(i));
        }
        return axonGroup;
    }

    public Group prepareNeuronGroup(int rnetPopulationListPosition)
    {
        Group neuronGroup = new Group();
        for(int i = 0; i < populationList.get(rnetPopulationListPosition).getNeuronList().size(); i++)
        {
            defaultNeuronProperties(i, rnetPopulationListPosition);
            neuronGroup.getChildren().add(populationList.get(rnetPopulationListPosition).getNeuronList().get(i));
        }
        return neuronGroup;
    }

    public void defaultNeuronProperties(int neuronPosition, int rnetPopulationListPosition){
        //---designating the color and size of the neuron:---
        // since the # of start neurons=input.length(or int inputLength), and the start neurons are the lower portion on the neuronList, the first input.length neurons
        //will be a start neuron. These will be green.
        if(neuronPosition < inputLength) { populationList.get(rnetPopulationListPosition).getNeuronList().get(neuronPosition).setColor(0, .8, 0); }
        //same logic applies for the end neurons. The last input.length of the neuronList is end neurons. These will be red.
        else if(neuronPosition >= populationList.get(rnetPopulationListPosition).getNeuronList().size() - inputLength) {
            populationList.get(rnetPopulationListPosition).getNeuronList().get(neuronPosition).setColor(1, 0, 0);
        }
        //The default radius of a neuron is 20.
        populationList.get(rnetPopulationListPosition).getNeuronList().get(neuronPosition).setRadius(20);
    }
}
