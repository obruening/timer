package obruening.timer.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

public abstract class BaseCommand implements Command {
    
    @Autowired
    protected ApplicationEventPublisher applicationEventPublisher;
}
