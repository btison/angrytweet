package org.jboss.demo.fsw.angrytweet.service;

import org.jboss.demo.fsw.angrytweet.model.ProviderServiceTicket;

public interface RouterService {
    
    void route(ProviderServiceTicket ticket);

}
