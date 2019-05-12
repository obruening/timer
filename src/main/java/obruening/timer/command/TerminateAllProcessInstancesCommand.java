package obruening.timer.command;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import obruening.timer.store.event.workflow.processinstance.TerminateAllProcessInstancesEvent;

@Component
@Order(100)
public class TerminateAllProcessInstancesCommand extends BaseCommand {
	
    @Override
    public String getName() {

        return "Terminate All Process Instances";
    }

    @Override
    @Transactional("primaryTransactionManager")
    public void execute() {
    	
    	applicationEventPublisher.publishEvent(new TerminateAllProcessInstancesEvent());
    }
}
