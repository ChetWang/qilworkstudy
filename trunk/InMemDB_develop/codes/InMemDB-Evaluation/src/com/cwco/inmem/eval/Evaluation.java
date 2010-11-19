package com.cwco.inmem.eval;

import java.util.Random;

public abstract class Evaluation {

	protected String db_type;

	protected int concurrencySize;

	private char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ")
			.toCharArray();

	public Evaluation(int concurrencySize) {
		this.concurrencySize = concurrencySize;
		init();
	}
	
	protected  void init(){};

	public abstract void insert(int counts);

	public abstract void delete();

	public abstract void get(int fromIndex, int toIndex);

	public void printMemStatus(String prefix) {
		Runtime r = Runtime.getRuntime();
		r.totalMemory();
		System.out.println(prefix + " TOTAL " + r.totalMemory() / 1024 / 1024
				+ "MB   USED " + (r.totalMemory() - r.freeMemory()) / 1024
				/ 1024 + "MB");
	}

	public final String[] createString() {
		String[] value = new String[Constants.VALUE_COLUMN_COUNT];
		Random randGen = new Random();
		char[] randBuffer = new char[Constants.VALUE_LENGTH];
		for(int x=0;x<Constants.VALUE_COLUMN_COUNT;x++){
		for (int i = 0; i < Constants.VALUE_LENGTH; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
		}
			value[x] = new String(randBuffer);
		}
		return value;
	}

}
