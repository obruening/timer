package obruening.timer.store.event.workflow.processinstance;

import org.camunda.bpm.engine.history.HistoricProcessInstance;

public class HistoricProcessInstanceSelectedEvent {

	private HistoricProcessInstance historicProcessInstance;
    
	public HistoricProcessInstanceSelectedEvent(HistoricProcessInstance historicProcessInstance) {
        this.historicProcessInstance = historicProcessInstance;
    }
	
    public HistoricProcessInstance getHistoricProcessInstance() {
		return historicProcessInstance;
	}
}
