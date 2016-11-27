/**
 * @author Kevin Mc Inerney
 * @version Version 1.2
 * @since 22/05/2015
 * 
 * */

public class Layer {

	protected ChildLayer next;
	protected int numberOfUnits;
	protected Unit[] units;
	protected double[] outputs;
	protected double[] inputs;
	
	public Layer(ChildLayer next,int numberOfUnits, double[] inputs){
		this.next = next;
		this.numberOfUnits = numberOfUnits;
		this.units = new Unit[numberOfUnits];
		this.inputs = inputs;
		this.outputs = new double[numberOfUnits];
		this.initUnits();
	}
	
	public void initUnits(){	
		for(int i = 0; i < numberOfUnits; i++){
			this.units[i] = new Unit();
		}
	}
	
	
	public double[] getOutputs() {
		return outputs;
	}
	
	public void setInputs(double[] inputs) {
		this.inputs = inputs;
	}

	public void showInputs(){
		
		System.out.print("Inputs ->	[");
		for(int i = 0; i < inputs.length; i++){
			System.out.print((double)Math.round(inputs[i] * 1000) / 1000 + ", ");
		}
		System.out.println("]");
		System.out.println();
	}

	public void setNext(ChildLayer next) {
		this.next = next;
	}
	
	
}
