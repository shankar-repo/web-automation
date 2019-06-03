package com.nectar.ambrosia.reporting.listeners;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nectar.ambrosia.common.Consts;
import com.nectar.ambrosia.reporting.TestCase;
import com.nectar.ambrosia.reporting.TestSuite;
import com.nectar.ambrosia.runner.helpers.CurrentRunProperties;
import com.nectar.ambrosia.utilities.CommonUtilities;

public class SuitesManager {
	static Logger log = Logger.getLogger(SuitesManager.class.getName());
	
	private static Map<String, TestSuite> suites = new HashMap<String, TestSuite>();
	private static SuitesManager suiteManager = null;

	private SuitesManager() {
	}

	public static SuitesManager getInstance() {
		if (suiteManager == null) {
			synchronized (SuitesManager.class) {
				if (suiteManager == null) {
					suiteManager = new SuitesManager();
				}
			}
		}
		return suiteManager;
	}

	public synchronized void addTestSuite(String name, TestSuite testSuite) {
		suites.put(name, testSuite);
	}

	public TestSuite getTestSuite(String name) {
		return suites.get(name);
	}

	public Map<String, TestSuite> getAllSuites() {
		return suites;
	}

	public JsonObject getAllSuitesJsonObject() {
		JsonArray ja = new JsonArray();
		for (String ts : suites.keySet()) {
			ja.add(suites.get(ts).getTestSuiteJSON());
		}
		JsonObject jo = new JsonObject();
		jo.add(Consts.J_TESTSUITES, ja);

		return jo;
	}

	public void generateReport() {
		Map<String, TestSuite> suites = SuitesManager.getInstance().getAllSuites();
		for (String suiteName : suites.keySet()) {
			TestSuite suite = suites.get(suiteName);
			Map<String, TestCase> testCases = suite.getTestCases();

			for (String testCaseName : testCases.keySet()) {
				TestCase testCase = testCases.get(testCaseName);
				testCase.updateStatus();
			}
			suite.updateStatus();
		}
		String templateContent = CommonUtilities.getFileContent(Consts.HTMLREPORTEMPLPATH);
		templateContent = templateContent.replace(Consts.TESTSUITEJSON, getAllSuitesJsonObject().toString());
		String reportFile = CurrentRunProperties.getReportPath() + "automation-report.html";
		
		CommonUtilities.createFileWithContent(reportFile, templateContent);
	}

}
