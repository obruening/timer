package obruening.timer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import obruening.timer.command.Command;
import obruening.timer.store.event.AutoUpdateEvent;

@Component
public class LeftPaneFxmlController extends Controller {
    
    @Autowired
    private List<Command> commandList;
    
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
	
    @FXML
    private VBox vBox;

    private EventHandler<ActionEvent> eventHandler;
    
    @FXML
    public void initialize() {
        
         eventHandler = new EventHandler<ActionEvent>() {
             
             @Override
             public void handle(ActionEvent event) {
                 
                 Command command = (Command)((Button)event.getSource()).getUserData();
                 command.execute();
             }
         };
        
         
         for (Command command : commandList) {
             vBox.getChildren().add(createCommandButton(command));
         }
         
         CheckBox autoUpdateCheckBox = new CheckBox("Auto Refresh 5 sec.");
         autoUpdateCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				
				applicationEventPublisher.publishEvent(new AutoUpdateEvent(newValue));
			}
		 });
         
         vBox.getChildren().add(autoUpdateCheckBox);
         double margin = 10;
         VBox.setMargin(autoUpdateCheckBox, new Insets(margin, margin, 0, margin));
         
    }
    
    private Button createCommandButton(Command command) {
        Button button = new Button(command.getName());
        button.setMinWidth(100);
        button.setUserData(command);
        button.setOnAction(eventHandler);
        
        double margin = 10;
        VBox.setMargin(button, new Insets(margin, margin, 0, margin));
        
        return button;
    }
}
