package obruening.timer.store.event;

import org.camunda.bpm.engine.repository.ProcessDefinition;

public class ProcessDefinitionSelectedEvent {
    
    private ProcessDefinition processDefinition;

    public ProcessDefinitionSelectedEvent(ProcessDefinition processDefinition) {
        this.setProcessDefinition(processDefinition); 
    }

    public ProcessDefinition getProcessDefinition() {
        return processDefinition;
    }

    public void setProcessDefinition(ProcessDefinition processDefinition) {
        this.processDefinition = processDefinition;
    }

}
