package com.esunny.contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import org.bcos.channel.client.TransactionSucCallback;
import org.bcos.web3j.abi.EventEncoder;
import org.bcos.web3j.abi.EventValues;
import org.bcos.web3j.abi.TypeReference;
import org.bcos.web3j.abi.datatypes.Address;
import org.bcos.web3j.abi.datatypes.DynamicArray;
import org.bcos.web3j.abi.datatypes.Event;
import org.bcos.web3j.abi.datatypes.Function;
import org.bcos.web3j.abi.datatypes.Type;
import org.bcos.web3j.abi.datatypes.Utf8String;
import org.bcos.web3j.abi.datatypes.generated.Uint256;
import org.bcos.web3j.crypto.Credentials;
import org.bcos.web3j.protocol.Web3j;
import org.bcos.web3j.protocol.core.DefaultBlockParameter;
import org.bcos.web3j.protocol.core.methods.request.EthFilter;
import org.bcos.web3j.protocol.core.methods.response.Log;
import org.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.bcos.web3j.tx.Contract;
import org.bcos.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * Auto generated code.<br>
 * <strong>Do not modify!</strong><br>
 * Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>, or {@link org.bcos.web3j.codegen.SolidityFunctionWrapperGenerator} to update.
 *
 * <p>Generated with web3j version none.
 */
public final class IdentityMgr extends Contract {
    private static String BINARY = "6060604052341561000c57fe5b5b6111448061001c6000396000f30060606040523615610076576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630ddbe9e2146100785780632bab05ff146101155780634586aaa11461018a5780637c09cd92146101c057806381b764fb146101d2578063c429453a14610310575bfe5b341561008057fe5b610113600480803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509190803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091905050610385565b005b341561011d57fe5b61012561077b565b6040518080602001828103825283818151815260200191508051906020019060200280838360008314610177575b80518252602083111561017757602082019150602081019050602083039250610153565b5050509050019250505060405180910390f35b341561019257fe5b6101be600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050610810565b005b34156101c857fe5b6101d0610b4f565b005b34156101da57fe5b610206600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050610d5a565b60405180806020018060200184815260200183810383528681815181526020019150805190602001908083836000831461025f575b80518252602083111561025f5760208201915060208101905060208303925061023b565b505050905090810190601f16801561028b5780820380516001836020036101000a031916815260200191505b508381038252858181518152602001915080519060200190808383600083146102d3575b8051825260208311156102d3576020820191506020810190506020830392506102af565b505050905090810190601f1680156102ff5780820380516001836020036101000a031916815260200191505b509550505050505060405180910390f35b341561031857fe5b610320610f8a565b6040518080602001828103825283818151815260200191508051906020019060200280838360008314610372575b8051825260208311156103725760208201915060208101905060208303925061034e565b5050509050019250505060405180910390f35b600060806040519081016040528084815260200183815260200143815260200142815250600060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082015181600001908051906020019061040492919061101f565b50602082015181600101908051906020019061042192919061101f565b506040820151816002015560608201518160030155905050600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161561055857600090505b600380549050811015610557573373ffffffffffffffffffffffffffffffffffffffff166003828154811015156104c357fe5b906000526020600020900160005b9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141561054b5760038181548110151561051b57fe5b906000526020600020900160005b6101000a81549073ffffffffffffffffffffffffffffffffffffffff02191690555b5b806001019050610490565b5b6000600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff021916908315150217905550600280548060010182816105c4919061109f565b916000526020600020900160005b33909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550507fddf2f5e633bae702fb3fc769a3c2ae401e04ef7c1e71697179db15d9c697221e33848443604051808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200180602001806020018481526020018381038352868181518152602001915080519060200190808383600083146106c4575b8051825260208311156106c4576020820191506020810190506020830392506106a0565b505050905090810190601f1680156106f05780820380516001836020036101000a031916815260200191505b50838103825285818151815260200191508051906020019080838360008314610738575b80518252602083111561073857602082019150602081019050602083039250610714565b505050905090810190601f1680156107645780820380516001836020036101000a031916815260200191505b50965050505050505060405180910390a15b505050565b6107836110cb565b600280548060200260200160405190810160405280929190818152602001828054801561080557602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190600101908083116107bb575b505050505090505b90565b60006000600091505b6002805490508210156108e0573373ffffffffffffffffffffffffffffffffffffffff1660028381548110151561084c57fe5b906000526020600020900160005b9054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614156108d4576002828154811015156108a457fe5b906000526020600020900160005b6101000a81549073ffffffffffffffffffffffffffffffffffffffff02191690555b5b816001019150610819565b6001600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055506003805480600101828161094c919061109f565b916000526020600020900160005b85909190916101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050600060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002090508273ffffffffffffffffffffffffffffffffffffffff167f9c5f1e950d5e509d743ab1b0fd69ec10060927bbdcb74d0caa2f1fc86fcfdfed82600001836001018460020154604051808060200180602001848152602001838103835286818154600181600116156101000203166002900481526020019150805460018160011615610100020316600290048015610ab55780601f10610a8a57610100808354040283529160200191610ab5565b820191906000526020600020905b815481529060010190602001808311610a9857829003601f168201915b5050838103825285818154600181600116156101000203166002900481526020019150805460018160011615610100020316600290048015610b385780601f10610b0d57610100808354040283529160200191610b38565b820191906000526020600020905b815481529060010190602001808311610b1b57829003601f168201915b50509550505050505060405180910390a25b505050565b6000600160003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515610ba957610d57565b600060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002090503373ffffffffffffffffffffffffffffffffffffffff167f9c5f1e950d5e509d743ab1b0fd69ec10060927bbdcb74d0caa2f1fc86fcfdfed82600001836001018460020154604051808060200180602001848152602001838103835286818154600181600116156101000203166002900481526020019150805460018160011615610100020316600290048015610cc25780601f10610c9757610100808354040283529160200191610cc2565b820191906000526020600020905b815481529060010190602001808311610ca557829003601f168201915b5050838103825285818154600181600116156101000203166002900481526020019150805460018160011615610100020316600290048015610d455780601f10610d1a57610100808354040283529160200191610d45565b820191906000526020600020905b815481529060010190602001808311610d2857829003601f168201915b50509550505050505060405180910390a25b50565b610d626110df565b610d6a6110df565b60006000600160008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff161515610df5576000602060405190810160405280600081525090602060405190810160405280600081525090809050935093509350610f82565b600060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020905080600001816001018260020154828054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610ed85780601f10610ead57610100808354040283529160200191610ed8565b820191906000526020600020905b815481529060010190602001808311610ebb57829003601f168201915b50505050509250818054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610f745780601f10610f4957610100808354040283529160200191610f74565b820191906000526020600020905b815481529060010190602001808311610f5757829003601f168201915b505050505091509350935093505b509193909250565b610f926110cb565b600380548060200260200160405190810160405280929190818152602001828054801561101457602002820191906000526020600020905b8160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019060010190808311610fca575b505050505090505b90565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061106057805160ff191683800117855561108e565b8280016001018555821561108e579182015b8281111561108d578251825591602001919060010190611072565b5b50905061109b91906110f3565b5090565b8154818355818115116110c6578183600052602060002091820191016110c591906110f3565b5b505050565b602060405190810160405280600081525090565b602060405190810160405280600081525090565b61111591905b808211156111115760008160009055506001016110f9565b5090565b905600a165627a7a72305820cd9078c6ce4c9d3c771a5278457843234293689f6fb8c1e50feb3ea4e51de3e50029";

    public static final String ABI = "[{\"constant\":false,\"inputs\":[{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"id\",\"type\":\"string\"}],\"name\":\"setIdentityInfo\",\"outputs\":[],\"payable\":false,\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"getRegAccounts\",\"outputs\":[{\"name\":\"\",\"type\":\"address[]\"}],\"payable\":false,\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"account\",\"type\":\"address\"}],\"name\":\"agreeIdentityInfo\",\"outputs\":[],\"payable\":false,\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"getIdentityProof\",\"outputs\":[],\"payable\":false,\"type\":\"function\"},{\"constant\":true,\"inputs\":[{\"name\":\"account\",\"type\":\"address\"}],\"name\":\"getIdentityInfo\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"string\"},{\"name\":\"\",\"type\":\"uint256\"}],\"payable\":false,\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"getValidAccounts\",\"outputs\":[{\"name\":\"\",\"type\":\"address[]\"}],\"payable\":false,\"type\":\"function\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"name\":\"account\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"name\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"id\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"blocknumber\",\"type\":\"uint256\"}],\"name\":\"Identity\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"account\",\"type\":\"address\"},{\"indexed\":false,\"name\":\"name\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"id\",\"type\":\"string\"},{\"indexed\":false,\"name\":\"blocknumber\",\"type\":\"uint256\"}],\"name\":\"IdentityReg\",\"type\":\"event\"}]";

    private IdentityMgr(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, Boolean isInitByName) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit, isInitByName);
    }

    private IdentityMgr(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, Boolean isInitByName) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit, isInitByName);
    }

    private IdentityMgr(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit, false);
    }

    private IdentityMgr(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit, false);
    }

    public static List<IdentityEventResponse> getIdentityEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Identity", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<IdentityEventResponse> responses = new ArrayList<IdentityEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            IdentityEventResponse typedResponse = new IdentityEventResponse();
            typedResponse.account = (Address) eventValues.getIndexedValues().get(0);
            typedResponse.name = (Utf8String) eventValues.getNonIndexedValues().get(0);
            typedResponse.id = (Utf8String) eventValues.getNonIndexedValues().get(1);
            typedResponse.blocknumber = (Uint256) eventValues.getNonIndexedValues().get(2);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<IdentityEventResponse> identityEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Identity", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, IdentityEventResponse>() {
            @Override
            public IdentityEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                IdentityEventResponse typedResponse = new IdentityEventResponse();
                typedResponse.account = (Address) eventValues.getIndexedValues().get(0);
                typedResponse.name = (Utf8String) eventValues.getNonIndexedValues().get(0);
                typedResponse.id = (Utf8String) eventValues.getNonIndexedValues().get(1);
                typedResponse.blocknumber = (Uint256) eventValues.getNonIndexedValues().get(2);
                return typedResponse;
            }
        });
    }

    public static List<IdentityRegEventResponse> getIdentityRegEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("IdentityReg", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        ArrayList<IdentityRegEventResponse> responses = new ArrayList<IdentityRegEventResponse>(valueList.size());
        for (EventValues eventValues : valueList) {
            IdentityRegEventResponse typedResponse = new IdentityRegEventResponse();
            typedResponse.account = (Address) eventValues.getNonIndexedValues().get(0);
            typedResponse.name = (Utf8String) eventValues.getNonIndexedValues().get(1);
            typedResponse.id = (Utf8String) eventValues.getNonIndexedValues().get(2);
            typedResponse.blocknumber = (Uint256) eventValues.getNonIndexedValues().get(3);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<IdentityRegEventResponse> identityRegEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("IdentityReg", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, IdentityRegEventResponse>() {
            @Override
            public IdentityRegEventResponse call(Log log) {
                EventValues eventValues = extractEventParameters(event, log);
                IdentityRegEventResponse typedResponse = new IdentityRegEventResponse();
                typedResponse.account = (Address) eventValues.getNonIndexedValues().get(0);
                typedResponse.name = (Utf8String) eventValues.getNonIndexedValues().get(1);
                typedResponse.id = (Utf8String) eventValues.getNonIndexedValues().get(2);
                typedResponse.blocknumber = (Uint256) eventValues.getNonIndexedValues().get(3);
                return typedResponse;
            }
        });
    }

    public Future<TransactionReceipt> setIdentityInfo(Utf8String name, Utf8String id) {
        Function function = new Function("setIdentityInfo", Arrays.<Type>asList(name, id), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public void setIdentityInfo(Utf8String name, Utf8String id, TransactionSucCallback callback) {
        Function function = new Function("setIdentityInfo", Arrays.<Type>asList(name, id), Collections.<TypeReference<?>>emptyList());
        executeTransactionAsync(function, callback);
    }

    public Future<DynamicArray<Address>> getRegAccounts() {
        Function function = new Function("getRegAccounts", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public Future<TransactionReceipt> agreeIdentityInfo(Address account) {
        Function function = new Function("agreeIdentityInfo", Arrays.<Type>asList(account), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public void agreeIdentityInfo(Address account, TransactionSucCallback callback) {
        Function function = new Function("agreeIdentityInfo", Arrays.<Type>asList(account), Collections.<TypeReference<?>>emptyList());
        executeTransactionAsync(function, callback);
    }

    public Future<TransactionReceipt> getIdentityProof() {
        Function function = new Function("getIdentityProof", Arrays.<Type>asList(), Collections.<TypeReference<?>>emptyList());
        return executeTransactionAsync(function);
    }

    public void getIdentityProof(TransactionSucCallback callback) {
        Function function = new Function("getIdentityProof", Arrays.<Type>asList(), Collections.<TypeReference<?>>emptyList());
        executeTransactionAsync(function, callback);
    }

    public Future<List<Type>> getIdentityInfo(Address account) {
        Function function = new Function("getIdentityInfo", 
                Arrays.<Type>asList(account), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        return executeCallMultipleValueReturnAsync(function);
    }

    public Future<DynamicArray<Address>> getValidAccounts() {
        Function function = new Function("getValidAccounts", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Address>>() {}));
        return executeCallSingleValueReturnAsync(function);
    }

    public static Future<IdentityMgr> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue) {
        return deployAsync(IdentityMgr.class, web3j, credentials, gasPrice, gasLimit, BINARY, "", initialWeiValue);
    }

    public static Future<IdentityMgr> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue) {
        return deployAsync(IdentityMgr.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "", initialWeiValue);
    }

    public static IdentityMgr load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new IdentityMgr(contractAddress, web3j, credentials, gasPrice, gasLimit, false);
    }

    public static IdentityMgr load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new IdentityMgr(contractAddress, web3j, transactionManager, gasPrice, gasLimit, false);
    }

    public static IdentityMgr loadByName(String contractName, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new IdentityMgr(contractName, web3j, credentials, gasPrice, gasLimit, true);
    }

    public static IdentityMgr loadByName(String contractName, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new IdentityMgr(contractName, web3j, transactionManager, gasPrice, gasLimit, true);
    }

    public static class IdentityEventResponse {
        public Address account;

        public Utf8String name;

        public Utf8String id;

        public Uint256 blocknumber;
    }

    public static class IdentityRegEventResponse {
        public Address account;

        public Utf8String name;

        public Utf8String id;

        public Uint256 blocknumber;
    }
}
