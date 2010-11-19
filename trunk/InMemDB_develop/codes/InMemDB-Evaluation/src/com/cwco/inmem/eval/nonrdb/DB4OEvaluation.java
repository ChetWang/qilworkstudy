package com.cwco.inmem.eval.nonrdb;

import java.util.concurrent.CountDownLatch;

import com.cwco.inmem.eval.rdbms.RDBMSEvaluation.FromToSearchThread;
import com.cwco.inmem.eval.rdbms.RDBMSEvaluation.SingleValueSearchThread;
import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.config.FileConfiguration;
import com.db4o.io.MemoryStorage;

public class DB4OEvaluation extends NonSQLEvaluation {

	protected EmbeddedObjectContainer container;

	public DB4OEvaluation(int concurrencySize) {
		super(concurrencySize);
		db_type = "DB4O 7";
		EmbeddedConfiguration embeddedConfiguration = Db4oEmbedded
				.newConfiguration();
		FileConfiguration fileConfiguration = embeddedConfiguration.file();
		fileConfiguration.storage(new MemoryStorage());
		container = Db4oEmbedded.openFile(embeddedConfiguration,
				"myEmbeddedDb.db4o");

	}

	@Override
	public void delete() {

	}

	@Override
	public void get(int fromIndex, int toIndex) {
		System.out.println(db_type + " SINGLE QUERY(SINGLE KEY) STARTED.");

		CountDownLatch cdl1 = new CountDownLatch(concurrencySize);
		CountDownLatch cdl2 = new CountDownLatch(concurrencySize);
		long t1 = System.nanoTime();
		for (int i = 0; i < concurrencySize; i++) {
//			new SingleValueSearchThread(cdl1, i, fromIndex).start();
		}
		try {
			cdl1.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}

		long t2 = System.nanoTime();

		System.out.println(db_type
				+ " QUERY(SINGLE KEY) FINISHED.  Total time spends="
				+ (t2 - t1) / 1000000 + " ms");

		System.out.println(db_type + " SINGLE QUERY(COMPLEX) STARTED.");
		for (int i = 0; i < concurrencySize; i++) {
//			new FromToSearchThread(cdl2, i, fromIndex, toIndex).start();
		}
		try {
			cdl2.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		long t3 = System.nanoTime();
		System.out.println(db_type
				+ " QUERY(COMPLEX) FINISHED.  Total time spends=" + (t3 - t2)
				/ 1000000 + " ms");
	}

	@Override
	public void insertObject(Object o) {
		container.store(o);
	}

}
