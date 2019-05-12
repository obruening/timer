package obruening.timer.util;

import javafx.scene.Parent;
import obruening.timer.controller.Controller;

public class Wrapper {
	
	private Parent node;
	private Controller controller;
	
	public Wrapper(Parent node, Controller controller) {
		this.node = node;
		this.controller = controller;
	}

	public Parent getNode() {
		return node;
	}

	public void setNode(Parent node) {
		this.node = node;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}
	
}
