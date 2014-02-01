package org.jboss.demo.fsw.angytweet.transform;

import java.util.concurrent.LinkedBlockingQueue;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jboss.demo.fsw.angrytweet.model.ProviderServiceTicket;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.switchyard.Exchange;
import org.switchyard.component.test.mixins.cdi.CDIMixIn;
import org.switchyard.test.Invoker;
import org.switchyard.test.MockHandler;
import org.switchyard.test.ServiceOperation;
import org.switchyard.test.SwitchYardRunner;
import org.switchyard.test.SwitchYardTestCaseConfig;
import org.switchyard.test.SwitchYardTestKit;
import org.w3c.dom.Document;

@RunWith(SwitchYardRunner.class)
@SwitchYardTestCaseConfig(mixins = CDIMixIn.class)
public class TicketTypeTransformationTest {
    
    @ServiceOperation("TicketEnricher.enrichTicket")
    private Invoker enrichTicket;  
    
    private SwitchYardTestKit testKit;
    
    @Test
    public void testTransformXMLtoJava() throws Exception {
        
        MockHandler ticketEnricherMock = testKit.replaceService("TicketEnricher");
        
        enrichTicket
            .inputType(QName.valueOf("{urn:switchyard-services:ticketservice:1.0}submitTicket"))
            .sendInOnly(loadXML("ticket3.xml").getDocumentElement());
        
        LinkedBlockingQueue<Exchange> receivedMessages = ticketEnricherMock.getMessages();
        Assert.assertNotNull(receivedMessages);
        Assert.assertEquals(1, receivedMessages.size()); 
        Exchange exchange  = receivedMessages.iterator().next();
        Object content = exchange.getMessage().getContent();
        Assert.assertTrue(content instanceof ProviderServiceTicket);
        ProviderServiceTicket ticket = (ProviderServiceTicket) content;
        Assert.assertEquals("code", ticket.getRequester());
        Assert.assertEquals("tel", ticket.getService());
        Assert.assertEquals("problem", ticket.getComments());
    }
    
    private Document loadXML(String path) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(getClass().getClassLoader().getResourceAsStream(path));
    }

}
