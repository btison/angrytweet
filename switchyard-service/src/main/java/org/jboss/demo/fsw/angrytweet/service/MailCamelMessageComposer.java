package org.jboss.demo.fsw.angrytweet.service;

import org.switchyard.Exchange;
import org.switchyard.component.camel.common.composer.CamelBindingData;
import org.switchyard.component.camel.common.composer.CamelMessageComposer;

public class MailCamelMessageComposer extends CamelMessageComposer {

    @Override
    public CamelBindingData decompose(Exchange exchange, CamelBindingData target) throws Exception {
        CamelBindingData data =  super.decompose(exchange, target);
        
        String to = (String)exchange.getContext().getProperty("to").getValue();
        String subject = (String)exchange.getContext().getProperty("subject").getValue();
        
        data.getMessage().setHeader("to",to);
        data.getMessage().setHeader("subject", subject);
        
        return data;
    }
    
    

}
