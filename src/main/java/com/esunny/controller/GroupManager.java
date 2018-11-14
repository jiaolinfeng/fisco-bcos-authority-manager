package com.esunny.controller;
  

import java.util.concurrent.ExecutionException;

import org.bcos.contract.source.Group;
import org.bcos.contract.tools.ToolConf;
import org.bcos.web3j.crypto.Credentials;
import org.bcos.web3j.crypto.GenCredential;
import org.bcos.web3j.protocol.Web3j;

import com.esunny.connection.Context;
import com.esunny.util.Storage;

import javafx.event.Event;
import javafx.fxml.FXML;

public class GroupManager {
    
    private static final String GROUP_NUM_KEY = "group_number";
    private static final String GROUP_PREFIX = "group_";
    
    @FXML
    private void initialize() {
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
            // TODO address add group tab
            ++groupIndex;
        }
        
        if (groupIndex < groupNumber)
            storage.put(GROUP_NUM_KEY, "" + groupIndex);
        
    }
    
    @FXML
    private void addGroup(Event event) { 
        Web3j web3 = Context.getInstance().getWeb3();
        ToolConf toolConf = Context.getInstance().getContext().getBean(ToolConf.class);
        Credentials credentials = GenCredential.create(toolConf.getPrivKey());
        
        Group group;
        try {
            group = Group.deploy(web3, credentials, Context.GAS_PRICE, Context.GAS_LIMIT, Context.INIT_WEI).get();
        } catch (InterruptedException | ExecutionException e) { 
            e.printStackTrace();
            return;
        }
        
        

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
    }
}
