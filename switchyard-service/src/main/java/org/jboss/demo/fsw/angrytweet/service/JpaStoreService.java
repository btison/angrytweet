package org.jboss.demo.fsw.angrytweet.service;

import org.jboss.demo.fsw.angrytweet.model.ProviderServiceTicketEntity;

public interface JpaStoreService {
    
    void storeTicket(ProviderServiceTicketEntity ticket);

}
