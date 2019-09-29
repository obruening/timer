package obruening.timer.controller;

import java.util.List;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import obruening.timer.store.event.ProcessDefinitionSelectedEvent;

@Component
public class ProcessDefinitionTableViewFxmlController extends Controller {
    
    private static final Logger logger = LoggerFactory.getLogger(ProcessDefinitionTableViewFxmlController.class);
	
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    
    @Autowired
    private RepositoryService repositoryService;
	
    @FXML
    private TableView<ProcessDefinition> processDefinitionTableView;

    @FXML
    private TableColumn<ProcessDefinition, String> idColumn;
    
    @FXML
    private TableColumn<ProcessDefinition, String> nameColumn;

    @FXML
    private TableColumn<ProcessDefinition, String> keyColumn;

    @FXML
    private TableColumn<ProcessDefinition, Integer> versionColumn;
    
    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<ProcessDefinition, String>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<ProcessDefinition, String>("name"));
        keyColumn.setCellValueFactory(new PropertyValueFactory<ProcessDefinition, String>("key"));
        versionColumn.setCellValueFactory(new PropertyValueFactory<ProcessDefinition, Integer>("version"));
        
        processDefinitionTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ProcessDefinition>() {

			@Override
			public void changed(ObservableValue<? extends ProcessDefinition> observable, ProcessDefinition oldValue, ProcessDefinition newValue) {
				if (newValue != null) {
					applicationEventPublisher.publishEvent(new ProcessDefinitionSelectedEvent(newValue));
				}
			}
		});
        
        List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().active().orderByProcessDefinitionId().asc().list();
        
        ObservableList<ProcessDefinition> observableProcessDefinitionList = FXCollections.observableArrayList(processDefinitionList);
        processDefinitionTableView.setItems(observableProcessDefinitionList);
    }
}
