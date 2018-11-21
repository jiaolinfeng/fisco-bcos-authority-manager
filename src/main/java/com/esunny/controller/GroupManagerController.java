package com.esunny.controller;
  
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.bcos.contract.source.ContractAbiMgr;
import org.bcos.contract.source.Group;
import org.bcos.contract.source.SystemProxy;
import org.bcos.contract.source.TransactionFilterChain;
import org.bcos.contract.tools.ToolConf;
import org.bcos.web3j.abi.datatypes.Address;
import org.bcos.web3j.abi.datatypes.DynamicArray;
import org.bcos.web3j.abi.datatypes.Type;
import org.bcos.web3j.abi.datatypes.Utf8String;
import org.bcos.web3j.abi.datatypes.generated.Uint256;
import org.bcos.web3j.crypto.Credentials;
import org.bcos.web3j.crypto.GenCredential;
import org.bcos.web3j.protocol.Web3j;

import com.esunny.connection.Context;
import com.esunny.contract.GroupFactory;
import com.esunny.ui.util.DialogUtils;
import com.esunny.util.Storage;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class GroupManagerController {
     
 
    
    private GroupFactory groupFactory;
    
    private List<Group> groupList = new ArrayList<Group>();
    
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private TabPane groupPane;
    
    @FXML
    private TextField contractNameField;
    
    @FXML
    private TextField contractAddrField;
    
    private ContractAbiMgr contractAbiMgr;
    
    @FXML
    private void initialize() { 
        
        try {
            SystemProxy systemProxy = SystemProxy.load(Context.systemAddr(), 
                    Context.web3(), Context.credentials(), Context.GAS_PRICE, Context.GAS_LIMIT);
            List<Type> contractAbiMgrRoute = systemProxy.getRoute(new Utf8String("ContractAbiMgr")).get();
            contractAbiMgr = ContractAbiMgr.load(contractAbiMgrRoute.get(0).toString(),
                    Context.web3(), Context.credentials(), Context.GAS_PRICE, Context.GAS_LIMIT);

            String contractAddr = contractAbiMgr.getAddr(new Utf8String("GroupFactory")).get().toString();
            System.out.println(contractAddr);
            groupFactory = GroupFactory.load(contractAddr, 
                    Context.web3(), Context.credentials(), Context.GAS_PRICE, Context.GAS_LIMIT);
            System.out.println(groupFactory);
            System.out.println(groupFactory.getAllGroups().get());
            List<Address> groupAddrList = groupFactory.getAllGroups().get().getValue();
            for (Address addr : groupAddrList) {
                addGroup(addr.toString());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            DialogUtils.alert("初始化CNS错误"); 
            System.exit(-1);
        }    
        
    }
    
    @FXML
    private void addGroup(Event event) {  
        String groupAddress;
        try {
            groupFactory.newGroup().get();
            groupAddress = groupFactory.getGroup(new Uint256(groupList.size())).get().toString();
        } catch (InterruptedException | ExecutionException e) { 
            e.printStackTrace();
            return;
        }
        
        addGroup(groupAddress);
    }
    
    private void addGroup(String groupAddress) {
        
        Group group = Group.load(groupAddress, Context.web3(), Context.credentials(), Context.GAS_PRICE, Context.GAS_LIMIT);
        
        groupList.add(group);
        
        addGroupTab(groupList.size() - 1, group);
    }
    
    private void addGroupTab(int groupIndex, Group group) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/esunny/view/group.fxml"));

            GroupController groupController = new GroupController(group);
            loader.setController(groupController);
            Parent node = loader.load();
            
            Tab groupTab = new Tab(" 用户组" + groupIndex + " ");
            groupTab.setContent(node);
            
            groupPane.getTabs().add(groupTab);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
    
    @FXML
    private void getContractAddr(Event event) {
        String contractName = contractNameField.getText().trim();
        if (contractName.isEmpty()) {
            DialogUtils.alert("合约名称为空");
            return;
        }
        
        if (contractAbiMgr == null) {
            DialogUtils.alert("CNS初始化失败");
            return;
        }
        
        String contractAddr;
        try {
            contractAddr = contractAbiMgr.getAddr(new Utf8String(contractName)).get().toString();
        } catch (InterruptedException | ExecutionException e) { 
            e.printStackTrace();
            DialogUtils.alert("CNS查询失败");
            return;
        }

        contractAddrField.setText(contractAddr);
    }
    
    
}
