package com.ml.assignment2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class GradedHomework2 {
	
	static InputValues inputValues = new InputValues();
		static FileReader fd = null;
	static BufferedReader buff;
	
	public static void main(String args[]){
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter input file name : ");
		String inputFileName = scanner.nextLine();
		
		System.out.println("Enter output file name for BinaryAdaBoosting : ");
		String outputFileName1 = scanner.nextLine();
		System.out.println("Enter output file name for RealAdaBoosting : ");
		String outputFileName2 = scanner.nextLine();
		
		
		
		System.out.println("Binary Ada Boosting");
		System.out.println("The output is generated in the file ");
		readInputFile(inputFileName);
		BinaryAdaBoosting binaryAdaBoosting = new BinaryAdaBoosting(inputValues,outputFileName1);
		binaryAdaBoosting.binaryAdaBoostingExecution();
		
		System.out.println("Real Ada Boosting");
		System.out.println("The output is generated in the file ");
		readInputFile(inputFileName);
		RealAdaBoosting realAdaBoosting = new RealAdaBoosting(inputValues,outputFileName2);
		realAdaBoosting.binaryAdaBoostingExecution();
	}
	
	public static void readInputFile(String inputFileName){
		String stringValue;
		String[] values;
		try {
			fd = new FileReader(inputFileName);
			buff = new BufferedReader(fd);
			
			stringValue = buff.readLine();
			if(stringValue == null || stringValue.trim().isEmpty()){
				System.out.println("Error in the data of the file");
				System.exit(0);
			}
			values = stringValue.split(" ");
			if(values.length != 3){
				System.out.println("Error in the data of the file");
				System.exit(0);
			}
			
			inputValues.setNumberOfIterations(Integer.parseInt(values[0]));
			inputValues.setNumberOfExamples(Integer.parseInt(values[1]));
			inputValues.setEpsilon(Double.parseDouble(values[2]));
			
			stringValue = buff.readLine();
			inputValues.setxValues(readInputExamples(inputValues,stringValue));
			
			stringValue = buff.readLine();
			inputValues.setyValues(readInputExamples(inputValues,stringValue));

			stringValue = buff.readLine();
			inputValues.setpValues(readInputExamples(inputValues,stringValue));
			
			//displayInputValues(inputValues);
			
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
/*
	private static void displayInputValues(InputValues inputValues){
	//	System.out.println("The outputs are generated in the files ");
		//System.out.println("Number of iterations: "+inputValues.getNumberOfIterations());
	//	System.out.println("Number of examples: "+inputValues.getNumberOfExamples());
	//	System.out.println("epsilon: "+inputValues.getEpsilon());
	//	System.out.println("X Values ");
		for (int i = 0;i < inputValues.getxValues().length ; i++) {
		//	System.out.println(" "+inputValues.getxValues()[i]);
		}
	//	System.out.println("Y Values ");
		for (int i = 0;i < inputValues.getyValues().length ; i++) {
		//	System.out.println(" "+inputValues.getyValues()[i]);
		}
	//	System.out.println("P Values ");
		for (int i = 0;i < inputValues.getpValues().length ; i++) {
		//	System.out.println(" "+inputValues.getpValues()[i]);
		}

	}*/
	private static double[] readInputExamples(InputValues inputValues,String stringValue) {
		double readValues[];
		if(stringValue == null || stringValue.trim().isEmpty()){
			System.out.println("Error in the data of the file");
			System.exit(0);
		}
		String[] values = stringValue.trim().split(" ");
		if(values.length != inputValues.getNumberOfExamples()){
			System.out.println("Error in the data of the file");
			System.exit(0);
		}

		readValues = new double[inputValues.getNumberOfExamples()];
		int i = 0;
		for (String value : values) {
			readValues[i] = Double.parseDouble(value);
			i++;
		}
		return readValues;
	}
}

