package obruening.timer.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class SetVariablesServiceTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        
        execution.setVariable("globalVariable", 111L);
        execution.setVariableLocal("localVariable", 222L);
    }

}
