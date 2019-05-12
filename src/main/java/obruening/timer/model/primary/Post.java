package obruening.timer.model.primary;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javafx.beans.property.SimpleStringProperty;

@Entity
@Table(name = "posts")
public class Post {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
        
    private SimpleStringProperty text = new SimpleStringProperty();
    
    private SimpleStringProperty author = new SimpleStringProperty();
    
    @OneToMany(mappedBy="post", cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
    private List<Comment> comments = new ArrayList<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    
    // ----
    public SimpleStringProperty text() {
    	return text;
    }
    
    @Access(AccessType.PROPERTY)
    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text.set(text);
    }
    
    
    // ----
    public SimpleStringProperty author() {
    	return author;
    }
    
    @Access(AccessType.PROPERTY)
	public String getAuthor() {
		return author.get();
	}

	public void setAuthor(String author) {
		this.author.set(author);
	}    


    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

}
