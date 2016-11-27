
public class Unit {
	
	private int fanin;
	private double net;
	private double activation;
	private double error;

	// fanin is the number of inputs to then unit
	public Unit(){
		
	}
	
	/**
	 * @param net: The summed input of each of 
	 * the correct units within a given layer multiplied
	 * by the correct weights for those units
	 * 
	 * @return: The y-value on the sigmoid function for 
	 * a given input
	 */
	public double sigmoid(double net){
		return 1/(1+Math.pow(Math.E, -net));
	}

	public int getFanin() {
		return fanin;
	}

	public void setFanin(int fanin) {
		this.fanin = fanin;
	}

	public double getNet() {
		return net;
	}

	public void setNet(double net) {
		this.net = net;
	}

	public double getActivation() {
		return activation;
	}

	public void setActivation(double activation) {
		this.activation = activation;
	}

	public double getError() {
		return error;
	}

	public void setError(double error) {
		this.error = error;
	}
	
	
	
	
	
	
}
