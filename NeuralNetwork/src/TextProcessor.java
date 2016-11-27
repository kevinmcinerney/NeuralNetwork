
/**
 * @author Kevin Mc Inerney
 * @version Version 1.2
 * @since 22/05/2015
 * 
 * */
 

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;



public class TextProcessor {
	 
    private File dataFilename;
    private BufferedReader br;
    private ArrayList<ArrayList<String>> dataSets;
    private ArrayList<String> trainingSet;
	private ArrayList<String> validationSet;
	private ArrayList<String> testingSet;
	private ArrayList<Integer> targetOutput;
	private Random rand = new Random();
	private int dataSize;
    
	public TextProcessor(String dataFileName){
		
		this.dataFilename = new File(TextProcessor.class.getResource("/Data/"+dataFileName).getFile());
		
		// Main data set
		this.dataSets = new ArrayList<ArrayList<String>>();
		
		// Three sub sets of the main data set
		this.trainingSet = new ArrayList<String>();
		this.testingSet  = new ArrayList<String>();
		this.validationSet = new ArrayList<String>();
		
		// Generate the target output vector
		this.targetOutput();
		
		// Size of data set
		this.dataSize = targetOutput.size();
		
	}
	
	
	/**
	 * 
	 * @param numberOfExamples The number of total examples (training, validation & testing) in your data
	 * @param trainingSplit    The percentage of data allocatated to the training set (eg, 0.1). Defaults to 0.6.
	 * @param validationSplit  The percentage of data allocatated to the validation set (eg, 0.1). Defaults to 0.2.
	 * @param testingSplit     The percentage of data allocatated to the testing set (eg, 0.1). Defaults o 0.2
	 * @return An array of validation sets: 1) Training, 2) Validation, 3) Testing
	 */
	public ArrayList<ArrayList<String>> splitData( 
		double trainingSplit, 
		double validationSplit, 
		double testingSplit){
		
		// Shuffled Indexes
		int[] indexes = randomIndexArray(dataSize);
		
		// Sentences for training/testing
		String[] dataSentences = new String[dataSize];
		
		//Reset Buffer
		resetBuffer();
		
		// DataSet size
		int trainingSize = (int)(dataSize*trainingSplit);
		int validationSize = (int)(dataSize*validationSplit) + trainingSize;
		
		String trainingExample;
		
		try {
			int count = 0;
			
			while ((trainingExample = br.readLine()) != null) {
				
				dataSentences[indexes[count++]] = trainingExample;
				
			}
			
			count = 0;
			
			for(String s: dataSentences){
				
				if(count++ < trainingSize){
					trainingSet.add(s);
				}
				else if(count++ < validationSize){
					validationSet.add(s);
				}
				else{
					testingSet.add(s);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		dataSets.add(trainingSet);
		dataSets.add(validationSet);
		dataSets.add(testingSet);
		
		return dataSets;
	}
	
	/*
	 * Overloaded for default call
	 * 0.6% Training Data
	 * 0.0 Validation Data
	 * 0.4% Test Data
	 */
	public ArrayList<ArrayList<String>> splitData(){
			
		return splitData(1, 0.0, 0.0);
		}
	
	/**
	 * 
	 * @param dataSet The dataset to process
	 * @return A feature matrix of deminsions (dataSetSize x 7)
	 */
	public double[][] preProcessText(ArrayList<String> dataSet, int numAlphabetFeatures, boolean boosterFeatures){
		
		int offset = 0;
		
		if(boosterFeatures){
			offset = 6;
		}
		
		
		// Contains all features - last 26 are alphabet letters
		double[][] featureMatrix = new double[dataSize][numAlphabetFeatures+offset+1];
		
		
		// Individual features
		double feature1[] = new double[dataSize];
		double feature2[] = new double[dataSize];
		double feature3[] = new double[dataSize];
		double feature4[] = new double[dataSize];
		double feature5[] = new double[dataSize];
		double feature6[] = new double[dataSize];
		
		// Small bag of commonly used words in Africaans
		ArrayList<String>popularAfricaansWords = new ArrayList<String>();
		popularAfricaansWords.add("die");
		popularAfricaansWords.add("wees");
		popularAfricaansWords.add("om");
		popularAfricaansWords.add("van");
		popularAfricaansWords.add("dat");
		popularAfricaansWords.add("het");
		popularAfricaansWords.add("dit");
		popularAfricaansWords.add("vir");
		popularAfricaansWords.add("nie");
		popularAfricaansWords.add("op");
		popularAfricaansWords.add("met");
		popularAfricaansWords.add("ek");
		popularAfricaansWords.add("sy");
		popularAfricaansWords.add("wat");
		popularAfricaansWords.add("hy");
		popularAfricaansWords.add("hulle");
		popularAfricaansWords.add("jy");
		popularAfricaansWords.add("een");
		popularAfricaansWords.add("deur");
		popularAfricaansWords.add("maar");
		popularAfricaansWords.add("te");
		popularAfricaansWords.add("'n");
		popularAfricaansWords.add("uit");
		popularAfricaansWords.add("moes");
		

		// Small bag of commonly used English words
		ArrayList<String>popularEnglishWords = new ArrayList<String>();
		popularEnglishWords.add("the");
		popularEnglishWords.add("be");
		popularEnglishWords.add("to");
		popularEnglishWords.add("of");
		popularEnglishWords.add("and");
		popularEnglishWords.add("that");
		popularEnglishWords.add("have");
		popularEnglishWords.add("it");
		popularEnglishWords.add("for");
		popularEnglishWords.add("not");
		popularEnglishWords.add("on");
		popularEnglishWords.add("with");
		popularEnglishWords.add("his");
		popularEnglishWords.add("are");
		popularEnglishWords.add("from");
		popularEnglishWords.add("by");
		popularEnglishWords.add("but");
		popularEnglishWords.add("some");
		popularEnglishWords.add("what");
		popularEnglishWords.add("i");
		popularEnglishWords.add("a");
		popularEnglishWords.add("we");
		
		
	
		/**
		 * Three letters with a higher frequency in Africaans
		 * @see http://www.sttmedia.com/characterfrequency-afrikaans
		 */
		ArrayList<Character>popularAfricaansLetters = new ArrayList<Character>();
		popularAfricaansLetters.add('d');
		popularAfricaansLetters.add('g');
		popularAfricaansLetters.add('k');
		
		
		/**
		 * Six letters used with a higher frequency in English
		 * @see http://www.sttmedia.com/characterfrequency-english
		 */
		ArrayList<Character>popularEnglishLetters = new ArrayList<Character>();
		popularEnglishLetters.add('c');
		popularEnglishLetters.add('t');
		popularEnglishLetters.add('h');
		
		// Double vowels appear more in Africaans
		// This is subjective observation of the author
		ArrayList<String> doubleVowels = new ArrayList<String>();
		doubleVowels.add("aa");
		doubleVowels.add("oo");
		
		// For each data example
		for(int dataRow = 0; dataRow < dataSet.size(); dataRow++) {
			
			// Extract the sentence portion of the data example
			String dataExample = dataSet.get(dataRow).toLowerCase().substring(2);
			
			// Count of popular Africaans letters
			double africaansLetterFeature = 0;
			
			// Count of popular English letters
			double englishLetterFeature = 0;

			// Count of popular Africaans words
			double africaansWordFeature = 0;
			
			// Count of popular English words
			double englishWordFeature = 0;
			
			// Count of special Africaans characters
			double africaansSpecialCharactersCount = 0;
			
			// Count vowel pairs
			double vowelPairsFeature = 0;
			
			// Word level analysis
			for(String word: dataExample.split(" ")){
				if(popularEnglishWords.contains(word)){
					englishWordFeature++;
				}
				else if(popularAfricaansWords.contains(word)){
					africaansWordFeature++;
				}
				for(String vowelPairs: doubleVowels){
					if(word.contains(vowelPairs)){
						vowelPairsFeature++;
					}
				}
				
				// For each letter in above word
				for(int l = 0; l < word.length(); l++){
					if(popularAfricaansLetters.contains(word.charAt(l))){
						africaansLetterFeature++;
					}else if(popularEnglishLetters.contains(word.charAt(l))){
						englishLetterFeature++;
					}	
					if((int)word.charAt(l) >= 232 && (int)word.charAt(l) <= 235){
						africaansSpecialCharactersCount++;
					}
					if((int)word.charAt(l) >= 238 && (int)word.charAt(l) <= 239){
						africaansSpecialCharactersCount++;
					}
					if((int)word.charAt(l) == 244 || (int)word.charAt(l) == 251 || (int)word.charAt(l) == 246){
						africaansSpecialCharactersCount++;
					}
					if(l > 1 && word.charAt(l) == 'n' && word.charAt(l-1) == '\''){
						africaansSpecialCharactersCount++;
					}
					int num = (int) word.charAt(l) % 97;
					if(num >= 0 && num < numAlphabetFeatures){
						featureMatrix[dataRow][((int)word.charAt(l) % 97)]++;	
					}	
				}
			}
			
			feature1[dataRow] = africaansLetterFeature; 
			feature2[dataRow] = englishLetterFeature;
			feature3[dataRow] = africaansWordFeature;
			feature4[dataRow] = englishWordFeature;
			feature5[dataRow] = vowelPairsFeature;
			feature6[dataRow] = africaansSpecialCharactersCount;
			
		}
		
		// Get Max of each feature for scaling
		double feature1Max = getMax(feature1);
		double feature2Max = getMax(feature2);
		double feature3Max = getMax(feature3);
		double feature4Max = getMax(feature4);
		double feature5Max = getMax(feature5);
		double feature6Max = getMax(feature6);
		
		
		// Get Min of each feature for scaling
		double feature1Min = getMin(feature1);
		double feature2Min = getMin(feature2);
		double feature3Min = getMin(feature3);
		double feature4Min = getMin(feature4);
		double feature5Min = getMin(feature5);
		double feature6Min = getMin(feature6);
		
		// Scale each sentence vertically
		for(int i = 0; i < featureMatrix.length; i++){
			double alphabetMax = getMax(featureMatrix[i]);
			double alphabetMin = getMin(featureMatrix[i]);
			for(int j = 0; j < featureMatrix[j].length; j++){
				featureMatrix[i][j] = scale(featureMatrix[i][j], alphabetMin, alphabetMax, -Math.sqrt(3), Math.sqrt(3));
			}
		}
			
		// Iterate through data again, this time scaling the features
		for(int i = 0; i < dataSet.size(); i++){
			
			// Scaling
			feature1[i] = scale(feature1[i], feature1Min, feature1Max, -Math.sqrt(3), Math.sqrt(3));
			feature2[i] = scale(feature2[i], feature2Min, feature2Max, -Math.sqrt(3), Math.sqrt(3));
			feature3[i] = scale(feature3[i], feature3Min, feature3Max, -Math.sqrt(3), Math.sqrt(3));
			feature4[i] = scale(feature4[i], feature4Min, feature4Max, -Math.sqrt(3), Math.sqrt(3));
			feature5[i] = scale(feature5[i], feature5Min, feature5Max, -Math.sqrt(3), Math.sqrt(3));
			feature6[i] = scale(feature6[i], feature6Min, feature6Max, -Math.sqrt(3), Math.sqrt(3));
			
			
			// Adding features to feature matrix
			if(boosterFeatures){
				featureMatrix[i][numAlphabetFeatures] = feature1[i];
				featureMatrix[i][numAlphabetFeatures+1] = feature2[i];
				featureMatrix[i][numAlphabetFeatures+2] = feature3[i];
				featureMatrix[i][numAlphabetFeatures+3] = feature4[i];
				featureMatrix[i][numAlphabetFeatures+4] = feature5[i];
				featureMatrix[i][numAlphabetFeatures+5] = feature6[i];
			}
			featureMatrix[i][numAlphabetFeatures+offset] = -1; // bias unit
		}
		
		return featureMatrix;
	}
		
	/**
	 * 
	 * @param input Input Value
	 * @param baseMin Minimum Input Value
	 * @param baseMax Maximum Input Value
	 * @param limitMin Minimum Scaled Output Value
	 * @param limitMax Maximum Scaled Output Value
	 * @return Scaled output
	 */
	public double scale(
			final double input, 
			final double baseMin, 
			final double baseMax, 
			final double limitMin, 
			final  double limitMax){
		return ((limitMax - limitMin) * (input - baseMin) / (baseMax - baseMin)) + limitMin;
	}


	/**
	 * Sets the target vector field
	 */
	public void targetOutput(){
		
		int targetOutput;
		ArrayList<Integer> targetOutputVector = new ArrayList<Integer>();
		String trainingExample;
		
		//Reset Buffer
		resetBuffer();
		
		
		try {
			while ((trainingExample = br.readLine()) != null) {
				targetOutput = Integer.parseInt(trainingExample.substring(0,1));
				targetOutputVector.add(targetOutput);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.targetOutput = targetOutputVector;
	}
	
	/**
	 * 
	 * @param Double Array
	 * @return Maximum element of Array
	 */
	public double getMax(double[] array){
		
		double max = 0;
		for(int i = 0; i < array.length; i++){
			if(array[i] > max){
				max = array[i];
			}
		}
		return max;
	}
	
	/**
	 * @param Double Array
	 * @return Minimum element of Array
	 */
	public double getMin(double[] array){
		
		double min = Double.MAX_VALUE;
		for(int i = 0; i < array.length; i++){
			if(array[i] < min){
				min = array[i];
			}
		}
		return min;
	}
	
	
	public void resetBuffer(){
		this.br = new BufferedReader(new InputStreamReader(this.getClass().
				getResourceAsStream("/Data/" + dataFilename.getName()), Charset.forName("UTF-8")));
	
	}

	public ArrayList<Integer> getTargetOutput() {
		return targetOutput;
	}
	
	public int[] randomIndexArray(int size){
		
		int[] randomIdxVector = new int[dataSize];
		
		// Indexes 0....n
		for(int i = 0; i < randomIdxVector.length;i++){
			randomIdxVector[i] = i;
		}
	
		for(int i = 0; i < size-1; i++){
			if(i % 2 == 0){
				int num1 = randEvenInt(0,dataSize);
				swap(randomIdxVector, i, num1);
			}
			else{
				int num2 = randOddInt(0,dataSize);
				swap(randomIdxVector, i, num2);
				}
		}
		return randomIdxVector;
	}
	
	public void swap(int[] array, int idx1, int idx2){
		
		int temp = array[idx1];
		array[idx1] = array[idx2];
		array[idx2] = temp;
	}
	
	public int randEvenInt(int min, int max) {

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min)) + min;
	    
	    if(randomNum % 2 != 0){
	    	randomNum = (randomNum + 1) % max;
	    }
	    

	    return randomNum;
	}
	
	public int randOddInt(int min, int max) {

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min)) + min;
	    
	    if(randomNum % 2 == 0){
	    	randomNum = (randomNum + 1) % max;
	    }

	    return randomNum;
	}
}
