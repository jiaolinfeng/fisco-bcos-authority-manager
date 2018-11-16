package com.esunny.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.bcos.contract.source.AuthorityFilter;
import org.bcos.contract.source.SystemProxy;
import org.bcos.contract.source.TransactionFilterChain;
import org.bcos.contract.tools.ToolConf;
import org.bcos.web3j.abi.datatypes.Address;
import org.bcos.web3j.abi.datatypes.Type;
import org.bcos.web3j.abi.datatypes.Utf8String;
import org.bcos.web3j.abi.datatypes.generated.Uint256;
import org.bcos.web3j.crypto.Credentials;
import org.bcos.web3j.crypto.GenCredential;
import org.bcos.web3j.crypto.Hash;
import org.bcos.web3j.protocol.Web3j;
import org.bcos.web3j.utils.Numeric;

import com.esunny.connection.Context;
import com.esunny.ui.util.DialogUtils;
import com.esunny.util.FormatUtils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;

public class FilterManagerController {

    @FXML
    private TextField accountField;
    
    @FXML
    private TextField addressField;
    
    @FXML
    private TextField functionField;
    
    @FXML
    private TabPane filterPane;
    
    private TransactionFilterChain filterChain;
    
    @FXML
    private void initialize() {
        Web3j web3 = Context.getInstance().getWeb3();
        ToolConf toolConf = Context.getInstance().getContext().getBean(ToolConf.class);
        Credentials credentials = GenCredential.create(toolConf.getPrivKey());
        
        try {
            SystemProxy systemProxy = SystemProxy.load(toolConf.getSystemProxyAddress(), 
                    web3, credentials, Context.GAS_PRICE, Context.GAS_LIMIT);
            List<Type> transactionFilterChainRoute = systemProxy.getRoute(new Utf8String("TransactionFilterChain")).get();    
            filterChain = TransactionFilterChain.load(
                    transactionFilterChainRoute.get(0).toString(), web3, credentials, Context.GAS_PRICE, Context.GAS_LIMIT);
            
            int filterSize = filterChain.getFiltersLength().get().getValue().intValue();
            for (int i = 0; i < filterSize; ++i) {
                String filterAddress = filterChain.getFilter(new Uint256(i)).get().toString();
                
                AuthorityFilter filter = AuthorityFilter.load(filterAddress, web3, credentials, Context.GAS_PRICE, Context.GAS_LIMIT);
                List<Type> filterInfo = filter.getInfo().get();
                System.out.println(filterInfo.get(0) + " " + filterInfo.get(2));
                
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/esunny/view/filter.fxml"));
                FilterController filterController = new FilterController(filter);
                loader.setController(filterController);
                Parent node = loader.load();
                Tab filterTab = new Tab(filterInfo.get(0).toString());
                filterTab.setContent(node);
                
                filterPane.getTabs().add(filterTab);
            }  
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void testPermission() {
        String account = accountField.getText().trim();
        if (!FormatUtils.isAddress(account)) { 
            DialogUtils.alert("账户地址格式错误");
            return;
        }
        
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
        
        try {
            boolean permission = filterChain.process(new Address(Numeric.cleanHexPrefix(account)), Address.DEFAULT, 
                    new Address(Numeric.cleanHexPrefix(address)), 
                    new Utf8String(Numeric.toHexString(Hash.sha3(function.getBytes())).substring(2,10)),
                    new Utf8String("")).get().getValue();
            if (permission) {
                DialogUtils.info("账户" + account + "有接口" + function + "的权限");
            } else {
                DialogUtils.info("账户" + account + "没有接口" + function + "的权限");
            }
        } catch (InterruptedException | ExecutionException e) { 
            e.printStackTrace();
            DialogUtils.alert("测试权限失败");
        }
    }
    
}
