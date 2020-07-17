package com.oocl.workshop.intern.support.util;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class EmailUtil {
    private static final String DELIMITER = ",";

    private static String replaceDelimiter(String addressStr){
        addressStr = addressStr.replaceAll(";",DELIMITER);
        addressStr = addressStr.replaceAll("，",DELIMITER);
        addressStr = addressStr.replaceAll("；",DELIMITER);
        return addressStr;
    }

    public static  InternetAddress[] parseEmailAddresses(String mailTo) throws AddressException {
        if(mailTo==null){
            return null;
        }
        mailTo = replaceDelimiter(mailTo);
        return InternetAddress.parse(mailTo);
    }
}
