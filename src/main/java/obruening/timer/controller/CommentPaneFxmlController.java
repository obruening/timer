package obruening.timer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import obruening.timer.model.primary.Comment;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CommentPaneFxmlController extends Controller {
    
    private static final Logger logger = LoggerFactory.getLogger(CommentPaneFxmlController.class);
	
	private Comment comment;
	private CommentContainerPaneFxmlController parentController;

	@FXML
	private Button removeButton;

	@FXML
	private TextField textField;


	@FXML
	public void initialize() {

		logger.info(this.toString());
		removeButton.setOnAction(event -> parentController.removeChildComponentAndEntity(this, comment));
	}

	public void postInit(CommentContainerPaneFxmlController parentController, Comment comment) {

		this.parentController = parentController;
		this.comment = comment;
		textField.textProperty().bindBidirectional(this.comment.text());
	}
}
