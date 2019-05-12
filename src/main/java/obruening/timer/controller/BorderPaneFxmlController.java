package obruening.timer.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

@Component
public class BorderPaneFxmlController extends Controller {
    
    private static final Logger logger = LoggerFactory.getLogger(BorderPaneFxmlController.class);
    
    @FXML
    private Pane centerPane;

    @FXML
    private Pane leftPane;
    
    @FXML
    private BorderPane borderPane;
    
    @FXML
    private MenuItem closeMenuItem;

    @FXML
    private MenuItem aboutMenuItem;
      
    @FXML
    public void initialize() throws IOException {
        
        Platform.runLater(new Runnable() {
            
            @Override
            public void run() {
            	
            	logger.info(springContext.getBeanFactory().getBeanDefinition("borderPaneFxmlController").getScope());
            
            	try {
                    borderPane.setLeft(loadFXML("/fxml/left_pane.fxml").getNode());
                    borderPane.setCenter(loadFXML("/fxml/tabview_pane.fxml").getNode());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        
         
         closeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {

                Platform.exit();
            }
        });
         
         
         aboutMenuItem.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("About");
                alert.setHeaderText("Multi Instance Demo");
                alert.setContentText("Multi Instance Activities in Camunda");
                alert.showAndWait();
            }
        });
    }
}
