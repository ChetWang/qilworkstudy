package com.creaway.inmemdb.demo.trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.creaway.inmemdb.InMemSession;
import com.creaway.inmemdb.trigger.InMemDBTrigger;

/**
 * DEMO: 数据库表TTTT （II, AA, BB），在数据有新数据插入时（INSERT），需要对新数据的在另一张表PPPP（II, CC,
 * DD）中进行记录。
 * 
 * @author Qil.Wong
 * 
 */
public class DemoTrigger1 extends InMemDBTrigger {

	@Override
	public void fireTrigger(InMemSession session, Object[] oldRow,
			Object[] newRow) throws SQLException {

		Connection conn = session.createConnection();
		try {
			PreparedStatement st = conn
					.prepareStatement("INSERT INTO PPPP VALUES(?,?,?)");
			// 因为这个触发器是对应新插入数据动作，因此使用的是newRow
			st.setObject(1, newRow[0]);
			st.setObject(2, newRow[1]);
			st.setObject(3, newRow[2]);
			st.execute();
		} finally {
			conn.close();
		}
	}
}
