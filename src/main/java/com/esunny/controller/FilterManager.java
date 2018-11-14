package com.esunny.controller;

import java.util.List;

import org.bcos.contract.source.AuthorityFilter;
import org.bcos.contract.source.SystemProxy;
import org.bcos.contract.source.TransactionFilterChain;
import org.bcos.contract.tools.ToolConf;
import org.bcos.web3j.abi.datatypes.Type;
import org.bcos.web3j.abi.datatypes.Utf8String;
import org.bcos.web3j.abi.datatypes.generated.Uint256;
import org.bcos.web3j.crypto.Credentials;
import org.bcos.web3j.crypto.GenCredential;
import org.bcos.web3j.protocol.Web3j;

import com.esunny.connection.Context;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class FilterManager {
    
    @FXML
    private TabPane filterPane;
    
    @FXML
    private void initialize() {
        Web3j web3 = Context.getInstance().getWeb3();
        ToolConf toolConf = Context.getInstance().getContext().getBean(ToolConf.class);
        Credentials credentials = GenCredential.create(toolConf.getPrivKey());
        
        try {
            SystemProxy systemProxy = SystemProxy.load(toolConf.getSystemProxyAddress(), 
                    web3, credentials, Context.GAS_PRICE, Context.GAS_LIMIT);
            List<Type> transactionFilterChainRoute = systemProxy.getRoute(new Utf8String("TransactionFilterChain")).get();    
            TransactionFilterChain transactionFilterChain = TransactionFilterChain.load(
                    transactionFilterChainRoute.get(0).toString(), web3, credentials, Context.GAS_PRICE, Context.GAS_LIMIT);
            int filterSize = transactionFilterChain.getFiltersLength().get().getValue().intValue();
            for (int i = 0; i < filterSize; ++i) {
                String filterAddress = transactionFilterChain.getFilter(new Uint256(i)).get().toString();
                AuthorityFilter filter = AuthorityFilter.load(filterAddress, web3, credentials, Context.GAS_PRICE, Context.GAS_LIMIT);
                List<Type> filterInfo = filter.getInfo().get();
                System.out.println(filterInfo.get(0) + " " + filterInfo.get(2));
                Tab filterTab = new Tab(filterInfo.get(0).toString());
                filterPane.getTabs().add(filterTab);
            }  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
