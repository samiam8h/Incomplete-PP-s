package NeuralCircuitSim;

import javafx.scene.shape.Cylinder;

import java.util.ArrayList;
import java.util.Random;

public class ReuralNet {
    //todo: make sure startNeurons are directed outwards
    //todo: make sure startNeurons aren't directed toward each other.
    //todo: make sure endNeurons are NOT directed outwards.
    private int[][] incidMatrix;
    private int neuronPopulation, axonPopulation;
    private byte[] input;
    private byte[] activationArr;
    private float[] potentialsArr;
    private ArrayList<int[]> possibleAxonComboList;

    private boolean isGeneration0 = true;
    private float incidMatrixMutationRate = 0.0f;
    private float neurochemSetMutationRate = 0.0f;
    private float receptorSetMutationRate = 0.0f;
    private float weightsMutationRate = 0.0f;
    private float synapsesMutationRate = 0.0f;
    private static Random random = new Random();

    private ArrayList<Neuron> neuronList = new ArrayList<>();
    private ArrayList<Axon> axonList = new ArrayList<>();

    private int[] possibleNeurochemicals = {1, 2, 3, 4, 5}; // 1:glutamate 2:GABA 3:acetylcholine 4:catecholamine 5:serotonin
    private int[] possibleReceptors = {1, -2, 3, -3, 4, -4, 5, -5};

    public ArrayList<Neuron> getNeuronList() {  return neuronList; }

    public ArrayList<Axon> getAxonList() {  return axonList; }

    public int[][] getIncidMatrix() { return incidMatrix; }

    public ArrayList<int[]> getPossibleAxonComboList(){ return possibleAxonComboList; }

    public byte[] getActivationArr(){ return activationArr; }

    public void setActivationArr(byte[] newActivationArr){ this.activationArr = newActivationArr; }

    public float[] getPotentialsArr() { return potentialsArr; }

    public void setPotentialsArr(float[] potentialsArr) { this.potentialsArr = potentialsArr; }

    public ReuralNet(int neuronPopulation, int axonPopulation, byte[] input) {
        this.input = input;

        //input sanitization for neuronPopulation and axonPopulation
        if(neuronPopulation == 0) this.neuronPopulation = 1;
        else if(neuronPopulation < 0) this.neuronPopulation *= -1;
        else this.neuronPopulation = neuronPopulation;
        if(axonPopulation < 0) axonPopulation *= -1;

        this.activationArr = new byte[neuronPopulation];
        this.potentialsArr = new float[neuronPopulation];

        //trim axonPopulation size if out of desired bounds
        if(axonPopulation > ((neuronPopulation*neuronPopulation + neuronPopulation) / 2)) {               //maximum
            this.axonPopulation = ((neuronPopulation-1)*(neuronPopulation-1) + (neuronPopulation-1)) / 2;
        }
        else if(axonPopulation < neuronPopulation/2) this.axonPopulation = neuronPopulation/2;   //minimum
        else { this.axonPopulation = axonPopulation; }

        //filling the neuron list
        Neuron N;
        for (int i = 0; i < this.neuronPopulation; i++) {
            neuronList.add(N = new Neuron(20, .8,.8,.8));
        }

//      Creating the generation0 activationArray based on the input //todo: can the input.length be greater than neuronPopulation/2? if true, then input sanitize.
        if(isGeneration0) {
            for (int i = 0; i < neuronPopulation; i++) {
                if (i >= input.length) break;
                activationArr[i] = input[i];
            }
        }

        //incidMatrix stores what two Neurons get connected together via an Axon, and which way the Axon is directed.
        incidMatrix = new int[this.axonPopulation][2];

        //stores the possible axon configurations(determined via permutation algorithm).
        possibleAxonComboList = permutationsList(this.neuronPopulation);

        //Mutating the incidence Matrix, or filling randomly if isGeneration0.
        if (isGeneration0) { mutateIncidMatrix(1); }
        else mutateIncidMatrix(incidMatrixMutationRate);

        Axon Ax;
        //Go through the incidMatrix and use the inital and terminal neuron objects to create axons and fill the axonList.
        for (int i = 0; i < this.axonPopulation; i++) {
            axonList.add(Ax = new Axon(neuronList.get(incidMatrix[i][0]), neuronList.get(incidMatrix[i][1]), 5, .8, .8 , .8));
        }

        //mutating synapses of each Axon, or filling randomly if isGeneration0
        if(isGeneration0) {
            mutateSynapses(1);
        }
        else mutateSynapses(synapsesMutationRate);

        //mutating weights of each Axon, or filling randomly if isGeneration0 (the updating of weights would be an interesting thing to expand upon).
        if(isGeneration0) { mutateWeights(1); }
        else mutateWeights(weightsMutationRate);
    }

    public void mutateIncidMatrix(float mutationRate) {
         /*An arrayList called "possibleAxonComboList" contains all possible dendritic connections between neurons
         -If a combo is chosen, it is then removed from the array, as well as its inverse(so the inverse should be in the slot above/below for easy access)

         -Removing cycles has yet to be determined.
         */

        //todo: what is the real probability of mutation?
        //todo: all neurons must have at least 1 dendrite attached to it.
        for(int i = 0; i < axonPopulation; i++){
            if(possibleAxonComboList.size() == 0) return; //If there are no possible connections to be made between neurons, don't bother.

            int randomComboIndex = random.nextInt(possibleAxonComboList.size());
            int[] combo = possibleAxonComboList.get(randomComboIndex);
            if (Math.random() < mutationRate)
            {
                // the code below ensures the incidence matrix mutates within the desired restrictions
                incidMatrix[i] = combo;
                if(possibleAxonComboList.size() == 1) possibleAxonComboList.remove(randomComboIndex); //if the list is size 1, remove that 1
                else if(randomComboIndex == possibleAxonComboList.size() - 1 && randomComboIndex % 2 == 0) possibleAxonComboList.remove(randomComboIndex); //if the index is even and the last index
                else if(randomComboIndex % 2 == 0) { //if index is even
                    possibleAxonComboList.remove(randomComboIndex + 1);
                    possibleAxonComboList.remove(randomComboIndex);
                }
                else { //if index is odd
                    possibleAxonComboList.remove(randomComboIndex);
                    possibleAxonComboList.remove(randomComboIndex -1);
                }
                //storing the terminalAxons of a Neuron inside the Neuron object itself for easy access and proper organzination.
                getNeuronList().get(combo[0]).addTerminalAxon(i);
                //todo: make startNeurons have only outward directed Axons, and endNeurons to have only inward directed Axons.
            }
        }
    }

    public void mutateSynapses(float mutationRate){
        for (Axon ax: axonList) {
            if(Math.random() < mutationRate){
                if(Math.random() < .5) ax.setSynapse(1);
                else ax.setSynapse(-1);
            }
        }
    }

    public void mutateWeights(float mutationRate){
        //todo(per geneticAlgorithm): make weight updating more...Interesting.
        for (int i = 0; i < axonList.size(); i++) {
//            if(true) {
                //For now the weights will just be 1;
                float temp = 1.0f;
                axonList.get(i).weight = temp;
//            }
        }
    }

//    private void removeElementfromComboListforSameI0(int[] combo) {
//        for (int i = 0; i < possibleAxonComboList.size(); i++) {
//            if(possibleAxonComboList.get(i)[0] == combo[0]) possibleAxonComboList.remove(i);
//        }
//    }
//
//    private boolean incidContainsNumInColumn0(int num) {
//        for (int i = 0; i < incidMatrix.length; i++) {
//            if(incidMatrix[i][0] == num) return true;
//        }
//        return false;
//    }

    public Cylinder prepareAxons(int i) {
        Axon axon = axonList.get(i);
        return axon.prepareAxon();
    }

    public ArrayList<int[]> permutationsList (int neuronPopulation){
        ArrayList<int[]> res = new ArrayList<>();
        int[] numsA;
        int[] numsB;

        for (int i = 0; i < neuronPopulation; i++) {
            for (int j = i; j < neuronPopulation; j++) {
                if(i != j){
                    numsA = new int[2];
                    numsB = new int[2];
                    numsA[0] = i; numsA[1] = j; res.add(numsA);
                    numsB[0] = j; numsB[1] = i; res.add(numsB);
                }
            }
        }
        for (int i = 0; i < neuronPopulation; i++) {
            if(res.get(i)[1] < input.length || res.get(i)[0] >= neuronPopulation - input.length) res.remove(i);
        }
        return res;
    }
}
