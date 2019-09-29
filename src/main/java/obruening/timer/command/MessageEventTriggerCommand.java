package obruening.timer.command;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import obruening.timer.store.event.workflow.MessageEventTriggerEvent;

@Component
@Order(3)
public class MessageEventTriggerCommand extends BaseCommand {
    
    @Override
    public String getName() {

        return "Message Trigger";
    }

    @Override
    @Transactional("primaryTransactionManager")
    public void execute() {
    	
    	applicationEventPublisher.publishEvent(new MessageEventTriggerEvent());
    }
}
