package org.jboss.demo.fsw.angrytweet.service;

import org.jboss.demo.fsw.angrytweet.model.ProviderServiceTicketErrorEntity;

public interface JpaStoreErrorService {
    
    void storeTicket(ProviderServiceTicketErrorEntity ticket);

}
