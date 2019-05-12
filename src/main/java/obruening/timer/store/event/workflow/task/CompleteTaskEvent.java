package obruening.timer.store.event.workflow.task;

import org.camunda.bpm.engine.task.Task;

public class CompleteTaskEvent {

	private Task task;
    
	public CompleteTaskEvent(Task task) {
        this.task = task;
    }
	
    public Task getTask() {
		return task;
	}
}
