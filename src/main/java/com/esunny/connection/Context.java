package com.esunny.connection;

import java.io.File;
import java.math.BigInteger;
import java.util.Map;
import java.util.Map.Entry;

import org.bcos.channel.client.Service;
import org.bcos.channel.handler.ChannelConnections;
import org.bcos.contract.tools.ToolConf;
import org.bcos.web3j.crypto.Credentials;
import org.bcos.web3j.crypto.GenCredential;
import org.bcos.web3j.protocol.Web3j;
import org.bcos.web3j.protocol.channel.ChannelEthereumService;
import org.bcos.web3j.protocol.core.methods.response.Log;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Context {

    private Context() {

    }

    private boolean isInit = false;
    private ApplicationContext context = null;
    private Service service = null;
    private Web3j web3 = null;
    private Credentials credentials = null;
    private String systemAddr = null;

    public static final BigInteger GAS_PRICE = new BigInteger("99999999999");
    public static final BigInteger GAS_LIMIT = new BigInteger("9999999999999");
    public static final BigInteger INIT_WEI = new BigInteger("0");

    private static Context inst = null;

    public static synchronized Context getInstance() {
        if (inst == null) {
            inst = new Context();
        }
        return inst;
    }

    public static Web3j web3() {
        return getInstance().getWeb3();
    }
    
    public static Credentials credentials() {
        return getInstance().getCredentials();
    }
    
    public static String systemAddr() {
        return getInstance().getSystemAddr();
    }
    
    public boolean init() {
        if (isInit) {
            return true;
        }

        try {
            File externConfigDir = new File(System.getProperty("user.dir") + "\\config");
            if (externConfigDir.exists()) {
                FileSystemXmlApplicationContext fileContext = new FileSystemXmlApplicationContext(
                        externConfigDir.getPath() + "\\applicationContext.xml");
                context = fileContext;
                service = context.getBean(Service.class);
                Map<String, ChannelConnections> connectionInfoMap = service.getAllChannelConnections();
                for (Entry<String, ChannelConnections> pair : connectionInfoMap.entrySet()) {
                    ChannelConnections channelConnection = pair.getValue();
                    channelConnection.setCaCertPath(
                            new File(externConfigDir.getPath() + "\\" + channelConnection.getCaCertPath()).toURI()
                                    .toString());
                    channelConnection.setClientKeystorePath(
                            new File(externConfigDir.getPath() + "\\" + channelConnection.getClientKeystorePath())
                                    .toURI().toString());
                }
            } else { 
                context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
                service = context.getBean(Service.class);
            }

            service.run();

            ChannelEthereumService channelEthereumService = new ChannelEthereumService();
            channelEthereumService.setChannelService(service);
            channelEthereumService.setTimeout(10000);
            web3 = Web3j.build(channelEthereumService);
            ToolConf toolConf = context.getBean(ToolConf.class);
            credentials = GenCredential.create(toolConf.getPrivKey());
            systemAddr = toolConf.getSystemProxyAddress();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("connect failed");
            return false;
        }

        isInit = true;
        return true;
    }

    public ApplicationContext getContext() {
        if (!isInit)
            return null;
        return context;
    }

    public Service getService() {
        if (!isInit)
            return null;
        return service;
    }

    public Web3j getWeb3() {
        if (!isInit)
            return null;
        return web3;
    }

    public boolean isReady() {
        return isInit;
    }

    public Credentials getCredentials() {
        if (!isInit)
            return null;
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public String getSystemAddr() {
        return systemAddr;
    }

    public void setSystemAddr(String systemAddr) {
        this.systemAddr = systemAddr;
    }


}
