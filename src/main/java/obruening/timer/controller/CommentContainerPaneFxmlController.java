package obruening.timer.controller;

import java.io.IOException;
import java.util.Optional;

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
import obruening.timer.model.primary.Comment;
import obruening.timer.model.primary.Post;
import obruening.timer.util.Wrapper;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CommentContainerPaneFxmlController extends Controller {
    
    private static final Logger logger = LoggerFactory.getLogger(CommentContainerPaneFxmlController.class);
	
	private Post post;

	@FXML
	private Button addButton;

	@FXML
	private TextField textField;
	
	@FXML
	private VBox commentListContainer;

	@FXML
	public void initialize() {

		logger.info(this.toString());
		this.addButton.setOnAction(event -> addComment());
	}

	public void postInit(Post post) {
		this.post = post;
	}

	private void addComment() {
		try {

			Wrapper wrapper = loadFXML("/fxml/comment_pane_view.fxml");
			Parent node = wrapper.getNode();
			CommentPaneFxmlController commentPaneFxmlController = (CommentPaneFxmlController) wrapper.getController();
			
			Comment comment = new Comment(post, "olli", "das ist ein kommentar");
			post.getComments().add(comment);
			commentPaneFxmlController.postInit(this, comment);

			wrapperList.add(wrapper);
			commentListContainer.getChildren().add(node);
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void removeChildComponentAndEntity(CommentPaneFxmlController controller, Comment comment) {
		Optional<Wrapper> wrapper = findByController(controller);
		if (wrapper.isPresent()) {
			commentListContainer.getChildren().remove(wrapper.get().getNode());
			wrapperList.remove(wrapper.get());
			post.getComments().remove(comment);
		}
	}
}
