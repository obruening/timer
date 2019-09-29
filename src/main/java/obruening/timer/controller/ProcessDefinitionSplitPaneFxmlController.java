package obruening.timer.controller;

import java.io.IOException;

import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;

@Component
public class ProcessDefinitionSplitPaneFxmlController extends Controller {

	@FXML
	private SplitPane processDefinitionSplitPane;
	
    @FXML
    public void initialize() throws IOException {
        
        processDefinitionSplitPane.getItems().add(loadFXML("/fxml/process_definition_table_view.fxml").getNode());
        processDefinitionSplitPane.getItems().add(loadFXML("/fxml/process_definition_detail_pane.fxml").getNode());
      }
}
