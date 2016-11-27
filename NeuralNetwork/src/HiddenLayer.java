/**
 * @author Kevin Mc Inerney
 * @version Version 1.2
 * @since 22/05/2015
 * 
 * */



public class HiddenLayer extends ChildLayer{
	
	public HiddenLayer(Layer prev, ChildLayer next, int numberOfUnits) {
		super(prev, next, numberOfUnits);
		this.initWeights();
	}
	
	@Override
	public void calculateNet(){
	
		units[numberOfUnits-1].setNet(-1);		
		for(int j = 0; j < weights.length; j++){
			double[] dotProduct = multiplyVectors(prev.inputs, weights[j]);
			units[j].setNet(sumVector(dotProduct));	
		}
	}
	
	@Override
	public void calculateActivation(){
			
		units[numberOfUnits-1].setActivation(-1);
		for(int j = 0; j < numberOfUnits - 1; j++){ 
			units[j].setActivation(units[j].sigmoid(units[j].getNet()));
		}
	}
	
	
	public void initWeights(){
		
		// Set range of initial weights	
		weights = new double[numberOfUnits-1][inputs.length];
		deltaWeights = new double[numberOfUnits-1][inputs.length];
		
		double min = -(1/(Math.sqrt(inputs.length)));
		double max =  (1/(Math.sqrt(inputs.length)));

		// Input random weights within above range
		for(int i = 0; i < weights.length; i++){  // 3
			for(int j = 0; j < weights[0].length; j++){ 
				weights[i][j] = min + (max - min) * rand.nextDouble();
			}
		}
	}
	
	@Override
	public void calculateSignalError(int target){
		
		double error = 0;
		for(int j = 0; j < numberOfUnits; j++){
			error = 0;
			for(int k = 0; k < next.numberOfUnits; k++){
				error +=  next.units[k].getError() * next.weights[k][j] * (1 -  units[j].getActivation()) * units[j].getActivation();
				units[j].setError(error);
			}
		}
	}
}
