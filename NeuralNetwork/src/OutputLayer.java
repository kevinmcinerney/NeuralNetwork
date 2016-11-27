/**
 * @author Kevin Mc Inerney
 * @version Version 1.2
 * @since 22/05/2015
 * 
 * */

public class OutputLayer extends ChildLayer{
	
	public OutputLayer(ChildLayer prev, int numberOfUnits) {
		super(prev, prev, numberOfUnits);
		initWeights();
	}
	
	
	public void calculateNet(){
		
		for(int j = 0; j < weights.length; j++){	
			double[] dotProduct = multiplyVectors(inputs, weights[j]);
			units[j].setNet(sumVector(dotProduct));		

		}
	}
	
	@Override
	public void calculateActivation(){
			
		
		for(int j = 0; j < numberOfUnits; j++){ 
			units[j].setActivation(units[j].sigmoid(units[j].getNet()));
		}
	}
	
	@Override
	public void calculateSignalError(int target){
		
		for(int i = 0; i < numberOfUnits; i++){
			double outputSignalError = -1 * (target - units[i].getActivation()) * (1 - units[i].getActivation()) * units[i].getActivation();
			units[i].setError(outputSignalError);
			target = (target == 0) ? 1 : 0;
		}
	}
	
	public boolean classified(int target){
		
		if(quantize(units[0].getActivation(), .3, .7) ==  target){
			target = (target == 0) ? 1 : 0;
			if(quantize(units[1].getActivation(), .3, .7) ==  target){
				return true;
			}
		}
		return false;
	}
	
	
	public void initWeights(){
		
		// Set range of initial weights
		weights = new double[numberOfUnits][inputs.length];
		deltaWeights = new double[numberOfUnits][inputs.length];
		
		double min = -(1/(Math.sqrt(inputs.length)));
		double max = (1/(Math.sqrt(inputs.length)));

		// Input random weights within above range
		for(int i = 0; i < weights.length; i++){  // 3
			for(int j = 0; j < weights[0].length; j++){ 
				weights[i][j] = min + (max - min) * rand.nextDouble();
			}
		}
	}
	
	public double quantize(double net, double min, double max){
		if(net >= max)
			return 1;
		else if (net <= min)
			return 0;
		else
			return net;
	}
}
