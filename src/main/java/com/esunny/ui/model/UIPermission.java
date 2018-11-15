package com.esunny.ui.model;

import javafx.beans.property.SimpleStringProperty;

public class UIPermission {

    private SimpleStringProperty address;
    
    private SimpleStringProperty function;

    public UIPermission() {
        this.address = new SimpleStringProperty();
        this.function = new SimpleStringProperty();
    }
    
    public final SimpleStringProperty addressProperty() {
        return this.address;
    }
    
    public final String getAddress() {
        return this.addressProperty().get();
    }
    
    public final void setAddress(final String address) {
        this.addressProperty().set(address);
    }
    
    public final SimpleStringProperty functionProperty() {
        return this.function;
    }
 
    public final String getFunction() {
        return this.functionProperty().get();
    }

    public final void setFunction(final String function) {
        this.functionProperty().set(function);
    }
    
}
