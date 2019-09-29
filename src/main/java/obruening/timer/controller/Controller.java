package obruening.timer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import obruening.timer.util.Wrapper;

public abstract class Controller {
    
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
	
	protected List<Wrapper> wrapperList = new ArrayList<>();
	
    @Autowired
    protected ConfigurableApplicationContext springContext;
    
    protected Wrapper loadFXML(String resourceName) throws IOException {
        logger.info(String.format("loading %s ...", resourceName));
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(resourceName));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Parent parent = fxmlLoader.load();
        Controller controller = fxmlLoader.getController();
        return new Wrapper(parent, controller);
    }
	
	protected Optional<Wrapper> findByController(Controller controller) {
		return wrapperList.stream().filter(w -> w.getController().equals(controller)).findFirst();
	}

	public List<Wrapper> getWrapperList() {
		return wrapperList;
	}

	public void setWrapperList(List<Wrapper> wrapperList) {
		this.wrapperList = wrapperList;
	}
}
