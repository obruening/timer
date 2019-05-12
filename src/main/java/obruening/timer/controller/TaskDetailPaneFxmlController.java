package obruening.timer.controller;

import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import obruening.timer.store.Store;
import obruening.timer.store.event.NotifyEvent;
import obruening.timer.store.event.workflow.task.CompleteTaskEvent;

@Component
public class TaskDetailPaneFxmlController extends Controller {

	@Autowired
	private Store store;
	
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

	@FXML
	private Label label;
	
	@FXML
	private Button button;
	
	private Task task;
		
    @FXML
    public void initialize() {
    	
    	label.setText("No task selected");
    	button.setDisable(true);
    	button.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				applicationEventPublisher.publishEvent(new CompleteTaskEvent(task));
			}
		});
    }
    
	@EventListener
	private void onNotifyEvent(NotifyEvent notifyEvent) {
		task = store.getSelectedTask();
		button.setDisable(task == null);
		label.setText(store.getTaskMessage());
	}
}
