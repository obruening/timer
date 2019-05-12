package obruening.timer.controller;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

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
import obruening.timer.store.event.workflow.processinstance.HistoricProcessInstanceSelectedEvent;

@Component
public class HiProcinstTableViewFxmlController extends Controller {
    
    private static final Logger logger = LoggerFactory.getLogger(HiProcinstTableViewFxmlController.class);
    
	@Autowired
	private Store store;
	
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @FXML
    private TableView<HistoricProcessInstance> hiProcinstTableView;

    @FXML
    private TableColumn<HistoricProcessInstance, String> idColumn;

    @FXML
    private TableColumn<HistoricProcessInstance, String> superProcessInstanceIdColumn;

    @FXML
    private TableColumn<HistoricProcessInstance, String> processDefinitionIdColumn;

    @FXML
    private TableColumn<HistoricProcessInstance, Date> startTimeColumn;

    @FXML
    private TableColumn<HistoricProcessInstance, Date> endTimeColumn;
    
    private List<HistoricProcessInstance> historicProcessInstanceList;
    

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<HistoricProcessInstance, String>("id"));
        superProcessInstanceIdColumn.setCellValueFactory(new PropertyValueFactory<HistoricProcessInstance, String>("superProcessInstanceId"));
        processDefinitionIdColumn.setCellValueFactory(new PropertyValueFactory<HistoricProcessInstance, String>("processDefinitionId"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<HistoricProcessInstance, Date>("startTime"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<HistoricProcessInstance, Date>("endTime"));
        
        hiProcinstTableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HistoricProcessInstance>() {

 			@Override
 			public void changed(ObservableValue<? extends HistoricProcessInstance> observable, HistoricProcessInstance oldValue, HistoricProcessInstance newValue) {
 				if (newValue != null) {
 					applicationEventPublisher.publishEvent(new HistoricProcessInstanceSelectedEvent(newValue));
 				}
 			}
 		});

        update();
    }

	@EventListener
	private void onNotifyEvent(NotifyEvent notifyEvent) {
		update();
	}


    
  private void update() {
    	
    	logger.info("in update");

        List<HistoricProcessInstance> newHistoricProcessInstanceList = store.getHistoricProcessInstanceList();
        
        if (newHistoricProcessInstanceList != historicProcessInstanceList) {
        	
        	logger.info("neue historicProcessInstanceList");
        	
        	HistoricProcessInstance selectedItem = hiProcinstTableView.getSelectionModel().getSelectedItem();
        
        	historicProcessInstanceList = newHistoricProcessInstanceList;
            ObservableList<HistoricProcessInstance> observableHistoricProcessInstanceList = FXCollections.observableArrayList(historicProcessInstanceList);
            hiProcinstTableView.setItems(observableHistoricProcessInstanceList);
            
            if (selectedItem != null) {
            	String id = selectedItem.getId();
            	Optional<HistoricProcessInstance> item = historicProcessInstanceList.stream().filter(h -> h.getId().equals(id)).findFirst();
            	if (item.isPresent()) {
                    hiProcinstTableView.refresh();
            		hiProcinstTableView.getSelectionModel().select(item.get());
            	}
            }

        } else {
        	logger.info("historicProcessInstanceList unverändert");
        }
    }
}
