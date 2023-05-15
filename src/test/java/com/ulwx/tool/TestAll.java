package com.ulwx.tool;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestAll extends TestCase{

	public static Test suite(){
		TestSuite test=new TestSuite();
		test.addTestSuite(CTimeTest.class);
		return test;
	}
	
}
