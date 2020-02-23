package com.pits.jmeter;

import static org.junit.Assert.*;

import org.apache.jmeter.control.TransactionController;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Test;

public class FilterControllerTest {

	{
		JMeterUtils.setJMeterHome("./data");
	}

	@Test
	public void testGetReplacementSubTree() {

		FilterController sut = new FilterController();
		sut.setIncludePath("./data/Sample.jmx");
		sut.setSwitchValue("OpenOverview");
		HashTree tree = sut.getReplacementSubTree();
		Assert.assertEquals(3, tree.size());
	}

	@Test
	public void testGetReplacementSubTreeWhenPathIsNull() {
		FilterController sut = new FilterController();
		sut.setIncludePath(null);
		sut.setSwitchValue("OpenOverview");
		HashTree tree = sut.getReplacementSubTree();
		Assert.assertEquals(0, tree.size());
	}

	@Test
	public void testGetReplacementSubTreeWhenSwitchValueIsNull() {

		FilterController sut = new FilterController();
		sut.setIncludePath("./data/Sample.jmx");
		sut.setSwitchValue(null);
		HashTree tree = sut.getReplacementSubTree();
		Assert.assertEquals(2, tree.size());
	}

}
