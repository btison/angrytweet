package org.jboss.demo.fsw.angrytweet.rtgov.epn.situation;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.overlord.rtgov.activity.model.ActivityUnit;
import org.overlord.rtgov.activity.model.soa.RequestSent;
import org.overlord.rtgov.epn.EPNManager;
import org.overlord.rtgov.epn.Network;
import org.overlord.rtgov.epn.Node;
import org.overlord.rtgov.epn.embedded.EmbeddedEPNManager;
import org.overlord.rtgov.epn.util.NetworkUtil;

public class EventProcessingNetworkTest {
    
    @Test
    public void testLoadEpn() throws Exception {
        byte[] b = loadNetworkFile("epn.json");
        
        Network network = NetworkUtil.deserialize(b);
        Assert.assertNotNull(network);
    }
    
    @Test
    public void testLoadTestEpn() throws Exception {
        byte[] b = loadNetworkFile("epn-test.json");
        
        Network network = NetworkUtil.deserialize(b);
        Assert.assertNotNull(network);
    }    
    
    @Test
    public void testNetwork() throws Exception {
        EPNManager epnManager = new EmbeddedEPNManager();
        
        Network epn = NetworkUtil.deserialize(loadNetworkFile("epn-test.json"));
        Assert.assertNotNull(epn);
        epnManager.register(epn);
        
        ActivityUnit a1 = new ActivityUnit();
        RequestSent rs1 = new RequestSent();
        rs1.setServiceType("{urn:org.jboss.fsw.demo:switchyard-angrytweet-service:1.0}TicketEnricherProcess/RouterService");
        a1.getActivityTypes().add(rs1);
        rs1.getProperties().put("ticketId", "T1234");
        rs1.getProperties().put("areaCode", "areaCode1");
        rs1.getProperties().put("customer", "customer1");
        rs1.getProperties().put("channel", "channel1");
        rs1.setTimestamp(1000);
        
        ActivityUnit a2 = new ActivityUnit();
        RequestSent rs2 = new RequestSent();
        rs2.setServiceType("{urn:org.jboss.fsw.demo:switchyard-angrytweet-service:1.0}TicketEnricherProcess/RouterService");
        a2.getActivityTypes().add(rs2);
        rs2.getProperties().put("ticketId", "T1235");
        rs2.getProperties().put("areaCode", "areaCode1");
        rs2.getProperties().put("customer", "customer2");
        rs2.getProperties().put("channel", "channel1");
        rs2.setTimestamp(2000);        
        
        List<Serializable> eventList = new ArrayList<Serializable>();
        eventList.add(a1);
        eventList.add(a2);
        
        epnManager.publish("ActivityUnits", eventList);
        Thread.sleep(1000);
        
        Node situationNode = epn.getNode("SituationNode");
        SituationProcessor processor = (SituationProcessor) situationNode.getEventProcessor();
        
        Assert.assertEquals(1, processor.getSituations().size());
        
    }
    
    private byte[] loadNetworkFile(String name) throws Exception {
        InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
        Assert.assertNotNull(is);
        byte[] b=new byte[is.available()];
        is.read(b);
        is.close();
        return b;
    }

}
