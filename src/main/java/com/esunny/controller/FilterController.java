package com.esunny.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.bcos.contract.source.AuthorityFilter;
import org.bcos.web3j.abi.datatypes.Address;
import org.bcos.web3j.abi.datatypes.Bool;
import org.bcos.web3j.abi.datatypes.Type;
import org.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.bcos.web3j.utils.Numeric;

import com.esunny.ui.util.DialogUtils;
import com.esunny.util.FormatUtils;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FilterController {

    private AuthorityFilter filter;
    
    @FXML
    private Label nameLabel;
    
    @FXML
    private Label descLabel;
    
    @FXML
    private Label statusLabel;
    
    @FXML
    private TextField accountField;
    
    @FXML
    private TextField groupField;
     
    
    private boolean enabled;
    
    public FilterController(AuthorityFilter filter) {
        this.filter = filter;
    }
    
    @FXML
    private void initialize() {
        try {
            enabled = filter.getEnable().get().getValue();
            statusLabel.setText(enabled ? "开启" : "关闭");
            
            List<Type> filterInfo = filter.getInfo().get();
            if (filterInfo == null || filterInfo.size() < 3)
                return;
            nameLabel.setText((String)filterInfo.get(0).getValue());
            descLabel.setText((String)filterInfo.get(2).getValue());
        } catch (InterruptedException | ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @FXML
    private void enableFilter(Event e) {
        setEnable(true);
    }
    
    @FXML
    private void disableFilter(Event e) {
        setEnable(false);
    }
    
    private void setEnable(boolean enable) {
        if (enable == enabled)
            return;
        try {
            filter.setEnable(new Bool(enable)).get();
        } catch (InterruptedException | ExecutionException e) { 
            e.printStackTrace();
            return;
        }
        enabled = enable;
        statusLabel.setText(enabled ? "开启" : "关闭");
    }
    
    @FXML
    private void setGroup(Event event) {
        String account = accountField.getText().trim();
        if (!FormatUtils.isAddress(account)) { 
            DialogUtils.alert("account format error");
            return;
        }
        
        String groupAddress = groupField.getText().trim();
        if (!FormatUtils.isAddress(groupAddress)) { 
            DialogUtils.alert("address format error");
            return;
        }
         
        
        try {
            TransactionReceipt receipt = filter.setUserToGroup(new Address(Numeric.cleanHexPrefix(account)), 
                    new Address(Numeric.cleanHexPrefix(groupAddress))).get();
            if (receipt == null) {
                DialogUtils.alert("set group failed");
                return;
            }
        } catch (InterruptedException | ExecutionException e) { 
            e.printStackTrace();
            DialogUtils.alert("set group failed");
            return;
        }
        DialogUtils.info("set group success");
    }
    
    @FXML
    private void getGroup(Event event) {
        String account = accountField.getText().trim();
        if (!FormatUtils.isAddress(account)) { 
            DialogUtils.alert("account format error");
            return;
        }
        
        try {
            Address groupAddress = filter.getUserGroup(new Address(Numeric.cleanHexPrefix(account))).get();
            groupField.setText(groupAddress.toString());
        } catch (InterruptedException | ExecutionException e) { 
            e.printStackTrace();
            return;
        }
        
    }
}
