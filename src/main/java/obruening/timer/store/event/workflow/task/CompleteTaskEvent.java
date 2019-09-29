package obruening.timer.store.event.workflow.task;

import java.util.Map;

import org.camunda.bpm.engine.task.Task;

public class CompleteTaskEvent {

	private Task task;
    private Map<String, Object> variableMap;

    public CompleteTaskEvent(Task task, Map<String, Object> variableMap) {
        this.task = task;
        this.variableMap = variableMap;
    }
	
    public Task getTask() {
		return task;
	}
    
    public Map<String, Object> getVariableMap() {
        return variableMap;
    }

}
