package org.jboss.demo.fsw.angrytweet.service;

import javax.inject.Inject;

import org.jboss.demo.fsw.angrytweet.crm.model.Account;
import org.jboss.demo.fsw.angrytweet.crm.model.ContactInfo;
import org.jboss.demo.fsw.angrytweet.crm.model.GetAccountInfo;
import org.jboss.demo.fsw.angrytweet.crm.model.GetAccountInfoResponse;
import org.jboss.demo.fsw.angrytweet.model.ProviderServiceTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.switchyard.component.bean.Reference;
import org.switchyard.component.bean.Service;

@Service(TicketEnricher.class)
public class TicketEnricherBean implements TicketEnricher {
    
    private static Logger log = LoggerFactory.getLogger(TicketEnricherBean.class);
    
    @Inject @Reference
    private CrmService crm;
    
    @Inject @Reference
    private StoreService store;
    
    @Inject @Reference
    private ChannelOutService channelOut;
    
    @Inject @Reference
    private RouterService router;

    @Override
    public void enrichTicket(ProviderServiceTicket ticket) {

        GetAccountInfo getAccountInfo = new GetAccountInfo();
        Account account = new Account();
        getAccountInfo.setAccount(account);
        if (ticket.getExtId().startsWith("T")) {
            ContactInfo info = new ContactInfo();
            account.setContactInfo(info);
            info.setTwitterId(ticket.getRequester());
        } else {
            account.setCode(ticket.getRequester());
        }
        
        GetAccountInfoResponse response = crm.getAccountInfo(getAccountInfo);
        if (response.isResult()) {
            Account accountResponse = response.getAccounts().get(0);
            ticket.setAreaCode(accountResponse.getContactInfo().getPostalCode());
            ticket.setCustomer(accountResponse.getCode());
            ticket.setEmail(accountResponse.getContactInfo().getEmailAddress());
        }

        log.info("Ticket after enrichment = " + ticket.toString());
        
        store.storeTicket(ticket);
        
        channelOut.determineChannelOut(ticket);
        
        log.info("Ticket after channelOut rules = " + ticket.toString());
        
        router.route(ticket);

    }

}
