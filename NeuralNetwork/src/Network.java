/**
 * @author Kevin Mc Inerney
 * @version Version 1.2
 * @since 22/05/2015
 * 
 * */

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;


public class Network {
	
	
	// Array of Layers
	private ChildLayer[] layers;
	
	// Layers
	private Layer inputLayer;
	private ChildLayer hiddenLayer;
	private ChildLayer outputLayer;
	
	// Input Vectors
	private double[][] trainingInputs;
	private double[][] validationInputs;
	private double[][] testingInputs;
	
	// Text Processor
	TextProcessor p;
	
	// Data Sets
	ArrayList<String> trainingSet;
	ArrayList<String> validationSet;
	ArrayList<String> testingSet;
	
	// Target Vector
	ArrayList<Integer> target;
	
	// Parameters
	double learningRate, momentum;
	
	// 0..26 for letters of alphabet
	int numFeatures;
	
	// parameters
	int epoch,maxEpoch;
	
	// features
	boolean boosterFeatures;
	
	// File
	File trainFile;
	File testFile;
	
	// Writer
	PrintWriter testOut;
	PrintWriter trainOut;
	
	public Network(int sizeOfHiddenLayer, double learningRate, double momentum,int maxEpoch,int numFeatures, boolean boosterFeatures){
		this.numFeatures = numFeatures;
		this.learningRate = learningRate;
		this.momentum = momentum;
		this.boosterFeatures = boosterFeatures;
		this.p = new TextProcessor("full_dataset.txt");
		this.initDataSets();
		this.initInputs(this.trainingSet);
		this.inputLayer = new InputLayer(hiddenLayer,trainingInputs[0]);
		this.hiddenLayer = new HiddenLayer(inputLayer, outputLayer, sizeOfHiddenLayer);
		this.outputLayer = new OutputLayer(hiddenLayer,2);
		this.hiddenLayer.setNext(outputLayer);
		this.layers = new ChildLayer[2];
		this.layers[0] = hiddenLayer;
		this.layers[1] = outputLayer;
		this.initTarget();
		this.epoch = 0;
		this.maxEpoch = maxEpoch;
	}
	
	
	public void train(){
		
		// For writing to a file
		/*try {
			trainFile = new File("src/Data/333lmTrain.txt");
			testFile = new File("src/Data/333lmTest.txt");
			trainOut = new PrintWriter(new BufferedWriter(new FileWriter(trainFile, true)),true);
			testOut = new PrintWriter(new BufferedWriter(new FileWriter(testFile, true)),true);
			//out.write("Epochs \t NumFeatures BoosterFeatures \t Train Error \t Test Error \n");
		} catch (IOException e) {
			e.printStackTrace();
		};*/

		int epoch = 0;
		
		double trainingAccuracy = 0;
		test:
		while(epoch++ < maxEpoch){
			
			trainingAccuracy = 0;
			
			for(int i = 0; i < trainingSet.size(); i++){
				
				initLayers();
				inputLayer.setInputs(trainingInputs[i]);
				for(int g = 0; g < inputLayer.units.length; g++){
					inputLayer.units[g].setActivation(inputLayer.inputs[g]);
				}
				
				this.hiddenLayer.prev.setInputs(this.inputLayer.inputs);
				
				feedforward();	
				
				if(((OutputLayer) outputLayer).classified(target.get(i))){
					trainingAccuracy++;
					continue;
				}
				
				backpropogate(i);
				
			}
			
			//trainOut.write((1 - (trainingAccuracy / trainingSet.size()))*100 + "\t");
			System.out.println("Epoch: "+ epoch + "\tTraining Error " + ((1 - (trainingAccuracy / trainingSet.size()))*100 + "%\t"));
		
			
			test();
			
			if((trainingAccuracy/trainingSet.size()) > .99){
				epoch = maxEpoch;
				break test;
			}
		}
		/*trainOut.write("\n");
		testOut.write("\n");
		trainOut.close();	
		testOut.close();*/
		
	}
	
	public void test(){
			
		double testAccuracy = 0;
		
		int i = 0;
		
		for(; i < testingSet.size(); i++){
			
			initLayers();
			
			inputLayer.setInputs(testingInputs[i]);
			
			for(int g = 0; g < inputLayer.units.length; g++){
				inputLayer.units[g].setActivation(inputLayer.inputs[g]);
			}
			
			this.hiddenLayer.prev.setInputs(this.inputLayer.inputs);
		
			feedforward();
			
			if(((OutputLayer) outputLayer).classified(target.get(i+trainingSet.size()))){
				testAccuracy++;
			}
		}
		//testOut.write((1 - (testAccuracy / testingSet.size()))*100 + "\t");
		System.out.println("\t\tTest Error:\t"+ ((1 - (testAccuracy / testingSet.size()))*100 + "%\t"));
	}
	
	public void feedforward(){
		
		for(int i = 0 ; i < layers.length; i++){
			layers[i].feedforward();
		}
	}
	
	
	public void backpropogate(int row){
		
		for(int i = layers.length-1; i >= 0; i--){
			layers[i].calculateSignalError(target.get(row));
		}
		adjustWeights();
	}
	
	public void adjustWeights(){
		for(int i = layers.length-1; i >= 0; i--){
			for(int k = 0; k < layers[i].weights.length; k++){
				for(int j = 0; j < layers[i].weights[0].length; j++){
					double error = layers[i].units[k].getError();
					double unitOutput = layers[i].prev.units[j].getActivation();
					layers[i].deltaWeights[k][j] = (-1*learningRate)*error*unitOutput 
							+ momentum*layers[i].deltaWeights[k][j];
					
					layers[i].weights[k][j] += layers[i].deltaWeights[k][j];
				}
			}
		}
	}
	
	private void initDataSets() {
		
		ArrayList<ArrayList<String>> dataSets = p.splitData(0.8,0,0.2);
		trainingSet = dataSets.get(0);
		validationSet = dataSets.get(1);
		testingSet = dataSets.get(2);
	}
	
	public void initTarget(){
		
		this.target = p.getTargetOutput();
	}
	
	public void initInputs(ArrayList<String> dataSet){
		
		this.trainingInputs = p.preProcessText(trainingSet, numFeatures, boosterFeatures);
		this.validationInputs= p.preProcessText(validationSet, numFeatures, boosterFeatures);
		this.testingInputs = p.preProcessText(testingSet, numFeatures, boosterFeatures);
		

	}
	
	public void initLayers(){
		for(Layer l: layers){
			l.initUnits();
		}
	}
	
	
	public static void main(String args[]){
		
		int hiddenLayerSize = Integer.parseInt(args[0]);
        double learningRate = Double.parseDouble(args[1]);
        double momentum = Double.parseDouble(args[2]);
        int maxEpochs = Integer.parseInt(args[3]);
        int numFeatures = Integer.parseInt(args[4]);
        Boolean boost = Boolean.valueOf(args[5]);
        
		Network nn = new Network(hiddenLayerSize, learningRate, momentum, maxEpochs, numFeatures, boost);
		System.out.println();
		System.out.println();
		System.out.println("=======================Neural Network Results===============");
		System.out.println();
		nn.train();
		System.out.println();
		System.out.println("============================================================");
		
	}
}
