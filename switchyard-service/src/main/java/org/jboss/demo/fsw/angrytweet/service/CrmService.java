package org.jboss.demo.fsw.angrytweet.service;

import org.jboss.demo.fsw.angrytweet.crm.model.GetAccountInfo;
import org.jboss.demo.fsw.angrytweet.crm.model.GetAccountInfoResponse;

public interface CrmService {
    
    GetAccountInfoResponse getAccountInfo(GetAccountInfo getAccountInfo);

}
