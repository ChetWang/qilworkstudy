package com.cwco.inmem.eval;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import com.cwco.inmem.eval.nonrdb.BerkeleyDBEvaluation;
import com.cwco.inmem.eval.nonrdb.DB4OEvaluation;
import com.cwco.inmem.eval.rdbms.DerbyEvaluation;
import com.cwco.inmem.eval.rdbms.H2Evaluation;
import com.cwco.inmem.eval.rdbms.HSQLDBEvaluation;
import com.cwco.inmem.eval.rdbms.MySQLEvaluation;
import com.cwco.inmem.eval.rdbms.OracleEvaluation;
import com.cwco.inmem.eval.rdbms.TimestenEvaluation;

public class TestMain {

	public static void main(String[] args) {
		File libFolder = new File(System.getProperty("user.dir") + "/lib/");
		for (File f : libFolder.listFiles()) {
			if (f.getName().endsWith("jar"))
				EvalClassLoader.addClassPath(f);
		}
		System.out.print("Supports for ");
		EvaluationClassEnum[] classEmums = EvaluationClassEnum.values();
		for (int i = 0; i < classEmums.length; i++) {
			if (i > 0) {
				System.out.print(",");
			}
			System.out.print(classEmums[i]);
		}
		System.out.print("\nInput target Evaluations(comma separated):");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		String target = "";
		try {
			target = in.readLine();
			String[] targets = target.split(",");
			if (target.trim().equals("all")) {
				EvaluationClassEnum[] all = EvaluationClassEnum.values();
				targets = new String[EvaluationClassEnum.values().length];
				for (int i = 0; i < targets.length; i++) {
					targets[i] = all[i].toString();
				}
			}
			System.out.print("Input total counts of inserted objects: ");
			in = new BufferedReader(new InputStreamReader(System.in));
			String countString = in.readLine();
			int counts = Integer.parseInt(countString);
			System.out
					.print("Input total concurrency size for the evaluation: ");
			in = new BufferedReader(new InputStreamReader(System.in));
			String concurrencySizeStr = in.readLine();
			int concurrencySize = Integer.parseInt(concurrencySizeStr);

			System.out.print("Input the value length for the evaluation: ");
			in = new BufferedReader(new InputStreamReader(System.in));
			Constants.VALUE_LENGTH = Integer.parseInt(in.readLine());

			System.out
					.print("Input the c_value COLUMN counts for the evaluation: ");
			in = new BufferedReader(new InputStreamReader(System.in));
			Constants.VALUE_COLUMN_COUNT = Integer.parseInt(in.readLine());

			System.out.println("************START************");
			for (String t : targets) {
				EvaluationClassEnum e = null;
				try {
					e = EvaluationClassEnum.valueOf(t.trim().toUpperCase());
				} catch (Exception ex) {
					System.err.println(t + " is NOT a valid target!");
					continue;
				}
				Class<?> c = null;
				switch (e) {
				case BERKELEY:
					c = BerkeleyDBEvaluation.class;
					break;
				case DB2:
					break;
				case DB4O:
					c = DB4OEvaluation.class;
					break;
				case DERBY:
					c = DerbyEvaluation.class;
					break;
				case H2:
					c = H2Evaluation.class;
					break;
				case HSQLDB:
					c = HSQLDBEvaluation.class;
					break;
				case MYIMPL:
					break;
				case MYSQL:
					c = MySQLEvaluation.class;
					break;
				case ORACLE:
					c = OracleEvaluation.class;
					break;
				case TIMESTEN:
					c = TimestenEvaluation.class;
					break;
				default:
					break;
				}

				if (c != null) {
					Evaluation evaluation = (Evaluation) c.getConstructor(
							int.class).newInstance(concurrencySize);
					evaluation.insert(counts);
					evaluation.get(Constants.TEST_DATA_FROM,
							Constants.TEST_DATA_TO);
				}
			}
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
	}

}
