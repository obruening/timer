package obruening.timer.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import com.google.common.graph.SuccessorsFunction;
import com.google.common.graph.Traverser;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import obruening.timer.model.primary.Post;
import obruening.timer.service.primary.PostService;
import obruening.timer.util.Wrapper;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ValidationViewFxmlController extends Controller {
    
    private static final Logger logger = LoggerFactory.getLogger(ValidationViewFxmlController.class);
	
	StringProperty textString = new SimpleStringProperty();
	
	@Autowired
	ResourceBundleMessageSource messageSource;
	
	private List<Wrapper> successorList = new ArrayList<>();
	
	@FXML
	private TextField textTextField;

	@FXML
	private TextField authorTextField;
	
	@FXML
	private Label textErrorLabel;

	@FXML
	private Label authorErrorLabel;
	
	@FXML
	private VBox vBox;
	
	@FXML
	private Button showButton;
	
	@FXML
	private GridPane gridPane;
	
	private Post post = new Post();
	
	@Autowired
	private PostService postService;
	
    @FXML
    public void initialize() throws IOException {
    	
    	logger.info(springContext.getBeanFactory().getBeanDefinition("validationViewFxmlController").getScope());

    	post.setText("das ist mein Text");
    	showButton.setOnAction(event -> showPost());
    	
    	/*
    	authorErrorLabel.setText("");
    	textErrorLabel.setText("");
    	
    	PostValidator postValidator = new PostValidator();
    	BindingResult bindingResult = new BeanPropertyBindingResult(post, "postobject");
    	postValidator.validate(post, bindingResult);
    	
    	FieldError fieldError = bindingResult.getFieldError("text");
    	System.out.println(messageSource.getMessage(fieldError.getCode(), null, Locale.getDefault()));
    	System.out.println(fieldError.getCode());
    	System.out.println(bindingResult.getFieldError("text"));
    	System.out.println(bindingResult.getErrorCount());
    	
    	textTextField.setText(post.getText());
    	authorTextField.setText(post.getAuthor());

    	textTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

				if (newValue == false) {
					System.out.println("textTextField blur");
					String actual = textTextField.getText();
					String errorMessage = "test".equalsIgnoreCase(actual) ? "Fehler" : "";
				    textErrorLabel.setText(errorMessage);
				}
				//System.out.println(String.format("textTextField oldValue %s, newValue %s",  oldValue, newValue));
			}
		});

    	authorTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				
				if (newValue == false) {
					System.out.println("textTextField blur");
				}
				//System.out.println(String.format("authorTextField oldValue %s, newValue %s",  oldValue, newValue));
			}
		});
    	
    	addPanelButton.setOnAction(e -> { addPanel(); });
    	*/
    	addPanel();
    }
    
    private void showPost() {
    	
    	Traverser<Controller> xTraverser = Traverser.forTree(new SuccessorsFunction<Controller>() {

			@Override
			public Iterable<Controller> successors(Controller controller) {
				return controller.getWrapperList().stream().map(w -> w.getController()).collect(Collectors.toList());
			}
		});
    	
    	Iterable<Controller> iterable = xTraverser.depthFirstPostOrder(this);
		for (Controller controller : iterable) {
   	        logger.info(controller.toString());
		}

		/*
		System.out.println(post.getText());
		post.getComments().forEach(c -> System.out.println(c.getText()));
		postService.save(post);
		*/
		
	}

	private void addPanel() {

    	try {
    		
    		Wrapper wrapper = loadFXML("/fxml/post_pane_view.fxml");
    		Parent node = wrapper.getNode();
    		PostPaneFxmlController postPaneFxmlController = (PostPaneFxmlController)wrapper.getController();
    		postPaneFxmlController.postInit(post);
    		
    		wrapperList.add(wrapper);
    		
			vBox.getChildren().add(node);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
    

	public void remove(Controller controller) {
		
		Optional<Wrapper> wrapper = findByController(controller);
		if (wrapper.isPresent()) {
			vBox.getChildren().remove(wrapper.get().getNode());
			wrapperList.remove(wrapper.get());
		}
	}
}
