package com.pits.jmeter;

import java.io.File;
import java.util.LinkedList;

import org.apache.jmeter.control.Controller;
import org.apache.jmeter.control.GenericController;
import org.apache.jmeter.control.ReplaceableController;
import org.apache.jmeter.gui.tree.JMeterTreeNode;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.services.FileServer;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jorphan.collections.HashTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterController extends GenericController implements ReplaceableController {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1336475505980571264L;

	private static final Logger log = LoggerFactory.getLogger(FilterController.class);

	private static final String INCLUDE_PATH_VALUE = "FilterController.includepath.value";

	private static final String SWITCH_VALUE = "FilterController.switch.value";

	private HashTree tree = null;

	public void setIncludePath(String path) {
		log.debug("Setting include path: {}", path);
		setProperty(new StringProperty(INCLUDE_PATH_VALUE, path));
	}

	public String getIncludePath() {
		return getProperty(INCLUDE_PATH_VALUE).getStringValue();
	}

	public void setSwitchValue(String path) {
		log.debug("Setting swtich value: {}", path);
		setProperty(new StringProperty(SWITCH_VALUE, path));

	}

	public String getSwitchValue() {
		return getProperty(SWITCH_VALUE).getStringValue();
	}

	public HashTree getReplacementSubTree() {
		if (this.tree == null) {
			resolveReplacementSubTree(null);
		}

		return this.tree;
	}

	public void resolveReplacementSubTree(JMeterTreeNode context) {

		try {
			final String path = getIncludePath();
			if (path == null || path.isEmpty()) {
				log.info("Include path empty");
				this.tree = new HashTree();
				return;
			}
			File file = new File(path);
			if (file.isAbsolute() && !file.exists()) {
				log.info("File does not exist at {}", path);
				this.tree = new HashTree();
				return;
			}
			log.info("Trying to read file at  {}", file.getAbsolutePath());
			if (!file.isAbsolute() && !file.exists()) {
				file = new File(FileServer.getFileServer().getBaseDir(), path);
				log.info("Trying to read file from  {}", file.getAbsolutePath());
			}

			if (!file.exists()) {
				log.info("File does not exist at {}", file.getAbsolutePath());
				this.tree = new HashTree();
				return;
			}
			log.info("Searching elements at {}", file.getAbsolutePath());
			HashTree tree = SaveService.loadTree(file);
			this.tree = getProperBranch(tree);
			removeDisabledItems(this.tree);
		} catch (Exception e) {
			log.warn("Unexpected error", e);
			this.tree = new HashTree();
		}
	}

	protected HashTree getProperBranch(HashTree tree) {
		final String switchValue = getSwitchValue();

		final LinkedList<HashTree> elementsToCheck = new LinkedList<>();
		elementsToCheck.add(tree);

		while (!elementsToCheck.isEmpty()) {

			final HashTree item = elementsToCheck.pop();

			for (final Object o : item.list()) {

				if (o instanceof Controller) {
					Controller controller = ((Controller) o);
					if (switchValue == null || switchValue.isEmpty() || controller.getName().matches(switchValue)) {
						return item.get(o);
					}
				}
				elementsToCheck.add(item.get(o));
			}
		}

		return new HashTree();

	}

	protected static void removeDisabledItems(HashTree tree) {
		if (tree == null) {
			return;
		}
		for (Object o : new LinkedList<>(tree.list())) {
			TestElement item = (TestElement) o;
			if (!item.isEnabled()) {
				tree.remove(item);
			} else {
				removeDisabledItems(tree.getTree(item));
			}
		}
	}

}
