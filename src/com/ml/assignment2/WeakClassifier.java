package com.ml.assignment2;

import java.util.ArrayList;
import java.util.List;

import org.omg.CORBA.portable.ValueBase;

public class WeakClassifier {

	InputValues inputValues;
	double weakHypothesis;
	boolean isLeftPositive;
	double errorEpsilon;
	double prPlus;
	double prMinus;
	double pwPlus;
	double pwMinus;
	double valuOfG;
	List<String> hypothesisCombination;
	double[] hypothesisOutputValues;
	
	public WeakClassifier(InputValues inputValues) {
		this.inputValues = inputValues;
		loadCombinations();
	}
	
	private void loadCombinations() {
	//	System.out.println("In Load");
		hypothesisCombination = new ArrayList<String>();
		hypothesisCombination.add(0.0+"#true");
		hypothesisCombination.add(0.0+"#false");
		for (int i = 0; i < inputValues.getNumberOfExamples()-1; i++) {
			double decisionValue = (inputValues.getxValues()[i]+inputValues.getxValues()[i+1])/2;
			hypothesisCombination.add(decisionValue+"#true");
			hypothesisCombination.add(decisionValue+"#false");
		}		
	//	hypothesisCombination.add(inputValues.getxValues()[inputValues.getNumberOfExamples() - 1]);
	}

	public void getWeakClassifier(){
		errorEpsilon = 1;
		isLeftPositive = false;
		weakHypothesis = 0;
		double[] xValues = inputValues.getxValues();
		double[] yValues = inputValues.getyValues();
		double[] pValues = inputValues.getpValues();
		double error = 0;
		double decisionValue;
		for (int i = 0; i < hypothesisCombination.size(); i++) {
			//case for isLeftPositive true
			String selectedHypothesis = hypothesisCombination.get(i);
			String[] splitValues = selectedHypothesis.split("#");
			decisionValue = Double.parseDouble(splitValues[0]);
			if("true".equals(splitValues[1])){
			error = 0;
			for (int j = 0; j < xValues.length; j++) {
				if(xValues[j] < decisionValue){
					if(yValues[j] == -1)
						error = error + pValues[j];
				}else if(xValues[j] > decisionValue){
					if(yValues[j] == 1)
						error = error + pValues[j];
				}
			}
			
			if(error < errorEpsilon){
				errorEpsilon = error;
				weakHypothesis = decisionValue;
				isLeftPositive = true;
			}
			}else if("false".equals(splitValues[1])){
			//case for isLeftPositive false
			error = 0;
			for (int j = 0; j < xValues.length; j++) {
				if(xValues[j] < decisionValue){
					if(yValues[j] == 1)
						error = error + pValues[j];
				}else if(xValues[j] > decisionValue){
					if(yValues[j] == -1)
						error = error + pValues[j];
				}
			}
			
			if(error < errorEpsilon){
				errorEpsilon = error;
				weakHypothesis = decisionValue;
				isLeftPositive = false;
			}
			}
		}
		//hypothesisCombination.remove(weakHypothesis+"#"+isLeftPositive);
	}
	
	public void getWeakClassifierForRealAdaBoosting(){
		valuOfG = 1;
		isLeftPositive = false;
		weakHypothesis = 0;
		
		double[] xValues = inputValues.getxValues();
		double[] yValues = inputValues.getyValues();
		double[] pValues = inputValues.getpValues();
		double GValue = 0;
		double decisionValue;
		double prPlusTmp = 0;
		double prMinusTmp = 0;
		double pwPlusTmp = 0;
		double pwMinusTmp = 0;
		String isPositive;
		double hypothesisOutput[] = new double[inputValues.getNumberOfExamples()];
		hypothesisOutputValues = new double[inputValues.getNumberOfExamples()];
		for (int i = 0; i < hypothesisCombination.size(); i++) {
			prPlusTmp = 0;
			prMinusTmp = 0;
			pwPlusTmp = 0;
			pwMinusTmp = 0;
			decisionValue = Double.parseDouble(hypothesisCombination.get(i).split("#")[0]);
			isPositive = (String)(hypothesisCombination.get(i).split("#")[1]);
			for (int j = 0; j < inputValues.getNumberOfExamples(); j++) {
				if("true".equals(isPositive)){
					if(xValues[j]<decisionValue)
						hypothesisOutput[j] = 1;
					else
						hypothesisOutput[j] = -1;
				}else if("false".equals(isPositive)){
					if(xValues[j]<decisionValue)
						hypothesisOutput[j] = -1;
					else
						hypothesisOutput[j] = 1;
				}
			}
			
			for (int j = 0; j < inputValues.getNumberOfExamples(); j++) {
				if(hypothesisOutput[j] == 1){
					if(yValues[j] == 1)
						prPlusTmp = prPlusTmp + pValues[j];
					else
						pwMinusTmp = pwMinusTmp + pValues[j];
				}else if(hypothesisOutput[j] == -1){
					if(yValues[j] == 1)
						pwPlusTmp = pwPlusTmp + pValues[j];
					else
						prMinusTmp = prMinusTmp + pValues[j];
				}
			}
			
			GValue = (Math.sqrt(prPlusTmp*pwMinusTmp)) + (Math.sqrt(prMinusTmp*pwPlusTmp));
			if(GValue < valuOfG){
				setWeakHypothesis(Double.parseDouble(hypothesisCombination.get(i).split("#")[0]));
				valuOfG = GValue;
				if("true".equals(hypothesisCombination.get(i).split("#")[1]))
					isLeftPositive = true;
				else
					isLeftPositive = false;
				prPlus = prPlusTmp;
				prMinus = prMinusTmp;
				pwPlus = pwPlusTmp;
				pwMinus  = pwMinusTmp;
				for (int j = 0; j < hypothesisOutput.length; j++) {
					hypothesisOutputValues[j] = hypothesisOutput[j];	
				}
			}
		}
	}

	public double getWeakHypothesis() {
		return weakHypothesis;
	}

	public void setWeakHypothesis(double weakHypothesis) {
		this.weakHypothesis = weakHypothesis;
	}

	public boolean isLeftPositive() {
		return isLeftPositive;
	}

	public void setLeftPositive(boolean isLeftPositive) {
		this.isLeftPositive = isLeftPositive;
	}

	public double getErrorEpsilon() {
		return errorEpsilon;
	}

	public void setErrorEpsilon(double errorEpsilon) {
		this.errorEpsilon = errorEpsilon;
	}

	public InputValues getInputValues() {
		return inputValues;
	}

	public void setInputValues(InputValues inputValues) {
		this.inputValues = inputValues;
	}

	public double getPrPlus() {
		return prPlus;
	}

	public void setPrPlus(double prPlus) {
		this.prPlus = prPlus;
	}

	public double getPrMinus() {
		return prMinus;
	}

	public void setPrMinus(double prMinus) {
		this.prMinus = prMinus;
	}

	public double getPwPlus() {
		return pwPlus;
	}

	public void setPwPlus(double pwPlus) {
		this.pwPlus = pwPlus;
	}

	public double getPwMinus() {
		return pwMinus;
	}

	public void setPwMinus(double pwMinus) {
		this.pwMinus = pwMinus;
	}

	public double getValuOfG() {
		return valuOfG;
	}

	public void setValuOfG(double valuOfG) {
		this.valuOfG = valuOfG;
	}

	public List<String> getHypothesisCombination() {
		return hypothesisCombination;
	}

	public void setHypothesisCombination(List<String> hypothesisCombination) {
		this.hypothesisCombination = hypothesisCombination;
	}

	public double[] getHypothesisOutputValues() {
		return hypothesisOutputValues;
	}

	public void setHypothesisOutputValues(double[] hypothesisOutputValues) {
		this.hypothesisOutputValues = hypothesisOutputValues;
	}

}
