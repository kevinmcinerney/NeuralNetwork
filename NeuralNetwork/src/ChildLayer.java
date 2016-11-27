/**
 * @author Kevin Mc Inerney
 * @version Version 1.2
 * @since 22/05/2015
 * 
 * */

import java.util.Arrays;
import java.util.Random;


public abstract class ChildLayer extends Layer {
	
	protected double[][] weights;
	protected double[][] deltaWeights;
	protected Random rand = new Random();
	protected Layer prev;
	
	public ChildLayer(Layer prev, ChildLayer next, int numberOfUnits){
		super(next, numberOfUnits, prev.getOutputs());
		this.prev = prev;
		
	}
	
	public void feedforward(){
		
		calculateNet();
		calculateActivation();
		setOutput();
	}
	
	public void setOutput(){
		
		for(int i = 0; i < outputs.length; i++){
			outputs[i] = units[i].getActivation();
		}
	}
	
	/**
	 * 
	 * @param First vector in multiplication term
	 * @param Second term in multiplication term
	 * @return Products of terms from two parameters in single array
	 */
	double[] multiplyVectors(double[] vector1, double[] vector2){
		
		double dotProduct[] = new double[vector1.length];
		for(int i = 0; i < vector1.length; i++){
			dotProduct[i] = vector1[i] * vector2[i];
		}
		return dotProduct;
	}
	
	/**
	 * 
	 * @param Vector to be summed
	 * @return A single sum of the param contens
	 */
	
	public double sumVector(double[] vector){
		
		double sum = 0;
		for(Double d: vector){
			sum += d;
		}	
		return sum;
	}
	
	public void showNet(){
		System.out.print("Net ->		[");
		for(Unit u: units){
			System.out.print((double)Math.round(u.getNet() * 1000) / 1000 + ", ");
		}
		System.out.println("]");
		System.out.println();
	}
	
	public void showActivation(){
		System.out.print("Activation ->	[");
		for(Unit u: units){
			System.out.print((double)Math.round(u.getActivation() * 1000) / 1000 + ", ");
		}
		System.out.println("]");
		//System.out.println();
	}
	
	public void showWeights(){
		
		System.out.println("Weights");
		for(int row = 0; row < weights.length; row++){
			System.out.print("[\t");
			for(int col = 0; col < weights[0].length; col++){
				System.out.print(weights[row][col]); 
			}
			System.out.println("]");
		}
		System.out.println();
	}
	
	public void showSignalErrors(){
		
		System.out.println("               Output Signal Error");
		System.out.println("-------------------------------------");
		System.out.print("[\t");
		for(int i = 0; i< units.length; i++){
			System.out.print(units[i].getError()  + "\t"); 
		}
		System.out.println("]");
		System.out.println();
	}
	
	public Layer getPrev() {
		return prev;
	}
	
	public double[][] getWeights() {
		return weights;
	}

	public abstract void calculateNet();
	
	public abstract void calculateActivation();
	
	public abstract void initWeights();
	
	public abstract void calculateSignalError(int target);
	
	
	
}
