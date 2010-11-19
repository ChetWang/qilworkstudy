package com.creaway.inmemdb.instance;

import com.creaway.inmemdb.cluster.ClusterIfc;
import com.creaway.inmemdb.cluster.ClusterSupport;

public class InstanceTest {
	
	public static void main(String...strings ){
		ClusterSupport cls = new ClusterSupport();
		System.out.println(ClusterIfc.class.isInstance(cls));
	}

}
