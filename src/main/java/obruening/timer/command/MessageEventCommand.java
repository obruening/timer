package obruening.timer.command;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import obruening.timer.store.event.workflow.MessageEventEvent;

@Component
@Order(3)
public class MessageEventCommand extends BaseCommand {
    
    @Override
    public String getName() {

        return "Message Event";
    }

    @Override
    @Transactional("primaryTransactionManager")
    public void execute() {
    	
    	applicationEventPublisher.publishEvent(new MessageEventEvent());
    }
}
