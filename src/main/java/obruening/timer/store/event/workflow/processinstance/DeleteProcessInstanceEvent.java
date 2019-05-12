package obruening.timer.store.event.workflow.processinstance;

import org.camunda.bpm.engine.history.HistoricProcessInstance;

public class DeleteProcessInstanceEvent {
	
	private HistoricProcessInstance historicProcessInstance;
    
	public DeleteProcessInstanceEvent(HistoricProcessInstance historicProcessInstance) {
        this.historicProcessInstance = historicProcessInstance;
    }
	
    public HistoricProcessInstance getHistoricProcessInstance() {
		return historicProcessInstance;
	}

}
