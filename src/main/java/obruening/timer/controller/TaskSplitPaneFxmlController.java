package obruening.timer.controller;

import java.io.IOException;

import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;

@Component
public class TaskSplitPaneFxmlController extends Controller {

	@FXML
	private SplitPane taskSplitPane;
	
    @FXML
    public void initialize() throws IOException {
        
    	taskSplitPane.getItems().add(loadFXML("/fxml/task_table_view.fxml").getNode());
    	taskSplitPane.getItems().add(loadFXML("/fxml/task_detail_pane.fxml").getNode());
      }
}
