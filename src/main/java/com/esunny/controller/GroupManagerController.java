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
import org.bcos.web3j.abi.datatypes.Type;
import org.bcos.web3j.abi.datatypes.Utf8String;
import org.bcos.web3j.crypto.Credentials;
import org.bcos.web3j.crypto.GenCredential;
import org.bcos.web3j.protocol.Web3j;

import com.esunny.connection.Context;
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
    
    private static final String GROUP_NUM_KEY = "group_number";
    private static final String GROUP_PREFIX = "group_";
    
    private Web3j web3;
    private ToolConf toolConf;
    private Credentials credentials;
    
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
        web3 = Context.getInstance().getWeb3();
        toolConf = Context.getInstance().getContext().getBean(ToolConf.class);
        credentials = GenCredential.create(toolConf.getPrivKey());
        
        
        try {
            SystemProxy systemProxy = SystemProxy.load(toolConf.getSystemProxyAddress(), 
                    web3, credentials, Context.GAS_PRICE, Context.GAS_LIMIT);
            List<Type> contractAbiMgrRoute = systemProxy.getRoute(new Utf8String("ContractAbiMgr")).get();
            contractAbiMgr = ContractAbiMgr.load(
                    contractAbiMgrRoute.get(0).toString(), web3, credentials, Context.GAS_PRICE, Context.GAS_LIMIT);
            contractAbiMgr.getAbiCount().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            DialogUtils.alert("初始化CNS错误");
            System.exit(-1);
        }    
        
        Storage storage = Storage.getInstance();
        String groupNumberStr = storage.get(GROUP_NUM_KEY);
        if (groupNumberStr == null) {
            storage.put(GROUP_NUM_KEY, "0");
            return;
        } 
        
        int groupNumber = Integer.parseInt(groupNumberStr); 
        
        int groupIndex = 0;
        while (groupIndex < groupNumber) {
            String groupInfoStr = storage.get(GROUP_PREFIX + groupIndex);
            if (groupInfoStr == null)
                break;
            System.out.println("group "+ groupIndex + ":" + groupInfoStr);
            addGroupTab(groupIndex, groupInfoStr);
            // TODO address add group tab
            ++groupIndex;
        }
        
        if (groupIndex < groupNumber)
            storage.put(GROUP_NUM_KEY, "" + groupIndex);
        
    }
    
    @FXML
    private void addGroup(Event event) {  
        Group group;
        try {
            group = Group.deploy(web3, credentials, Context.GAS_PRICE, Context.GAS_LIMIT, Context.INIT_WEI).get();
        } catch (InterruptedException | ExecutionException e) { 
            e.printStackTrace();
            return;
        }
        
        groupList.add(group);

        int groupIndex = 0;
        Storage storage = Storage.getInstance();
        String groupAddress = group.getContractAddress();
        String groupNumberStr = storage.get(GROUP_NUM_KEY);
        if (groupNumberStr != null) { 
            groupIndex = Integer.parseInt(groupNumberStr);
        }
        storage.put(GROUP_PREFIX + groupIndex, groupAddress);
        storage.put(GROUP_NUM_KEY, "" + (groupIndex + 1));
        
        System.out.println(groupAddress);
        addGroupTab(groupIndex, group);
    }
    
    private String openImportGroupDialog() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/esunny/view/dialog_import_group.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("New Contract");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(anchorPane.getScene().getWindow());
            Scene scene = new Scene(loader.load());
            dialogStage.setScene(scene);
            dialogStage.setResizable(false);

            ImportGroupDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (controller.isConfirmed()) {  
                return controller.getGroupAddress();
            }
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String();
    }
    
    @FXML
    private void importGroup(Event event) {  
        
        String groupAddress = openImportGroupDialog();
        if (groupAddress.isEmpty()) {
            DialogUtils.alert("用户组地址格式不正确");
            return;
        }
        
        Group group;
        try {
            group = Group.load(groupAddress, web3, credentials, Context.GAS_PRICE, Context.GAS_LIMIT);
            group.getDesc().get(); // 只是为了测试地址是否正确
        } catch (InterruptedException | ExecutionException e) { 
            e.printStackTrace();
            return;
        }
        
        groupList.add(group);

        int groupIndex = 0;
        Storage storage = Storage.getInstance(); 
        String groupNumberStr = storage.get(GROUP_NUM_KEY);
        if (groupNumberStr != null) { 
            groupIndex = Integer.parseInt(groupNumberStr);
        }
        storage.put(GROUP_PREFIX + groupIndex, groupAddress);
        storage.put(GROUP_NUM_KEY, "" + (groupIndex + 1));
        
        System.out.println(groupAddress);
        addGroupTab(groupIndex, group);
    }
    
    private void addGroupTab(int groupIndex, String groupAddress) {
        
        Group group = Group.load(groupAddress, web3, credentials, Context.GAS_PRICE, Context.GAS_LIMIT);
        
        groupList.add(group);
        
        addGroupTab(groupIndex, group);
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
            // TODO Auto-generated catch block
            e.printStackTrace();
            DialogUtils.alert("CNS查询失败");
            return;
        }

        contractAddrField.setText(contractAddr);
    }
    
    
}
