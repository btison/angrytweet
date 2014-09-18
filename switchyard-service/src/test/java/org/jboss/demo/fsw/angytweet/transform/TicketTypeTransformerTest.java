package org.jboss.demo.fsw.angytweet.transform;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jboss.demo.fsw.angrytweet.model.ProviderServiceTicket;
import org.jboss.demo.fsw.angrytweet.transform.TicketTypeTransformer;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TicketTypeTransformerTest {
    
    @Test
    public void testTransformFromXML() throws Exception {
        Document doc = loadXML("ticket.xml");
        Assert.assertNotNull(doc);
        Element parent = doc.getDocumentElement();
        
        TicketTypeTransformer transformer = new TicketTypeTransformer();
        
        ProviderServiceTicket ticket = transformer.transformSubmitTicketToProviderServiceTicket(parent);
        
        Assert.assertNotNull(ticket);
        Assert.assertNotNull(ticket.getExtId());
        Assert.assertEquals("code", ticket.getRequester());
        Assert.assertEquals("area", ticket.getAreaCode());
        Assert.assertEquals("tel", ticket.getService());
        Assert.assertEquals("problem", ticket.getComments());
        Assert.assertEquals(1, ticket.getUrgent());
    }
    
    @Test
    public void testTransformFromXMLNoAreaCode() throws Exception {
        Document doc = loadXML("ticket2.xml");
        Assert.assertNotNull(doc);
        Element parent = doc.getDocumentElement();
        
        TicketTypeTransformer transformer = new TicketTypeTransformer();
        
        ProviderServiceTicket ticket = transformer.transformSubmitTicketToProviderServiceTicket(parent);
        
        Assert.assertNotNull(ticket);
        Assert.assertEquals("code", ticket.getRequester());
        Assert.assertNull(ticket.getAreaCode());
        Assert.assertEquals(0, ticket.getUrgent());
        Assert.assertNotNull(ticket.getSubmitted());
        Assert.assertEquals("Portal", ticket.getChannelIn());
    }
    
    private Document loadXML(String path) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(getClass().getClassLoader().getResourceAsStream(path));
    }

}
