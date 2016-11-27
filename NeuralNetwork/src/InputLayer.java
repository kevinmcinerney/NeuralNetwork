/**
 * @author Kevin Mc Inerney
 * @version Version 1.2
 * @since 22/05/2015
 * 
 * */

public class InputLayer extends Layer{
	
	public InputLayer(ChildLayer next, double[] inputs) {
		super(next, inputs.length, inputs);
		this.outputs = inputs;
		initActivation();
	}
	
	public void initActivation(){
		for(int i = 0; i < inputs.length; i++){
			units[i].setActivation(inputs[i]);
		}
	}
	
	
}
