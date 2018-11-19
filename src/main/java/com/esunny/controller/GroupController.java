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
import com.esunny.util.FormatUtils;

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
    private Label createStatusLabel;
    
    @FXML
    private TextField groupAddrField;
    
    @FXML
    private TextField groupDescField;
    
    @FXML
    private TextField addressField;
    
    @FXML
    private TextField functionField;
    
    @FXML
    private TableView<UIPermission> permissionTable;
    
    private ObservableList<UIPermission> permissionList = FXCollections.observableArrayList();
    
    private boolean blackStatus;
    
    private boolean createStatus;
    
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
            String groupDesc = group.getDesc().get().getValue().trim();
            if (groupDesc.isEmpty()) {
                groupDesc = "无组描述";
            }
            groupDescField.setText(groupDesc);
            blackStatus = group.getBlack().get().getValue();
            blackStatusLabel.setText(blackStatus ? "开启" : "关闭");
            
            createStatus = group.getCreate().get().getValue();
            createStatusLabel.setText(createStatus ? "开启" : "关闭");
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
    
    @FXML
    private void setGroupDesc() {
        String groupDesc = groupDescField.getText().trim();
        try {
            group.setDesc(new Utf8String(groupDesc)).get();
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void setPermission(boolean permission) {
        
        String address = addressField.getText().trim();
        if (!FormatUtils.isAddress(address)) { 
            DialogUtils.alert("合约地址格式错误");
            return;
        }
        
        String function = functionField.getText().trim();
        if (!FormatUtils.isInterface(function)) { 
            DialogUtils.alert("合约接口格式错误");
            return;
        }
        
        String functionDesc = "{\"contract\":\"" + address + "\", \"function\":\""+function+"\"}";
        System.out.println(functionDesc);
        try {
            TransactionReceipt receipt = 
            group.setPermission(new Address(Numeric.cleanHexPrefix(address)), 
                    new Utf8String(Numeric.toHexString(Hash.sha3(function.getBytes())).substring(2,10)),
                    new Utf8String(functionDesc), new Bool(permission)).get();
            if (receipt == null) {
                DialogUtils.alert("组权限设置失败");
                return;
            }
        } catch (InterruptedException | ExecutionException e) { 
            e.printStackTrace();
            DialogUtils.alert("组权限设置失败");
            return;
        }

        DialogUtils.info("组权限设置成功"); 
        load();
    }
    
    private void load() { 
        permissionList.clear();
        try {
            List<Bytes32> keys = group.getKeys().get().getValue();
            for (Bytes32 key : keys) { 
                String desc = group.getFuncDescwithPermissionByKey(key).get().getValue();
                if (desc.isEmpty()) {
                    System.out.println(Numeric.toHexString(key.getValue()) + " no permission");
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
    
    @FXML
    private void enableCreate() {
        setCreate(true);
    }
    
    @FXML
    private void disableCreate() {
        setCreate(false);
    }
    
    private void setCreate(boolean enable) {
        if (enable == createStatus)
            return;
        try {
            group.setCreate(new Bool(enable)).get();
        } catch (InterruptedException | ExecutionException e) { 
            e.printStackTrace();
            return;
        }
        createStatus = enable;
        createStatusLabel.setText(createStatus ? "开启" : "关闭");
    }
}
