package obruening.timer.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateVariableMapping;
import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.variable.VariableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class VariableMapping implements DelegateVariableMapping {
	
	private static Logger logger = LoggerFactory.getLogger(VariableMapping.class);

	@Override
	public void mapInputVariables(DelegateExecution superExecution, VariableMap subVariables) {
		logger.info("in mapInputVariables");
		Long var1Value = (Long)superExecution.getVariable("var1");
		subVariables.put("var2", var1Value * 2);
		//subVariables.forEach((key, value) -> logger.info(key + " " + value));
		
	}

	@Override
	public void mapOutputVariables(DelegateExecution superExecution, VariableScope subInstance) {
		logger.info("in mapOutputVariables");
		
		
	}

}
