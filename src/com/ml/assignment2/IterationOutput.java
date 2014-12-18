package com.ml.assignment2;

public class IterationOutput {
	double weakClassifierValue;
	boolean isLeftPositive;
	double errorEpsilon;
	double alphaValue;
	double valueOfZ;
	double[] newPValues;
	double[] classifierOutput;
	
	
	double valueOfG;
	double cPlus;
	double cMinus;
	double[][] iterationValueOfG;
	double[] finalValueOfF;
	double valueOfEt;
	double boundOnEt;
	
	public double getAlphaValue() {
		return alphaValue;
	}
	public void setAlphaValue(double alphaValue) {
		this.alphaValue = alphaValue;
	}
	public double getWeakClassifierValue() {
		return weakClassifierValue;
	}
	public void setWeakClassifierValue(double weakClassifierValue) {
		this.weakClassifierValue = weakClassifierValue;
	}
	public double getErrorEpsilon() {
		return errorEpsilon;
	}
	public boolean isLeftPositive() {
		return isLeftPositive;
	}
	public void setLeftPositive(boolean isLeftPositive) {
		this.isLeftPositive = isLeftPositive;
	}
	public void setErrorEpsilon(double errorEpsilon) {
		this.errorEpsilon = errorEpsilon;
	}
	public double getValueOfZ() {
		return valueOfZ;
	}
	public void setValueOfZ(double valueOfZ) {
		this.valueOfZ = valueOfZ;
	}
	public double[] getNewPValues() {
		return newPValues;
	}
	public void setNewPValues(double[] newPValues) {
		this.newPValues = newPValues;
	}
	public double[] getClassifierOutput() {
		return classifierOutput;
	}
	public void setClassifierOutput(double[] classifierOutput) {
		this.classifierOutput = classifierOutput;
	}
	public double getValueOfG() {
		return valueOfG;
	}
	public void setValueOfG(double valueOfG) {
		this.valueOfG = valueOfG;
	}
	public double getcPlus() {
		return cPlus;
	}
	public void setcPlus(double cPlus) {
		this.cPlus = cPlus;
	}
	public double getcMinus() {
		return cMinus;
	}
	public void setcMinus(double cMinus) {
		this.cMinus = cMinus;
	}
	public double[][] getIterationValueOfG() {
		return iterationValueOfG;
	}
	public void setIterationValueOfG(double[][] iterationValueOfG) {
		this.iterationValueOfG = iterationValueOfG;
	}
	public double[] getFinalValueOfF() {
		return finalValueOfF;
	}
	public void setFinalValueOfF(double[] finalValueOfF) {
		this.finalValueOfF = finalValueOfF;
	}
	public double getValueOfEt() {
		return valueOfEt;
	}
	public void setValueOfEt(double valueOfEt) {
		this.valueOfEt = valueOfEt;
	}
	public double getBoundOnEt() {
		return boundOnEt;
	}
	public void setBoundOnEt(double boundOnEt) {
		this.boundOnEt = boundOnEt;
	}
}
