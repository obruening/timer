package obruening.timer.controller;

import java.io.IOException;

import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;

@Component
public class HiProcinstSplitPaneFxmlController extends Controller {

	@FXML
	private SplitPane hiProcinstSplitPane;
	
    @FXML
    public void initialize() throws IOException {
        
    	hiProcinstSplitPane.getItems().add(loadFXML("/fxml/hi_procinst_table_view.fxml").getNode());
    	hiProcinstSplitPane.getItems().add(loadFXML("/fxml/hi_procinst_detail_pane.fxml").getNode());
      }
}
