package obruening.timer.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.spring.ProcessEngineFactoryBean;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class WorkflowEngineConfig {
    
    @Autowired
    @Qualifier("primaryDataSource")
    public DataSource dataSource;
    
    @Autowired
    @Qualifier("primaryTransactionManager")
    public JpaTransactionManager transactionManager;
    
    @Autowired
    @Qualifier("primaryEntityManagerFactory")
    public EntityManagerFactory entityManagerFactory;
    
    @Value("classpath:/processes/*.bpmn")
    private Resource[] resources;
    
    @Bean
    SpringProcessEngineConfiguration processEngineConfiguration() {
        final SpringProcessEngineConfiguration config = new SpringProcessEngineConfiguration();
        config.setDataSource(dataSource);
        config.setDatabaseType("h2");
        config.setTransactionManager(transactionManager);
        config.setJobExecutorActivate(true);
        config.setJpaEntityManagerFactory(entityManagerFactory);
        config.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        config.setTransactionsExternallyManaged(true);
        config.setMetricsEnabled(true);
        
        config.setDeploymentResources(resources);
        
        return config;
    }
    
    @Bean
    ProcessEngineFactoryBean processEngineFactoryBean() {
        final ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration(processEngineConfiguration());
        return processEngineFactoryBean;
    }
    
    @Bean
    public RepositoryService repositoryService(ProcessEngineFactoryBean pefb) throws Exception{
        return pefb.getObject().getRepositoryService();
    }
    
    @Bean
    public RuntimeService runtimeService(ProcessEngineFactoryBean pefb) throws Exception {
        return pefb.getObject().getRuntimeService();
    }
 
    @Bean
    public HistoryService historyService(ProcessEngineFactoryBean pefb) throws Exception {
        return pefb.getObject().getHistoryService();
    }
 
    @Bean
    public ManagementService managementService(ProcessEngineFactoryBean pefb) throws Exception {
        return pefb.getObject().getManagementService();
    }
 
    @Bean
    public IdentityService identityService(ProcessEngineFactoryBean pefb) throws Exception {
        return pefb.getObject().getIdentityService();
    }
 
    @Bean
    public TaskService taskService(ProcessEngineFactoryBean pefb) throws Exception {
        return pefb.getObject().getTaskService();
    }

}
