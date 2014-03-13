package org.jboss.demo.fsw.angrytweet.rest;

import java.util.Date;
import java.util.Random;

import javax.inject.Named;

import org.jboss.demo.fsw.angrytweet.model.ProviderServiceTicket;
import org.jboss.demo.fsw.angrytweet.model.ProviderServices;
import org.switchyard.annotations.Transformer;

@Named("ServiceTicketTransformer")
public class ServiceTicketTransformer {
    
    private Random random = new Random(System.currentTimeMillis());
    
    @Transformer(from="org.jboss.demo.fsw.angrytweet.rest.ServiceTicket")
    public ProviderServiceTicket transform(ServiceTicket from) {
        ProviderServiceTicket ticket = new ProviderServiceTicket();
        ticket.setExtId("M" + Long.toString(Math.abs(random.nextLong())));
        ticket.setRequester(from.getCustomer());
        String servicename = from.getService();
        if (ProviderServices.exists(servicename)) {
            ticket.setService(servicename);
        } else {
            ticket.setService("N.A.");
        }
        ticket.setComments(from.getProblem());
        ticket.setUrgent(Math.max(0, from.getUrgent()));
        ticket.setSubmitted(new Date());
        ticket.setChannelIn("Mobile");
        return ticket;
    }

}
