package obruening.timer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.Metamodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javassist.compiler.ast.Variable;
import obruening.timer.model.primary.berechtigung.Berechtigung;

@Component
public class PlaygroundCommandLineRunner implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(PlaygroundApplication.class);
    
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void run(String... args) throws Exception {
        

        logger.info("Variable: " + isEntity(Variable.class));
        logger.info("Berechtigung: " + isEntity(Berechtigung.class));
        
    }
    
    
    public boolean isEntity(Class<?> clazz){
        Metamodel metamodel = entityManager.getMetamodel();
        try {
            metamodel.entity(clazz);
        } catch (IllegalArgumentException e) {
            // NOTE: the exception means the class is NOT an entity. There is no reason to log it.
            return false;
        }
        return true;
    }


}
