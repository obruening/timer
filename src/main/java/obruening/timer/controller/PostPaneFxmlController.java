package obruening.timer.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import obruening.timer.model.primary.Post;
import obruening.timer.util.Wrapper;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class PostPaneFxmlController extends Controller {
    
    private static final Logger logger = LoggerFactory.getLogger(PostPaneFxmlController.class);
	
	private Post post;

	@FXML
	private Button removeButton;

	@FXML
	private Button addButton;

	@FXML
	private TextField textField;

	@FXML
	private VBox postContainer;

	@FXML
	public void initialize() throws IOException {

		logger.info(this.toString());
	}

	public void postInit(Post post) {

		this.post = post;
		textField.textProperty().bindBidirectional(this.post.text());
		addCommentContainer();
	}
	
	private void addCommentContainer() {
		try {

			Wrapper wrapper = loadFXML("/fxml/comment_container_pane_view.fxml");
			Parent node = wrapper.getNode();
			CommentContainerPaneFxmlController commentContainerPaneFxmlController = (CommentContainerPaneFxmlController) wrapper.getController();
			
			commentContainerPaneFxmlController.postInit(post);
			wrapperList.add(wrapper);
			postContainer.getChildren().add(node);
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
