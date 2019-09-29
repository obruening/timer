package obruening.timer.controller;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.Metamodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LongStringConverter;
import obruening.timer.starter.ProcessStarter;
import obruening.timer.store.Store;
import obruening.timer.store.event.NotifyEvent;
import obruening.timer.store.event.RefreshEvent;

@Component
public class ProcessDefinitionDetailPaneFxmlController extends Controller {
    
    private static final Logger logger = LoggerFactory.getLogger(ProcessDefinitionDetailPaneFxmlController.class);

	@Autowired
	private Store store;
	
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    
    @PersistenceContext
    private EntityManager entityManager;

	@FXML
	private Label label;
	
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
    
    @FXML
    private TableColumn<Variable, Variable> actionColumn;
    
	@FXML
	private Button button;
	
	@FXML
	private TextField nameTextField;

    @FXML
	private TextField longValueTextField;

    @FXML
    private TextField doubleValueTextField;

    @FXML
    private TextField textValueTextField;
	
	@FXML
    private Button addButton;
    
	
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
        
        text2ValueColumn.setCellValueFactory(new PropertyValueFactory<Variable, String>("text2Value"));
        

        actionColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        actionColumn.setCellFactory(param -> new TableCell<Variable, Variable>() {
            private final Hyperlink deleteButton = new Hyperlink("Delete");

            @Override
            protected void updateItem(Variable variable, boolean empty) {
                super.updateItem(variable, empty);

                if (variable == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);
                deleteButton.setOnAction(event -> getTableView().getItems().remove(variable));
            }
        });
        
        
    	label.setText("No task selected");
    	button.setDisable(false);
    	button.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
			    
			    Map<String, Object> map = new TreeMap<>();
			    for (Variable variable: tableView.getItems()) {
			        map.put(variable.getNameValue(), variable.getValue());
                    
                }
			    logger.info(map.toString());
			    
			    store.getSelectedProcessStarter().start(map);
				applicationEventPublisher.publishEvent(new RefreshEvent());
			}
		});
    	
        addButton.setOnAction(new EventHandler<ActionEvent>() {
             @Override
             public void handle(ActionEvent e) {
                 
                 tableView.getItems().add(
                         new Variable(
                                 nameTextField.getText(), 
                                 getLongFromString(longValueTextField.getText()),
                                 getDoubleFromString(doubleValueTextField.getText()),
                                 textValueTextField.getText()));
                 
                 nameTextField.clear();
                 longValueTextField.clear();
                 doubleValueTextField.clear();
                 textValueTextField.clear();
             }
         });
    }
    
    
    private Long getLongFromString(String s) {
        
        try {
            return Long.valueOf(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double getDoubleFromString(String s) {
        
        try {
            return Double.valueOf(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    
	@EventListener
	private void onNotifyEvent(NotifyEvent notifyEvent) {
		
	    ObservableList<Variable> observableVariableList = FXCollections.observableArrayList();
	    
	    ProcessStarter selectedProcessStarter = store.getSelectedProcessStarter();
	    if (selectedProcessStarter != null) {
	        
		  List<Variable> variableList = store
		      .getSelectedProcessStarter()
		      .getMap()
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
