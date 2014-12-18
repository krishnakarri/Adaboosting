package com.ml.assignment2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class RealAdaBoosting {
	InputValues inputValues;
	WeakClassifier weakClassifier;
	HashMap<Integer,IterationOutput> outputValues;
	IterationOutput itrOutput;
	String outputFileName;
	
	public RealAdaBoosting(InputValues inputValues,String outputFileName){
		this.inputValues = inputValues;
		weakClassifier = new WeakClassifier(inputValues);
		outputValues = new HashMap<Integer,IterationOutput>();
		this.outputFileName = outputFileName;
	}
	
	public void binaryAdaBoostingExecution() {
		double alphaValue;
		
		double[][] valueOfg = new double[inputValues.getNumberOfIterations()][inputValues.getNumberOfExamples()];
		for (int k = 0; k < inputValues.getNumberOfIterations(); k++) {
			for (int j = 0; j < inputValues.getNumberOfExamples(); j++) {
				valueOfg[k][j] = -1;
			}
		}
		double valueOfZ[] = new double[inputValues.getNumberOfIterations()];
		for (int j = 0; j < inputValues.getNumberOfIterations(); j++) {
			valueOfZ[j] = -1;
		}
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		try {
			fileWriter = new FileWriter(new File(outputFileName));
			bufferedWriter = new BufferedWriter(fileWriter);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			bufferedWriter.write("Karri, Jaya Venkata Krishna");
			bufferedWriter.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < inputValues.getNumberOfIterations(); i++) {
			try {
				bufferedWriter.newLine();
				bufferedWriter.write("Iteration "+(i+1));
				bufferedWriter.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			weakClassifier.getWeakClassifierForRealAdaBoosting();
			itrOutput = new IterationOutput();
			itrOutput.setWeakClassifierValue(weakClassifier.getWeakHypothesis());
			itrOutput.setLeftPositive(weakClassifier.isLeftPositive());
			itrOutput.setErrorEpsilon(weakClassifier.getValuOfG());
			double[] xValues = inputValues.getxValues();
			double[] classifierOutput = weakClassifier.getHypothesisOutputValues();

			itrOutput.setClassifierOutput(classifierOutput);
			
			double cPlus,cMinus;
			cPlus = calculateValueOfC(weakClassifier.getPrPlus(),weakClassifier.getPwMinus());
			cMinus = calculateValueOfC(weakClassifier.getPwPlus(),weakClassifier.getPrMinus());
			itrOutput.setcPlus(cPlus);
			itrOutput.setcMinus(cMinus);
			
			
			valueOfZ[i] = calculateValueOfZ(weakClassifier.getPrPlus(),weakClassifier.getPwMinus(),weakClassifier.getPwPlus(),weakClassifier.getPrMinus(),cPlus,cMinus);
			itrOutput.setValueOfZ(valueOfZ[i]);
			
			
			for (int j = 0; j < inputValues.getNumberOfExamples(); j++) {
				if(weakClassifier.getHypothesisOutputValues()[j] == 1)
					valueOfg[i][j] = cPlus; 
				else
					valueOfg[i][j] = cMinus;
			}	
			double[] prenormalisedProbability = new double[inputValues.getNumberOfExamples()];
			for (int j = 0; j < prenormalisedProbability.length; j++) {
				prenormalisedProbability[j] = inputValues.getpValues()[j] * ((double)Math.exp(-1*inputValues.getyValues()[j]*valueOfg[i][j]));
			}
			
			double[] newProbability = new double[inputValues.getNumberOfExamples()];
			for (int j = 0; j < prenormalisedProbability.length; j++) {
				newProbability[j] = prenormalisedProbability[j]/valueOfZ[i];
			}

			itrOutput.setIterationValueOfG(valueOfg);
			itrOutput.setNewPValues(newProbability);
			inputValues.setpValues(newProbability);
			weakClassifier.setInputValues(inputValues);
			
			double[] finalValueOfF = new double[inputValues.getNumberOfExamples()];
			for (int j = 0; j < inputValues.getNumberOfExamples(); j++) {
				finalValueOfF[j] = 0;
				for (int k = 0; k < inputValues.getNumberOfIterations(); k++) {
					if(valueOfg[k][j] != -1)
						finalValueOfF[j] = finalValueOfF[j] + valueOfg[k][j];
				}
			}
			itrOutput.setFinalValueOfF(finalValueOfF);
			
			double errorCount = 0;
			double[] yValue = inputValues.getyValues();
			for (int j = 0; j < inputValues.getNumberOfExamples(); j++) {
				//System.out.println("Fianl Value "+finalValueOfF[j]);
			//	System.out.println("y value "+yValue[j]);
				if(finalValueOfF[j] >= 0.0 && yValue[j] != 1)
					errorCount++;
				if(finalValueOfF[j] < 0.0 && yValue[j] != -1)
					errorCount++;
			}
		//	System.out.println("Error Count : "+errorCount);
			double valueOfEt;
			valueOfEt = (double)(errorCount/inputValues.getNumberOfExamples());
		//	System.out.println("Et : "+valueOfEt);
			itrOutput.setValueOfEt(valueOfEt);
			
			double boundOnEt = 1;
			for (int j = 0; j < inputValues.getNumberOfIterations(); j++) {
				if(valueOfZ[j] != -1)
					boundOnEt = boundOnEt * valueOfZ[j];  
			}
			itrOutput.setBoundOnEt(boundOnEt);
			
			outputValues.put(i, itrOutput);
			displayIterationOutput(bufferedWriter);
		}
		try {
			bufferedWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private double calculateValueOfZ(double prPlus, double pwMinus,
			double pwPlus, double prMinus, double cPlus, double cMinus) {
		double valueOfZ = (Math.exp(-1*cPlus) * prPlus) + (Math.exp(cMinus) * prMinus) + (Math.exp(-1*cMinus) * pwPlus) + (Math.exp(cPlus) * pwMinus);
		return valueOfZ;
	}

	private double calculateValueOfC(double value1, double value2) {
		double cValue;
		double epsilon = inputValues.getEpsilon();
		cValue = 0.5*(Math.log((value1+epsilon)/(value2+epsilon)));
		return cValue;
	}

	private void displayIterationOutput(BufferedWriter bufferedWriter) {
		try {
			
			if(itrOutput.isLeftPositive())
					bufferedWriter.write("Classifier h = I(x < "+(float)itrOutput.getWeakClassifierValue()+")");
			else
				bufferedWriter.write("Classifier h = I(x < "+(float)itrOutput.getWeakClassifierValue()+")");
			bufferedWriter.newLine();
			bufferedWriter.write("G error = "+(float)itrOutput.getErrorEpsilon());
			bufferedWriter.newLine();
			bufferedWriter.write("C_Plus = "+(float)itrOutput.getcPlus()+", "+"C_Minus = "+(float)itrOutput.getcMinus());
			//bufferedWriter.newLine();
			//bufferedWriter.write();
			bufferedWriter.newLine();
			bufferedWriter.write("Normalization Factor Z = "+(float)itrOutput.getValueOfZ());
			bufferedWriter.newLine();
			bufferedWriter.write("Pi after normalization = ");
			for (int j = 0; j < itrOutput.getNewPValues().length; j++) {
				bufferedWriter.write(""+(float)itrOutput.getNewPValues()[j]);
				if(j!=itrOutput.getNewPValues().length-1)
					bufferedWriter.write(", ");
			}
			bufferedWriter.newLine();
			bufferedWriter.write("f(x) = ");
			double[] finalValueOfF = itrOutput.getFinalValueOfF();
			for (int j = 0; j < inputValues.getNumberOfExamples(); j++) {
				bufferedWriter.write(""+(float)finalValueOfF[j]);
				if(j!=itrOutput.getNewPValues().length-1)
					bufferedWriter.write(", ");
			}
			bufferedWriter.newLine();
			bufferedWriter.write("Boosted Classifier Error = ");
			bufferedWriter.write(Double.toString((float)itrOutput.getValueOfEt()));
			bufferedWriter.newLine();
			bufferedWriter.write("Bound on Error = ");
			bufferedWriter.write(Double.toString((float)itrOutput.getBoundOnEt()));
			bufferedWriter.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int getTheBoostedErrorCount() {
		IterationOutput iterationOutput;
		double yValue[] = inputValues.getyValues();
		double finalPrediction[] = new double[inputValues.getNumberOfExamples()];
		double sum = 0;
		int errorCount = 0;
		for (int i = 0; i < inputValues.getNumberOfExamples(); i++) {
			sum = 0;
			for (int j = 0; j < outputValues.size(); j++) {
				iterationOutput = (IterationOutput)outputValues.get(j);
				sum = sum + (iterationOutput.getAlphaValue() * iterationOutput.getClassifierOutput()[i]);
			}
			if(sum < 0){
				finalPrediction[i] = -1;
				if(yValue[i] != finalPrediction[i])
					errorCount++;
			}else{
				finalPrediction[i] = 1;
				if(yValue[i] != finalPrediction[i])
					errorCount++;
			}
		}
		return errorCount;
	}

	private void calculateUpdatedProbabilities() {
		double[] qValues = calculateQValues();
		double[] pValues = inputValues.getpValues();
		double z = 0;
		for (int i = 0; i < inputValues.getNumberOfExamples(); i++) {
			z = z + (qValues[i]*pValues[i]);
		}
		itrOutput.setValueOfZ(z);
		
		double[] newPValues = new double[inputValues.getNumberOfExamples()];
		for (int i = 0; i < inputValues.getNumberOfExamples(); i++) {
			newPValues[i] = (qValues[i]*pValues[i])/z;
		}
		itrOutput.setNewPValues(newPValues);
		inputValues.setpValues(newPValues);
		weakClassifier.setInputValues(inputValues);
	}

	private double[] calculateQValues() {
		double alpha = itrOutput.getAlphaValue();
		double[] qValues = new double[inputValues.getNumberOfExamples()];
		double[] yValues = inputValues.getyValues();
		double[] xValues = inputValues.getxValues();
		
		for (int i = 0; i < inputValues.getNumberOfExamples(); i++) {
			if(itrOutput.isLeftPositive() && xValues[i] < itrOutput.getWeakClassifierValue()){
				if(yValues[i] == 1)
					qValues[i] = Math.pow(Math.E, -1*alpha);
				else if(yValues[i] == -1)
					qValues[i] = Math.pow(Math.E, alpha);
			}else if(itrOutput.isLeftPositive() && xValues[i] > itrOutput.getWeakClassifierValue()){
				if(yValues[i] == -1)
					qValues[i] = Math.pow(Math.E, -1*alpha);
				else if(yValues[i] == 1)
					qValues[i] = Math.pow(Math.E, alpha);
			}else if(!itrOutput.isLeftPositive() && xValues[i] < itrOutput.getWeakClassifierValue()){
				if(yValues[i] == -1)
					qValues[i] = Math.pow(Math.E, -1*alpha);
				else if(yValues[i] == 1)
					qValues[i] = Math.pow(Math.E, alpha);
			}else if(!itrOutput.isLeftPositive() && xValues[i] > itrOutput.getWeakClassifierValue()){
				if(yValues[i] == 1)
					qValues[i] = Math.pow(Math.E, -1*alpha);
				else if(yValues[i] == -1)
					qValues[i] = Math.pow(Math.E, alpha);
			}
				
		}
		return qValues;
	}

	private double calculateValueOfAlpha(double epsilon) {
		double alphaValue = 0;
		alphaValue = 0.5*(Math.log((1-epsilon)/epsilon));
		return alphaValue;
	}


}
