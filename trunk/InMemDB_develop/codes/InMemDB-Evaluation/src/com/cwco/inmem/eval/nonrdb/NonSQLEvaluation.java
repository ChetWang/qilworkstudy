package com.cwco.inmem.eval.nonrdb;

import java.util.concurrent.CountDownLatch;

import com.cwco.inmem.eval.Evaluation;

public abstract class NonSQLEvaluation extends Evaluation {

	public NonSQLEvaluation(int concurrencySize) {
		super(concurrencySize);
	}

	public abstract void insertObject(Object o);

	@Override
	public void insert(int counts) {
		System.out.println(db_type + " INSERT START.   ROW COUNTS = " + counts);
		System.out.println(db_type + " TOTAL CONCURRENT THREADS = "
				+ concurrencySize);
		long t = System.nanoTime();
		CountDownLatch countDown = new CountDownLatch(concurrencySize);
		printMemStatus(db_type + " MEM STATUS WHEN STARTED: ");
		for (int i = 0; i < concurrencySize; i++) {
			new InsertThread(countDown, i, counts / concurrencySize).start();
		}
		try {
			countDown.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(db_type + " INSERT FINISHED.  Total time spends="
				+ (System.nanoTime() - t) / 1000000 + " ms");
		printMemStatus(db_type + " MEM STATUS WHEN FINISHED: ");

	}

	/**
	 * Ensure same objects to insert,contain key-value array
	 * 
	 * @return
	 */
	public ObjectBean generateObject(int index) {
		return new ObjectBean(index, createString());
	}

	public class InsertThread extends Thread {

		CountDownLatch cdl;
		int concurrencyIndex;
		/**
		 * total row counts
		 */
		int count;

		public InsertThread(CountDownLatch cdl, int concurrencyIndex, int count) {
			this.cdl = cdl;
			this.concurrencyIndex = concurrencyIndex;
			this.count = count;
		}

		public void run() {
			try {
				for (int i = 0; i < count; i++) {
					ObjectBean obj = NonSQLEvaluation.this.generateObject(concurrencyIndex * count
							+ i);
					insertObject(obj);
				}
				cdl.countDown();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
