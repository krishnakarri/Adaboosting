package com.ml.assignment2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;


public class BinaryAdaBoosting {
	InputValues inputValues;
	WeakClassifier weakClassifier;
	HashMap<Integer,IterationOutput> outputValues;
	IterationOutput itrOutput;
	String outputFileName;
	
	public BinaryAdaBoosting(InputValues inputValues,String outputFileName){
		this.inputValues = inputValues;
		weakClassifier = new WeakClassifier(inputValues);
		outputValues = new HashMap<Integer,IterationOutput>();
		this.outputFileName = outputFileName;
	}
	
	public void binaryAdaBoostingExecution() {
		double alphaValue;
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
			weakClassifier.getWeakClassifier();
			itrOutput = new IterationOutput();
			itrOutput.setWeakClassifierValue(weakClassifier.getWeakHypothesis());
			itrOutput.setLeftPositive(weakClassifier.isLeftPositive());
			itrOutput.setErrorEpsilon(weakClassifier.getErrorEpsilon());
			double[] xValues = inputValues.getxValues();
			double[] classifierOutput = new double[inputValues.getNumberOfExamples()];
			for (int j = 0; j < inputValues.numberOfExamples; j++) {
				if(weakClassifier.isLeftPositive()){
					if(xValues[j] < weakClassifier.getWeakHypothesis())
						classifierOutput[j] = 1;
					else
						classifierOutput[j] = -1;
				}else{
					if(xValues[j] < weakClassifier.getWeakHypothesis())
						classifierOutput[j] = -1;
					else
						classifierOutput[j] = 1;
				}
			}
			itrOutput.setClassifierOutput(classifierOutput);
			
			alphaValue = calculateValueOfAlpha(weakClassifier.getErrorEpsilon());
			itrOutput.setAlphaValue(alphaValue);
			
			calculateUpdatedProbabilities();  //adaboost-1.dat jxk141230_binary_1.txt  jxk141230_real_1.txt
			 
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
	

	private void displayIterationOutput(BufferedWriter bufferedWriter) {
		try {
			
			
		if(itrOutput.isLeftPositive())
			bufferedWriter.write("Classifier h = I(x < "+itrOutput.getWeakClassifierValue()+")");
		else
			bufferedWriter.write("Classifier h = I(x < "+itrOutput.getWeakClassifierValue()+")");
		bufferedWriter.newLine();
		bufferedWriter.write("Error = "+(float)itrOutput.getErrorEpsilon());
		bufferedWriter.newLine();
		bufferedWriter.write("Alpha = "+(float)itrOutput.getAlphaValue());
		bufferedWriter.newLine();
		bufferedWriter.write("Normalization Factor Z = "+(float)itrOutput.getValueOfZ());
		bufferedWriter.newLine();
		bufferedWriter.write("Pi after normalization = ");
		for (int i = 0; i < itrOutput.getNewPValues().length; i++) {
			bufferedWriter.write(""+(float)itrOutput.getNewPValues()[i]);
			if(i!=itrOutput.getNewPValues().length-1)
				bufferedWriter.write(", ");	
			
		}
		bufferedWriter.newLine();
		bufferedWriter.write("Boosted Classifier f(x) = ");
		for (int i = 0;i<outputValues.size();i++) {
			IterationOutput entry = (IterationOutput)outputValues.get(i);
			
			if(i > 0)
				bufferedWriter.write(" + ");
			bufferedWriter.write(Double.toString(((IterationOutput)entry).getAlphaValue()));
			if(entry.isLeftPositive())
				bufferedWriter.write(" * I(x < "+entry.getWeakClassifierValue()+")");
			else
				bufferedWriter.write(" * I(x > "+entry.getWeakClassifierValue()+")");
		}
		bufferedWriter.newLine();
		
		int errorCount = getTheBoostedErrorCount();
		double valueOfEt = (double)errorCount/inputValues.getNumberOfExamples();
		bufferedWriter.write("Boosted Classifier Error = "+valueOfEt);
		bufferedWriter.newLine();
		bufferedWriter.write("Bound on Error = ");
		double finalBound = 1;
		for (int i = 0;i<outputValues.size();i++) {
			IterationOutput iteratorOutput = (IterationOutput)outputValues.get(i);
			finalBound = finalBound * iteratorOutput.getValueOfZ();
		}
		bufferedWriter.write(Double.toString(finalBound));
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
