package com.creaway.inmemdb.demo.trigger;

import com.creaway.inmemdb.InMemSession;
import com.creaway.inmemdb.datapush.InMemDBCommandPushTrigger;

public class DemoTrigger2 extends InMemDBCommandPushTrigger{

	@Override
	public Object isSatisfied(InMemSession session, Object[] oldRow,
			Object[] newRow) {
		
		return newRow;
	}

}
