package obruening.timer.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import obruening.timer.store.Store;
import obruening.timer.store.event.NotifyEvent;
import obruening.timer.store.event.workflow.task.TaskSelectedEvent;

@Component
public class TaskTableViewFxmlController extends Controller {
    
    private static final Logger logger = LoggerFactory.getLogger(TaskTableViewFxmlController.class);

	@Autowired
	private Store store;
	
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
	
    @FXML
    private TableView<Task> taskTableView;

    @FXML
    private TableColumn<Task, String> idColumn;
    
    @FXML
    private TableColumn<Task, String> taskDefinitionKeyColumn;

    @FXML
    private TableColumn<Task, String> processInstanceIdColumn;

    @FXML
    private TableColumn<Task, String> processDefinitionIdColumn;

    @FXML
    private TableColumn<Task, String> assigneeColumn;
    
    @FXML
    private TableColumn<Task, Date> dueDateColumn;

    @FXML
    private TableColumn<Task, String> overdueColumn;
    
    private List<Task> taskList;

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("id"));
        taskDefinitionKeyColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("taskDefinitionKey"));
        processInstanceIdColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("processInstanceId"));
        processDefinitionIdColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("processDefinitionId"));
        assigneeColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("assignee"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<Task, Date>("dueDate"));
        overdueColumn.setCellValueFactory(cellData -> new SimpleStringProperty(getOverdueMessage(cellData.getValue().getDueDate())));
        
        taskTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Task>() {

			@Override
			public void changed(ObservableValue<? extends Task> observable, Task oldValue, Task newValue) {
				if (newValue != null) {
					applicationEventPublisher.publishEvent(new TaskSelectedEvent(newValue));
				}
			}
		});
        
        update();
    }
    
    private String getOverdueMessage(Date dueDate) {

        if (dueDate == null) {
        	return "";
        }
                
    	LocalDateTime dueDateLocalDateTime = LocalDateTime.ofInstant(dueDate.toInstant(), ZoneId.systemDefault());
    	return LocalDateTime.now().isAfter(dueDateLocalDateTime) ? "overdue" : "ok";
    }
    
	@EventListener
	private void onNotifyEvent(NotifyEvent notifyEvent) {
		update();
	}

    private void update() {

        List<Task> newTaskList = store.getTaskList();
        
        if (newTaskList != taskList) {
        	logger.info("neue Taskliste");
        	taskList = newTaskList;
            ObservableList<Task> observableTaskList = FXCollections.observableArrayList(taskList);
            taskTableView.setItems(observableTaskList);
            taskTableView.refresh();
            
            if (store.getSelectedTask() != null) {
            	taskTableView.getSelectionModel().select(store.getSelectedTask());
            } else {
            	taskTableView.getSelectionModel().clearSelection();
            }
        } else {
        	logger.info("taskliste unverändert");
        }
    }
}
