package obruening.timer.controller;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.Metamodel;

import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LongStringConverter;
import obruening.timer.store.Store;
import obruening.timer.store.event.NotifyEvent;
import obruening.timer.store.event.workflow.task.CompleteTaskEvent;

@Component
public class TaskDetailPaneFxmlController extends Controller {

    private static final Logger logger = LoggerFactory.getLogger(TaskDetailPaneFxmlController.class);

    @Autowired
    private Store store;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    
    @PersistenceContext
    private EntityManager entityManager;

    @FXML
    private Label label;

    @FXML
    private Button button;

    private Task selectedTask;

    @FXML
    private TableView<Variable> tableView;

    @FXML
    private TableColumn<Variable, String> nameValueColumn;
    
    @FXML
    private TableColumn<Variable, Double> doubleValueColumn;
    
    @FXML
    private TableColumn<Variable, Long> longValueColumn;

    @FXML
    private TableColumn<Variable, String> textValueColumn;

    @FXML
    private TableColumn<Variable, String> text2ValueColumn;
    
    private Variable getVariable(CellEditEvent<Variable, ?> cellEditEvent) {
        return cellEditEvent.getTableView().getItems().get(cellEditEvent.getTablePosition().getRow());
    }

    @FXML
    public void initialize() {
        
        tableView.setEditable(true);
        
        nameValueColumn.setCellValueFactory(new PropertyValueFactory<Variable, String>("nameValue"));
        
        doubleValueColumn.setCellValueFactory(new PropertyValueFactory<Variable, Double>("doubleValue"));
        doubleValueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        doubleValueColumn.setOnEditCommit(new EventHandler<CellEditEvent<Variable, Double>>() {
            @Override
            public void handle(CellEditEvent<Variable, Double> t) {

                getVariable(t).setDoubleValue(t.getNewValue());
            }
        });

        longValueColumn.setCellValueFactory(new PropertyValueFactory<Variable, Long>("longValue"));
        longValueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new LongStringConverter()));
        longValueColumn.setOnEditCommit(new EventHandler<CellEditEvent<Variable, Long>>() {
            @Override
            public void handle(CellEditEvent<Variable, Long> t) {
                
                getVariable(t).setLongValue(t.getNewValue());
            }
        });

        textValueColumn.setCellValueFactory(new PropertyValueFactory<Variable, String>("textValue"));
        textValueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        textValueColumn.setOnEditCommit(new EventHandler<CellEditEvent<Variable, String>>() {
            @Override
            public void handle(CellEditEvent<Variable, String> t) {
                (t.getTableView().getItems().get(t.getTablePosition().getRow())).setTextValue(t.getNewValue());
            }
        });


        label.setText("No task selected");
        button.setDisable(true);
        button.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                
                Map<String, Object> map = new TreeMap<>();
                for (Variable variable: tableView.getItems()) {
                    map.put(variable.getNameValue(), variable.getValue());
                    
                }

                applicationEventPublisher.publishEvent(new CompleteTaskEvent(selectedTask, map));
            }
        });
    }

    @EventListener
    private void onNotifyEvent(NotifyEvent notifyEvent) {
        selectedTask = store.getSelectedTask();

        button.setDisable(selectedTask == null);
        label.setText(store.getTaskMessage());
        
        
        ObservableList<Variable> observableVariableList = FXCollections.observableArrayList();
        
        Map<String, Object> selectedVariableMap = store.getSelectedVariableMap();
        if (selectedVariableMap != null) {
            
            List<Variable> variableList = selectedVariableMap
              .entrySet()
              .stream()
              .sorted(Map.Entry.comparingByKey()) 
              .map(this::toVariable)
              .collect(Collectors.toList());
        
          observableVariableList = FXCollections.observableArrayList(variableList);
        }
        tableView.setItems(observableVariableList);
    }
    
    private Variable toVariable(Map.Entry<String, Object> entry) {
        
        Variable variable = new Variable();
        variable.setNameValue(entry.getKey());
        Object value = entry.getValue();
        if (value instanceof String) {
            variable.setTextValue((String)value);
        } else if (value instanceof Long) {
            variable.setLongValue((Long)value);
        } else if (value instanceof Double) {
            variable.setDoubleValue((Double)value);
        } else if (isEntity(value.getClass())) {
            variable.setObject(value);
        }
        return variable;
    }
    
    public boolean isEntity(Class<?> clazz){
        Metamodel metamodel = entityManager.getMetamodel();
        try {
            metamodel.entity(clazz);
        } catch (IllegalArgumentException e) {
            // NOTE: the exception means the class is NOT an entity. There is no reason to log it.
            return false;
        }
        return true;
    }
}
