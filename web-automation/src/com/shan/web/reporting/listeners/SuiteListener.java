package com.shan.web.reporting.listeners;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import com.shan.web.common.Status;
import com.shan.web.reporting.TestSuite;

public class SuiteListener implements ISuiteListener {
	static Logger log = Logger.getLogger(SuiteListener.class.getName());
	
	@Override
	public void onStart(ISuite suite) {
		TestSuite testSuite = new TestSuite(suite.getName());
		testSuite.setStatus(Status.RUNNING);
		testSuite.setStartTime(Calendar.getInstance().getTime());
		SuitesManager.getInstance().addTestSuite(suite.getName(), testSuite);
		log.info(suite.getName() + " suite started running");
	}

	@Override
	public void onFinish(ISuite suite) {
		SuitesManager.getInstance().getTestSuite(suite.getName()).setEndTime(Calendar.getInstance().getTime());
		log.info(suite.getName() + " suite completed");
	}

}
