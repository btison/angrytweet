package org.jboss.demo.fsw.angrytweet.crm.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.jws.WebService;
import javax.xml.ws.Holder;

import org.jboss.demo.fsw.angrytweet.crm.model.Account;
import org.jboss.demo.fsw.angrytweet.crm.model.ContactInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebService(
        portName = "AccountServicePort",
        serviceName = "AccountService",
        wsdlLocation = "WEB-INF/crm.wsdl",
        targetNamespace = "urn:crm:account-service:1.0",
        endpointInterface = "org.jboss.demo.fsw.angrytweet.crm.service.AccountService"
     )
public class AccountServiceImpl implements AccountService {
    
    private static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    
    private static Map<String, Account> byCode;
    private static Map<String, Account> byTwitterId; 
    
    static {
        String configDir = System.getProperty("jboss.server.config.dir");  
        FileReader reader = null;
        try {
            File crm = new File(configDir,"crm.properties");
            reader = new FileReader(crm);
            Properties crmProps = new Properties();
            crmProps.load(reader);
            processProperties(crmProps);
            logger.info("Crm data initialized");
        } catch (Exception e) {
            logger.warn("Crm properties file not found in '" + configDir + "'");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {}
            }
        }
    }

    @Override
    public void getAccountInfo(Account account, 
            Holder<Boolean> result, Holder<String> reason, Holder<List<Account>> accountList) {
        
        Account found = null;
        String code = account.getCode();
        if (code != null && !code.isEmpty()) {
            found = byCode.get(code);
        } else {
            ContactInfo contactInfo = account.getContactInfo();
            if (contactInfo != null) {
                String twitterId = contactInfo.getTwitterId();
                if (twitterId != null && !twitterId.isEmpty()) {
                    found = byTwitterId.get(twitterId);
                }
            }
        }
        if (found == null) {
            result.value = false;
            reason.value = "Account not found";
        } else {
            result.value = true;
            List<Account> accounts = new ArrayList<Account>();
            accounts.add(found);
            accountList.value = accounts;
        }
    }
    
    private static void processProperties(Properties crmProps) {
        byCode = new HashMap<String, Account>();
        byTwitterId = new HashMap<String, Account>();
        for (Object s : crmProps.keySet()) {
            String code = (String) s;
            Account account = new Account();
            account.setCode(code);
            String[] value = crmProps.getProperty(code).split(",");
            ContactInfo contactInfo = new ContactInfo();
            account.setContactInfo(contactInfo);
            contactInfo.setFirstName(value[0]);
            contactInfo.setLastName(value[1]);
            String twitterId = value[2];
            contactInfo.setTwitterId(twitterId);
            contactInfo.setPostalCode(value[3]);
            contactInfo.setEmailAddress(value[4]);
            byCode.put(code, account);
            if (twitterId != null && !twitterId.isEmpty()) {
                byTwitterId.put(twitterId, account);
            }            
        }
    }

}
