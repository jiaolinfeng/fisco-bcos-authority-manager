package com.esunny.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.bcos.contract.source.Group;
import org.bcos.web3j.abi.datatypes.Address;
import org.bcos.web3j.abi.datatypes.Bool;
import org.bcos.web3j.abi.datatypes.Utf8String;
import org.bcos.web3j.abi.datatypes.generated.Bytes32;
import org.bcos.web3j.crypto.Hash;
import org.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.bcos.web3j.utils.Numeric;

import com.esunny.ui.model.UIPermission;
import com.esunny.ui.util.DialogUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class GroupController {
    
    private Group group;
    
    @FXML
    private Label blackStatusLabel;
    
    @FXML
    private TextField groupAddrField;
    
    @FXML
    private Label groupDescLabel;
    
    @FXML
    private TextField addressField;
    
    @FXML
    private TextField functionField;
    
    @FXML
    private TableView<UIPermission> permissionTable;
    
    private ObservableList<UIPermission> permissionList = FXCollections.observableArrayList();
    
    private boolean blackStatus;
    
    public GroupController(Group group) {
        this.group  = group;
    }
    
    @FXML
    private void initialize() {
        ObservableList<TableColumn<UIPermission, ?>> observableList = permissionTable
                .getColumns();
        observableList.get(0)
                .setCellValueFactory(new PropertyValueFactory<>("address"));
        observableList.get(1)
                .setCellValueFactory(new PropertyValueFactory<>("function")); 

        permissionTable.setItems(permissionList);

        groupAddrField.setText(group.getContractAddress());
        try {
            groupDescLabel.setText(group.getDesc().get().getValue());
            blackStatus = group.getBlack().get().getValue();
            blackStatusLabel.setText(blackStatus ? "开启" : "关闭");
        } catch (InterruptedException | ExecutionException e) { 
            e.printStackTrace();
        }
        
        load();
    }
    
    @FXML
    private void addPermission(Event e) {
        setPermission(true);
    }
    
    @FXML
    private void removePermission(Event e) {
        setPermission(false);
    }
    
    private void setPermission(boolean permission) {
 
        String address = addressField.getText().trim();
        if (address.isEmpty()) { 
            DialogUtils.alert("address is empty");
            return;
        }
        
        String function = functionField.getText().trim();
        if (function.isEmpty()) { 
            DialogUtils.alert("function is empty");
            return;
        }
        
        String functionDesc = "Contract:" + address + ", Function:"+function;
        System.out.println(function);
        System.out.println(Hash.sha3(function).substring(2,10));
        System.out.println(functionDesc);
        try {
            TransactionReceipt receipt = 
            group.setPermission(new Address(Numeric.cleanHexPrefix(address)), 
                    new Utf8String(Numeric.toHexString(Hash.sha3(function.getBytes())).substring(2,10)),
                    new Utf8String(functionDesc), new Bool(permission)).get();
            if (receipt == null) {
                DialogUtils.alert("set permission failed");
                return;
            }
        } catch (InterruptedException | ExecutionException e) { 
            e.printStackTrace();
            DialogUtils.alert("set permission failed");
            return;
        }

        DialogUtils.info("set permission success"); 
        load();
    }
    
    private void load() { 
        permissionList.clear();
        try {
            List<Bytes32> keys = group.getKeys().get().getValue();
            for (Bytes32 key : keys) { 
                String desc = group.getFuncDescwithPermissionByKey(key).get().getValue();
                if (desc.isEmpty()) {
                    System.out.println("" + key + " no permission");
                } else {
                    System.out.println(desc);
                    UIPermission permission = new UIPermission();
                    permission.setFunction(desc);
                    permissionList.add(permission);
                }
            }
        } catch (InterruptedException | ExecutionException e) { 
            e.printStackTrace();
            return;
        } 
    }
    
    
    
    @FXML
    private void enableBlack() {
        setBlackStatus(true);
    }
    
    @FXML
    private void disableBlack() {
        setBlackStatus(false);
    }
    
    private void setBlackStatus(boolean enable) {
        if (enable == blackStatus)
            return;
        try {
            group.setBlack(new Bool(enable)).get();
        } catch (InterruptedException | ExecutionException e) { 
            e.printStackTrace();
            return;
        }
        blackStatus = enable;
        blackStatusLabel.setText(blackStatus ? "开启" : "关闭");
    }
}
