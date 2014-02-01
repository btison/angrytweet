package org.jboss.demo.fsw.angrytweet.csv;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.jboss.demo.fsw.angrytweet.model.ProviderServiceTicket;
import org.junit.Assert;
import org.junit.Test;

public class CsvRecordProcessorTest {
    
    @Test
    public void testHandle() throws Exception {
        String record = "'12345';'2014.01.21 12:08:56 CET';'CustomerCode';'tel';'comments';1";
        
        CamelContext ctx = new DefaultCamelContext();
        Exchange exchange = new DefaultExchange(ctx);
        exchange.getIn().setBody(record);
        
        CsvRecordProcessor handler = new CsvRecordProcessor();
        handler.process(exchange);
        
        Object body = exchange.getIn().getBody();
        Assert.assertNotNull(body);
        Assert.assertTrue(body instanceof ProviderServiceTicket);
        ProviderServiceTicket ticket = (ProviderServiceTicket) body;
        Assert.assertEquals("H12345", ticket.getExtId());
        Assert.assertEquals("CustomerCode", ticket.getRequester());
        Assert.assertEquals("tel", ticket.getService());
        Assert.assertEquals("comments", ticket.getComments());
        Assert.assertEquals("Helpdesk", ticket.getChannelIn());
        Assert.assertNotNull(ticket.getSubmitted());
        Assert.assertEquals("Tue Jan 21 12:08:56 CET 2014", ticket.getSubmitted().toString());
    }

}
