package obruening.timer.store.event.workflow.task;

import org.camunda.bpm.engine.task.Task;

public class TaskSelectedEvent {

	private Task task;
    
	public TaskSelectedEvent(Task task) {
        this.task = task;
    }
	
    public Task getTask() {
		return task;
	}
}
