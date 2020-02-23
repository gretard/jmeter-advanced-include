package com.pits.jmeter;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.jmeter.control.gui.AbstractControllerGui;
import org.apache.jmeter.gui.util.FilePanel;
import org.apache.jmeter.gui.util.VerticalPanel;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterControllerGui extends AbstractControllerGui {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3287024276830445030L;
	private static final Logger log = LoggerFactory.getLogger(FilterControllerGui.class);

	private final FilePanel includePanel = new FilePanel(JMeterUtils.getResString("include_path"), ".jmx");

	private JTextField switchValueFilter = new JTextField("");

	public TestElement createTestElement() {
		FilterController controller = new FilterController();
		modifyTestElement(controller);
		return controller;
	}

	public FilterControllerGui() {
		super();
		init();
		clearGui();
	}

	@Override
	public void clearGui() {
		super.clearGui();
		includePanel.clearGui();
		switchValueFilter.setText("");
	}

	private void init() {
		setLayout(new BorderLayout(0, 5));
		setBorder(makeBorder());
		add(makeTitlePanel(), BorderLayout.NORTH);

		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.setBorder(BorderFactory.createEtchedBorder());
		mainPanel.add(includePanel);
		mainPanel.add(createSwitchPanel());
		add(mainPanel, BorderLayout.CENTER);

	}

	@Override
	public void configure(TestElement el) {
		super.configure(el);
		if (el instanceof FilterController) {
			FilterController controller = (FilterController) el;
			this.includePanel.setFilename(controller.getIncludePath());
			this.switchValueFilter.setText(controller.getSwitchValue());
		}

	}

	@Override
	public String getLabelResource() {
		return getClass().getCanonicalName();
	}

	@Override
	public String getStaticLabel() {
		return "Advanced include controller";
	}

	public void modifyTestElement(TestElement testElement) {
		log.info("modifying  test element" + testElement.getName());
		super.configureTestElement(testElement);
		if (testElement instanceof FilterController) {
			FilterController e = (FilterController) testElement;
			e.setIncludePath(includePanel.getFilename());
			e.setSwitchValue(switchValueFilter.getText());
		}

	}

	private JPanel createSwitchPanel() {
		JPanel switchPanel = new JPanel(new BorderLayout(5, 0));
		JLabel selectionLabel = new JLabel("Switch value");
		selectionLabel.setLabelFor(switchValueFilter);
		switchPanel.add(selectionLabel, BorderLayout.WEST);
		switchPanel.add(switchValueFilter, BorderLayout.CENTER);
		return switchPanel;
	}

}
