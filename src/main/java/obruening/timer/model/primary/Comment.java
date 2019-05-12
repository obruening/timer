package obruening.timer.model.primary;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javafx.beans.property.SimpleStringProperty;

@Entity
@Table(name = "comments")
public class Comment {
    
    public Comment() {}
    public Comment(Post post, String author, String text) {
        this.setPost(post);
        this.setAuthor(author);
        this.setText(text);
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
	    
    @OneToMany(mappedBy="comment", cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
    private List<Rating> ratings;


    private SimpleStringProperty text = new SimpleStringProperty();
    
    private SimpleStringProperty author = new SimpleStringProperty();

    
    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
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

    
    public Post getPost() {
        return post;
    }
    
    public void setPost(Post post) {
        this.post = post;
    }
    
    public List<Rating> getRatings() {
		return ratings;
	}
    
	public void setRatings(List<Rating> ratings) {
		this.ratings = ratings;
	}
}