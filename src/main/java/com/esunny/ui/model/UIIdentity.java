package com.esunny.ui.model;

import javafx.beans.property.SimpleStringProperty;

public class UIIdentity {
    private SimpleStringProperty account;
    private SimpleStringProperty name;
    private SimpleStringProperty id;
    
    public UIIdentity() {
        this.account = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.id = new SimpleStringProperty();
    }
    
    public final SimpleStringProperty accountProperty() {
        return this.account;
    }
    
    public final String getAccount() {
        return this.accountProperty().get();
    }
    
    public final void setAccount(final String account) {
        this.accountProperty().set(account);
    }
    
    public final SimpleStringProperty nameProperty() {
        return this.name;
    }
    
    public final String getName() {
        return this.nameProperty().get();
    }
    
    public final void setName(final String name) {
        this.nameProperty().set(name);
    }
    
    public final SimpleStringProperty idProperty() {
        return this.id;
    }
    
    public final String getId() {
        return this.idProperty().get();
    }
    
    public final void setId(final String id) {
        this.idProperty().set(id);
    }
}
