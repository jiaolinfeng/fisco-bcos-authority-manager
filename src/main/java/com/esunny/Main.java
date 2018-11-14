package com.esunny;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.bcos.contract.source.SystemProxy;
import org.bcos.contract.source.TransactionFilterChain;
import org.bcos.contract.tools.ToolConf;
import org.bcos.web3j.abi.datatypes.Address;
import org.bcos.web3j.abi.datatypes.Type;
import org.bcos.web3j.abi.datatypes.Utf8String;
import org.bcos.web3j.abi.datatypes.generated.Uint256;
import org.bcos.web3j.crypto.Credentials;
import org.bcos.web3j.crypto.GenCredential;
import org.bcos.web3j.protocol.Web3j;

import com.esunny.connection.Context;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    
    public static void main(String args[]) throws InterruptedException, ExecutionException {

        System.out.println("init context...");
        boolean isReady = Context.getInstance().init();
        System.out.println("init " + isReady);
       
        /*new Thread(new Runnable() {
            
            @Override
            public void run() {
                System.out.println("init context...");
                boolean isReady = Context.getInstance().init();
                System.out.println("init " + isReady);
            }
            
        }).start();
        */
        launch(args);
        
        System.exit(0);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
 
        Parent root = FXMLLoader.load(getClass().getResource("/com/esunny/view/main.fxml"));
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("权限管理工具 - FISCO BCOS");
        primaryStage.show();
    }
    
}
