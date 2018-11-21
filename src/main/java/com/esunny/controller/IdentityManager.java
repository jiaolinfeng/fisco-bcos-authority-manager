package com.esunny.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.bcos.contract.source.ContractAbiMgr;
import org.bcos.contract.source.SystemProxy;
import org.bcos.web3j.abi.datatypes.Address;
import org.bcos.web3j.abi.datatypes.Type;
import org.bcos.web3j.abi.datatypes.Utf8String;
import org.bcos.web3j.protocol.core.methods.response.TransactionReceipt;

import com.esunny.connection.Context;
import com.esunny.contract.IdentityMgr;
import com.esunny.ui.model.UIIdentity;
import com.esunny.ui.util.DialogUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class IdentityManager {

    @FXML
    private TableView<UIIdentity> regTable;
    
    @FXML
    private TableView<UIIdentity> validTable;
    
    private ObservableList<UIIdentity> regIdentityList = FXCollections.observableArrayList();
    
    private ObservableList<UIIdentity> validIdentityList = FXCollections.observableArrayList();
    
    private IdentityMgr identityMgr;
    
    @FXML
    private void initialize() {
        System.out.println("identity init");
        initRegTable();
        initIdentityTable();
        initContract();
        loadData();
    }

    private void initContract() {
        try {
            SystemProxy systemProxy = SystemProxy.load(Context.systemAddr(), 
                    Context.web3(), Context.credentials(), Context.GAS_PRICE, Context.GAS_LIMIT);
            List<Type> contractAbiMgrRoute = systemProxy.getRoute(new Utf8String("ContractAbiMgr")).get();
            ContractAbiMgr contractAbiMgr = ContractAbiMgr.load(contractAbiMgrRoute.get(0).toString(),
                    Context.web3(), Context.credentials(), Context.GAS_PRICE, Context.GAS_LIMIT);

            String contractAddr = contractAbiMgr.getAddr(new Utf8String("IdentityMgr")).get().toString();
            System.out.println(contractAddr);
            identityMgr = IdentityMgr.load(contractAddr, 
                    Context.web3(), Context.credentials(), Context.GAS_PRICE, Context.GAS_LIMIT);
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.alert("初始化权限错误");
            System.exit(-1);
        } 
    }
    
    @FXML
    private void refresh() {
        regIdentityList.clear();
        validIdentityList.clear();
        loadData();
    }
    
    private void loadData() {
        try {
            List<Address> regAccounts = identityMgr.getRegAccounts().get().getValue();
            for (Address account : regAccounts) {
                System.out.println("reg: "+ account.toString());
                if (account.getValue().equals(Address.DEFAULT.getValue()))
                    continue;
                List<Type> identityInfo = identityMgr.getIdentityInfo(account).get();
                UIIdentity uiIdentity = new UIIdentity();
                uiIdentity.setAccount(account.toString());
                uiIdentity.setName(identityInfo.get(0).toString());
                uiIdentity.setId(identityInfo.get(1).toString());
                regIdentityList.add(uiIdentity);
            }
            
            List<Address> validAccounts = identityMgr.getValidAccounts().get().getValue();
            for (Address account : validAccounts) {
                System.out.println("valid: "+ account.toString());
                if (account.getValue().equals(Address.DEFAULT.getValue()))
                    continue;
                List<Type> identityInfo = identityMgr.getIdentityInfo(account).get();
                UIIdentity uiIdentity = new UIIdentity();
                uiIdentity.setAccount(account.toString());
                uiIdentity.setName(identityInfo.get(0).toString());
                uiIdentity.setId(identityInfo.get(1).toString());
                validIdentityList.add(uiIdentity);
            }
        } catch (InterruptedException | ExecutionException e) { 
            e.printStackTrace();
        }
    }
    
    private void initIdentityTable() { 
        ObservableList<TableColumn<UIIdentity, ?>> columns = validTable.getColumns();
        columns.get(0).setCellValueFactory(new PropertyValueFactory<>("account")); 
        columns.get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        columns.get(2).setCellValueFactory(new PropertyValueFactory<>("id"));
        validTable.setItems(validIdentityList);
    }
    
    private void initRegTable() {
        Callback<TableColumn<UIIdentity, String>, TableCell<UIIdentity, String>> editCellFactory = new Callback<TableColumn<UIIdentity, String>, TableCell<UIIdentity, String>>() {
            @Override
            public TableCell<UIIdentity, String> call(TableColumn<UIIdentity, String> p) {
                return new EditingCell();
            }
        };
        ObservableList<TableColumn<UIIdentity, ?>> columns = regTable.getColumns();
        TableColumn<UIIdentity, String> accountCol = (TableColumn<UIIdentity, String>) columns.get(0);
        accountCol.setCellValueFactory(new PropertyValueFactory<>("account")); 
        accountCol.setCellFactory(editCellFactory); 
        accountCol.setStyle( "-fx-alignment: CENTER-LEFT;");
        
        columns.get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        columns.get(1).setStyle( "-fx-alignment: CENTER-LEFT;");
        
        columns.get(2).setCellValueFactory(new PropertyValueFactory<>("id"));
        columns.get(2).setStyle( "-fx-alignment: CENTER-LEFT;");
        
        Callback<TableColumn<UIIdentity, String>, TableCell<UIIdentity, String>> operCellFactory = new Callback<TableColumn<UIIdentity, String>, TableCell<UIIdentity, String>>() {
            @Override
            public TableCell<UIIdentity, String> call(final TableColumn<UIIdentity, String> param) {
                TableCell<UIIdentity, String> cell = new OperCell();
                return cell;
            }
        };
        
        @SuppressWarnings("unchecked")
        TableColumn<UIIdentity, String> operationsCol = (TableColumn<UIIdentity, String>) columns.get(3);
        operationsCol.setCellFactory(operCellFactory);
        
        regTable.setItems(regIdentityList); 
    }
    
    
    class EditingCell extends TableCell<UIIdentity, String> {

        private TextField textField;

        public EditingCell() {
        }

        @Override
        public void startEdit() {
            if (!isEmpty()) {
                super.startEdit();
                createTextField();
                setText(null);
                setGraphic(textField);
                textField.selectAll();
            }
        }

        @Override
        public void cancelEdit() {
            super.cancelEdit();

            setText((String) getItem());
            setGraphic(null);
        }

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                if (isEditing()) {
                    if (textField != null) {
                        textField.setText(getString());
                    }
                    setText(null);
                    setGraphic(textField);
                } else {
                    setText(getString());
                    setGraphic(null);
                }
            }
        }

        private void createTextField() {
            textField = new TextField(getString());
            textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
            textField.focusedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                    if (!arg2) {
                        commitEdit(textField.getText());
                    }
                }
            });
        }

        private String getString() {
            return getItem() == null ? "" : getItem().toString();
        }
    }

    class OperCell extends TableCell<UIIdentity, String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(buildOperation());
                setText(" ");
            }
        }

        private HBox buildOperation() {
            HBox operationView = new HBox(10);
            Button agreeButton = new Button("通过");
            agreeButton.setOnMouseClicked(new EventHandler<Event>() {

                @Override
                public void handle(Event event) { 
                    UIIdentity uiIdentity = (UIIdentity) getTableRow().getItem();
                    System.out.println(uiIdentity.getAccount());
                    try {
                        TransactionReceipt receipt = 
                                identityMgr.agreeIdentityInfo(new Address(uiIdentity.getAccount())).get(); 
                    } catch (InterruptedException | ExecutionException e) { 
                        e.printStackTrace();
                    }
                    regIdentityList.remove(getIndex());
                }
            });
            agreeButton.setMaxHeight(20);
            operationView.getChildren().add(agreeButton);
            return operationView;
        }
    }
    
}
