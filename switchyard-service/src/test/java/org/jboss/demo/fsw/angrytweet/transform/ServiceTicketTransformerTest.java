package org.jboss.demo.fsw.angrytweet.transform;

import java.util.Date;

import org.jboss.demo.fsw.angrytweet.model.ProviderServiceTicket;
import org.jboss.demo.fsw.angrytweet.model.ProviderServiceTicketErrorEntity;
import org.junit.Assert;
import org.junit.Test;


public final class ServiceTicketTransformerTest {
    
    @Test
    public void testTransformToErrorTicket() throws Exception {
        ProviderServiceTicket ticket = new ProviderServiceTicket();
        ticket.setExtId("P123456789");
        ticket.setRequester("requester");
        ticket.setService("service");
        ticket.setComments("problem description");
        ticket.setUrgent(5);
        ticket.setSubmitted(new Date());
        ticket.setChannelIn("Portal");
        
        ServiceTicketTransformer transformer = new ServiceTicketTransformer();
        
        ProviderServiceTicketErrorEntity result = transformer.transformProviderServiceTicketToErrorEntity(ticket);
        
        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getTicket());
    }


}
