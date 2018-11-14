package com.esunny.connection;

import java.io.File;
import java.math.BigInteger;
import java.util.Map;
import java.util.Map.Entry;

import org.bcos.channel.client.Service;
import org.bcos.channel.handler.ChannelConnections;
import org.bcos.web3j.protocol.Web3j;
import org.bcos.web3j.protocol.channel.ChannelEthereumService;
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

    public static BigInteger GAS_PRICE = new BigInteger("99999999999");
    public static BigInteger GAS_LIMIT = new BigInteger("9999999999999");

    private static Context inst = null;

    public static Context getInstance() {
        if (inst == null) {
            inst = new Context();
        }
        return inst;
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
            //Thread.sleep(2000);
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


}
