package com.creaway.inmemdb.trigger;

import com.creaway.inmemdb.InMemSession;
import com.creaway.inmemdb.api.datapush.PushActionListener;
import com.creaway.inmemdb.datapush.InMemDBCommandPushTrigger;

/**
 * 数据推送模式触发器测试
 * 
 * @author Qil.Wong
 * 
 */
public class PushTriggerTest extends InMemDBCommandPushTrigger {

	public PushTriggerTest() {
		System.out.println("init " + getClass());
		addPushActionListener(new PushActionListener() {

			@Override
			public void actionPerformed(Object o) {
				System.out.println("local  " + o);
//				try {
//					Thread.sleep(200);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				o.toString();
			}
		});
	}

	@Override
	public Object isSatisfied(InMemSession session, Object[] oldRow, Object[] newRow) {
		StringBuffer sb = new StringBuffer();
		if (type == InMemDBTrigger.TYPE_DELETE) {
			for (Object o : oldRow) {
				sb.append(o.toString()).append(" ");
			}
		}else{
			for (Object o : newRow) {
				sb.append(o.toString()).append(" ");
			}
		}
		return sb.toString();
//		ArrayList l = new ArrayList();
//		l.add("xxxxxxxxxx");
//		l.add(new Date());
//		l.add(11111);
//		l.add(0.00123f);
//		l.add(newRow);
//		return l;
	}

}
