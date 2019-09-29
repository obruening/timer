package obruening.timer;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


@SpringBootApplication
public class PlaygroundApplication extends Application {
    
    @Bean
    public CountDownLatch closeLatch() {
        return new CountDownLatch(1);
    }
    
    private static final Logger logger = LoggerFactory.getLogger(PlaygroundApplication.class);
    
    private ApplicationContext springContext;
    
    private Parent root;
    
    public static void main(String[] args) throws InterruptedException {
    	

        //SpringApplication.run(PlaygroundApplication.class, args);
        
        launch(PlaygroundApplication.class, args);
        
        /*
        ConfigurableApplicationContext ctx = SpringApplication.run(PlaygroundApplication.class, args);
        
        final CountDownLatch closeLatch = ctx.getBean(CountDownLatch.class);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                closeLatch.countDown();
            }
        });
        closeLatch.await();
        */
        
    }
    
    
    @Bean
    public HostServices createHostService() {
        return this.getHostServices();
    }
    
    @Override
    public void init() throws Exception {

        springContext = SpringApplication.run(PlaygroundApplication.class);
        logger.info(springContext.getClass().getName());
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/border_pane.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        root = fxmlLoader.load();
        logger.info("--> in init");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        primaryStage.setTitle("Timer and SLA Examples");
        
        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
        logger.info("--> in start");
    }
    
    @Override
    public void stop() {
        
        logger.info("stopping ...");

        int exitCode = SpringApplication.exit(springContext, new ExitCodeGenerator() {

            @Override
            public int getExitCode() {

                return 0;
            }
        });

        System.exit(exitCode);
    }
    
    
}
