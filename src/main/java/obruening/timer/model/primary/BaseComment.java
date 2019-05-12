package obruening.timer.model.primary;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.MappedSuperclass;

import javafx.beans.property.SimpleStringProperty;

@MappedSuperclass
public class BaseComment {

    protected SimpleStringProperty text = new SimpleStringProperty();
    
    protected SimpleStringProperty author = new SimpleStringProperty();
    
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
}