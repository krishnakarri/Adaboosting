package com.ml.assignment2;

public class InputValues {
	double xValues[];
	double yValues[];
	double pValues[];
	
	int numberOfIterations;
	int numberOfExamples;
	double epsilon;
	
	public int getNumberOfIterations() {
		return numberOfIterations;
	}
	public void setNumberOfIterations(int numberOfIterations) {
		this.numberOfIterations = numberOfIterations;
	}
	public int getNumberOfExamples() {
		return numberOfExamples;
	}
	public void setNumberOfExamples(int numberOfExamples) {
		this.numberOfExamples = numberOfExamples;
	}
	public double[] getxValues() {
		return xValues;
	}
	public void setxValues(double[] xValues) {
		this.xValues = xValues;
	}
	public double[] getyValues() {
		return yValues;
	}
	public void setyValues(double[] yValues) {
		this.yValues = yValues;
	}
	public double[] getpValues() {
		return pValues;
	}
	public void setpValues(double[] pValues) {
		this.pValues = pValues;
	}
	public double getEpsilon() {
		return epsilon;
	}
	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}
	
	
	
}
