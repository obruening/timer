package obruening.timer.command;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import obruening.timer.store.event.RefreshEvent;

@Component
@Order(101)
public class RefreshCommand extends BaseCommand {
	
    @Override
    public String getName() {

        return "Refresh";
    }

    @Override
    @Transactional("primaryTransactionManager")
    public void execute() {
    	applicationEventPublisher.publishEvent(new RefreshEvent());
    }
}
