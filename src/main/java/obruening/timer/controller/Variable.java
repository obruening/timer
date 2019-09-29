package obruening.timer.controller;

import java.util.Objects;

import org.apache.commons.lang.StringUtils;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Variable {
        
    private SimpleStringProperty nameValue = new SimpleStringProperty();
    
    private SimpleObjectProperty<Double> doubleValue = new SimpleObjectProperty<>();
    
    private SimpleObjectProperty<Long> longValue = new SimpleObjectProperty<>();
    
    private SimpleStringProperty textValue = new SimpleStringProperty();
    
    private SimpleStringProperty text2Value = new SimpleStringProperty();
    
    private Object object;
    
    
    public Variable() {}
    
    public Variable(String name, Long l, Double d, String text) {
        this.setNameValue(name);
        this.setLongValue(l);
        this.setDoubleValue(d);
        this.setTextValue(text);
    }
    
    // nameValue
    public SimpleStringProperty nameValue() {
        return nameValue;
    }

    public String getNameValue() {
        return nameValue.get();
    }

    public void setNameValue(String nameValue) {
        this.nameValue.set(nameValue);
    }
    
    // doubleValue
    public SimpleObjectProperty<Double> doubleValue() {
    	return doubleValue;
    }

    public Double getDoubleValue() {
        return doubleValue.get();
    }

    public void setDoubleValue(Double d) {
        this.doubleValue.set(d);
    }

    // longValue
    public SimpleObjectProperty<Long> longValue() {
        return longValue;
    }

    public Long getLongValue() {
        return longValue.get();
    }

    public void setLongValue(Long l) {
        this.longValue.set(l);
    }

    // textValue
    public SimpleStringProperty textValue() {
        return textValue;
    }

    public String getTextValue() {
        return textValue.get();
    }

    public void setTextValue(String text) {
        this.textValue.set(text);
    }

    // textValue
    public SimpleStringProperty text2Value() {
        return text2Value;
    }

    public String getText2Value() {
        return text2Value.get();
    }

    public void setText2Value(String text2) {
        this.text2Value.set(text2);
    }
        
    public Object getValue() {
        if (getLongValue() != null) {
            return getLongValue();
        } else if (getDoubleValue() != null) {
            return getDoubleValue();
        } else if (Objects.nonNull(object)){
            return object;
        } else if (StringUtils.isNotBlank(getTextValue())) {
            return getTextValue();
        } else {
            return null;
        }
    }
    
    // object
    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
