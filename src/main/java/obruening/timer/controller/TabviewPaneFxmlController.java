package obruening.timer.controller;

import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javafx.application.HostServices;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

@Component
public class TabviewPaneFxmlController extends Controller {
    
    private static final Logger logger = LoggerFactory.getLogger(TabviewPaneFxmlController.class);

    @Autowired
    private HostServices hostServices;
    
    @Autowired
    @Qualifier(value = "primaryH2Url")
    private String h2Url;
    
    @FXML
    private Tab taskTab;
    
    @FXML
    private Tab hiProcinstTab;

    @FXML
    private TabPane tabPane;
    
    @FXML
    private WebView h2WebView;
    
    @FXML
    private Hyperlink h2Hyperlink;

    @FXML
    private WebView helpWebView;
    
    @FXML
    private Tab validationTab;
    
    private static final String HELP = "/html/doc.html";

    @FXML
    public void initialize() throws IOException {
    	
        
        taskTab.setContent(loadFXML("/fxml/task_split_pane.fxml").getNode());
        hiProcinstTab.setContent(loadFXML("/fxml/hi_procinst_split_pane.fxml").getNode());
        validationTab.setContent(loadFXML("/fxml/validation_view.fxml").getNode());
        
        logger.info(h2Url);
        
        WebEngine h2WebEngine = h2WebView.getEngine();
        h2WebEngine.load(h2Url);
        
        URL helpUrl = this.getClass().getResource(HELP);
        WebEngine helpWebEngine = helpWebView.getEngine();
        helpWebEngine.load(helpUrl.toString());

        h2Hyperlink.setOnAction(event -> hostServices.showDocument(h2Url));
      }
}
