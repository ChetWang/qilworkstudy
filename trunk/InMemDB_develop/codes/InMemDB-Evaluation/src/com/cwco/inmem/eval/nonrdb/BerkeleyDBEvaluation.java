package com.cwco.inmem.eval.nonrdb;

import java.io.File;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.CacheMode;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public class BerkeleyDBEvaluation extends NonSQLEvaluation {

	private static DatabaseEntry theKey = new DatabaseEntry();
	private static DatabaseEntry theData = new DatabaseEntry();
	BDBEnv myDbEnv = new BDBEnv();
	EntryBinding dataBinding;

	public BerkeleyDBEvaluation(int concurrencySize) {
		super(concurrencySize);
		db_type = "BERKELEY DB";
		myDbEnv.setup(new File(System.getProperty("user.dir") + "/tmp/JEDB"),
				false);
		dataBinding = new SerialBinding(myDbEnv.getClassCatalog(),
				ObjectBean.class);
	}

	@Override
	public void insertObject(Object o) {

		theKey = new DatabaseEntry(intToByteArray1(((ObjectBean) o).getIndex()));

		// using our SerialBinding
		dataBinding.objectToEntry(o, theData);

		// Put it in the database. These puts are transactionally protected
		// (we're using autocommit).
		

		myDbEnv.getVendorDB().put(null, theKey, theData);
		// txn.commit();
	}

	public static byte[] intToByteArray1(int i) {
		byte[] result = new byte[4];
		result[0] = (byte) ((i >> 24) & 0xFF);
		result[1] = (byte) ((i >> 16) & 0xFF);
		result[2] = (byte) ((i >> 8) & 0xFF);
		result[3] = (byte) (i & 0xFF);
		return result;
	}

	@Override
	public void delete() {

	}

	@Override
	public void get(int fromIndex, int toIndex) {

	}

	public class BDBEnv {

		private Environment myEnv;

		// The databases that our application uses
		private Database objDb;

		private Database classCatalogDb;

		// Needed for object serialization
		private StoredClassCatalog classCatalog;

		public StoredClassCatalog getClassCatalog() {
			return classCatalog;
		}

		// Our constructor does nothing
		public BDBEnv() {
		}

		// The setup() method opens all our databases and the environment
		// for us.
		public void setup(File envHome, boolean readOnly)
				throws DatabaseException {

			EnvironmentConfig myEnvConfig = new EnvironmentConfig();
			
			DatabaseConfig myDbConfig = new DatabaseConfig();
			
//			myEnvConfig.setCacheMode(CacheMode.KEEP_HOT);
			myEnvConfig.setCachePercent(89);

			// If the environment is read-only, then
			// make the databases read-only too.
			myEnvConfig.setReadOnly(readOnly);
			myDbConfig.setReadOnly(readOnly);

			// If the environment is opened for write, then we want to be
			// able to create the environment and databases if
			// they do not exist.
			myEnvConfig.setAllowCreate(!readOnly);
			myDbConfig.setAllowCreate(!readOnly);

			// Allow transactions if we are writing to the database
			myEnvConfig.setTransactional(!readOnly);
			myDbConfig.setTransactional(!readOnly);

			// Open the environment
			myEnv = new Environment(envHome, myEnvConfig);
			
			// Transaction txn = myDbEnv.getEnv().beginTransaction(null, null);
			// Now open, or create and open, our databases
			// Open the vendors and inventory databases
			objDb = myEnv.openDatabase(null, "ObjectBean", myDbConfig);
			classCatalogDb = myEnv.openDatabase(null, "ClassCatalogDB",
					myDbConfig);

			// Create our class catalog
			classCatalog = new StoredClassCatalog(classCatalogDb);
			
			
		}

		// getter methods

		// Needed for things like beginning transactions
		public Environment getEnv() {
			return myEnv;
		}

		public Database getVendorDB() {
			return objDb;
		}

		// Close the environment
		public void close() {
			if (myEnv != null) {
				try {
					// Close the secondary before closing the primaries

					objDb.close();

					// Finally, close the environment.
					myEnv.close();
				} catch (DatabaseException dbe) {
					System.err.println("Error closing MyDbEnv: "
							+ dbe.toString());
					System.exit(-1);
				}
			}
		}
	}

}
