package com.esunny.util;

import java.util.regex.Pattern;

import org.bcos.web3j.utils.Numeric;

public class FormatUtils {

    public static boolean isInterface(String function) {
        if (function.isEmpty())
            return false;
        final String pattern = "^\\w+\\((.*)\\)$";  
        return Pattern.matches(pattern, function);
    }
    
    public static boolean isAddress(String address) {
        if (address.isEmpty())
            return false;
        if (!Numeric.containsHexPrefix(address))
            return false;
        if (address.length() != 42) // address length 0x + 160bits
            return false;
        return true;
    }
}
