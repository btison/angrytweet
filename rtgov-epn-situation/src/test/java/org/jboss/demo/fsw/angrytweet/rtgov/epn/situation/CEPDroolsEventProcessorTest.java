package org.jboss.demo.fsw.angrytweet.rtgov.epn.situation;

import java.io.Serializable;

import org.junit.Assert;
import org.junit.Test;
import org.overlord.rtgov.activity.model.Context;
import org.overlord.rtgov.activity.model.Context.Type;
import org.overlord.rtgov.analytics.situation.Situation;
import org.overlord.rtgov.analytics.situation.Situation.Severity;

public class CEPDroolsEventProcessorTest {
    
    @Test
    public void testServiceAlert() throws Exception {
        
        CEPDroolsEventProcessor processor = new CEPDroolsEventProcessor();
        processor.setRuleName("RoutedTicketEvent");
        processor.init();
        
        RoutedTicketEvent e1 = new RoutedTicketEvent();
        e1.setCustomer("customer1");
        e1.setAreaCode("areaCode1");
        e1.setTstamp(1000);
        
        RoutedTicketEvent e2 = new RoutedTicketEvent();
        e2.setCustomer("customer2");
        e2.setAreaCode("areaCode1");
        e2.setTstamp(2000);
        
        Serializable result = processor.process("RoutedTicket", e1, 0);
        Assert.assertNull(result);
        
        result = processor.process("RoutedTicket", e2, 0);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Situation);
        Situation situation = (Situation) result;
        Assert.assertEquals("AreaCode|areaCode1", situation.getSubject());
        Assert.assertEquals("Service Alert", situation.getType());
        Assert.assertEquals("Service Alert for Area Code areaCode1", situation.getDescription());
        Assert.assertEquals(Severity.High, situation.getSeverity());
        Assert.assertNotNull(situation.getContext());
        Assert.assertEquals(1, situation.getContext().size());
        for (Context context : situation.getContext()) {
            Assert.assertEquals(context.getType(), Type.Conversation);
            Assert.assertEquals(context.getValue(), "areaCode1");
        }
        Assert.assertNotNull(situation.getProperties());
        Assert.assertNotNull(situation.getProperties().get("areaCode"));
        Assert.assertEquals("areaCode1", situation.getProperties().get("areaCode"));
    }
    
    @Test
    public void testServiceAlertMultipleAlerts() throws Exception {
        
        CEPDroolsEventProcessor processor = new CEPDroolsEventProcessor();
        processor.setRuleName("RoutedTicketEvent");
        processor.init();
        
        RoutedTicketEvent e1 = new RoutedTicketEvent();
        e1.setCustomer("customer1");
        e1.setAreaCode("areaCode1");
        e1.setTstamp(1000);
        
        RoutedTicketEvent e2 = new RoutedTicketEvent();
        e2.setCustomer("customer2");
        e2.setAreaCode("areaCode1");
        e2.setTstamp(2000);
        
        RoutedTicketEvent e3 = new RoutedTicketEvent();
        e3.setCustomer("customer3");
        e3.setAreaCode("areaCode1");
        e3.setTstamp(3000);
        
        RoutedTicketEvent e4 = new RoutedTicketEvent();
        e4.setCustomer("customer4");
        e4.setAreaCode("areaCode1");
        e4.setTstamp(4000);
        
        RoutedTicketEvent e5 = new RoutedTicketEvent();
        e5.setCustomer("customer5");
        e5.setAreaCode("areaCode1");
        e5.setTstamp(910000);
        
        Serializable result = processor.process("RoutedTicket", e1, 0);
        Assert.assertNull(result);
        
        result = processor.process("RoutedTicket", e2, 0);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Situation);
        Situation situation = (Situation) result;
        Assert.assertEquals("AreaCode|areaCode1", situation.getSubject());
        Assert.assertEquals("Service Alert", situation.getType());
        Assert.assertEquals("Service Alert for Area Code areaCode1", situation.getDescription());
        Assert.assertEquals(Severity.High, situation.getSeverity());
        Assert.assertNotNull(situation.getContext());
        Assert.assertEquals(1, situation.getContext().size());
        for (Context context : situation.getContext()) {
            Assert.assertEquals(context.getType(), Type.Conversation);
            Assert.assertEquals(context.getValue(), "areaCode1");
        }
        Assert.assertNotNull(situation.getProperties());
        Assert.assertNotNull(situation.getProperties().get("areaCode"));
        Assert.assertEquals("areaCode1", situation.getProperties().get("areaCode"));
        
        result = processor.process("RoutedTicket", e3, 0);
        Assert.assertNotNull(result);
        
        result = processor.process("RoutedTicket", e4, 0);
        Assert.assertNotNull(result);
        
        result = processor.process("RoutedTicket", e5, 0);
        Assert.assertNull(result);
    }
    
    @Test
    public void testCustomerAlert() throws Exception {
        
        CEPDroolsEventProcessor processor = new CEPDroolsEventProcessor();
        processor.setRuleName("RoutedTicketEvent");
        processor.init();
        
        RoutedTicketEvent e1 = new RoutedTicketEvent();
        e1.setCustomer("customer1");
        e1.setChannel("channel1");
        e1.setTstamp(1000);
        
        RoutedTicketEvent e2 = new RoutedTicketEvent();
        e2.setCustomer("customer1");
        e2.setChannel("channel2");
        e2.setTstamp(2000);
        
        Serializable result = processor.process("RoutedTicket", e1, 0);
        Assert.assertNull(result);
        
        result = processor.process("RoutedTicket", e2, 0);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof Situation);
        Situation situation = (Situation) result;
        Assert.assertEquals("Customer|customer1", situation.getSubject());
        Assert.assertEquals("Customer Alert", situation.getType());
        Assert.assertEquals("Customer Alert for customer customer1", situation.getDescription());
        Assert.assertEquals(Severity.High, situation.getSeverity());
        Assert.assertNotNull(situation.getContext());
        Assert.assertEquals(1, situation.getContext().size());
        for (Context context : situation.getContext()) {
            Assert.assertEquals(context.getType(), Type.Conversation);
            Assert.assertEquals(context.getValue(), "customer1");
        }
        Assert.assertNotNull(situation.getProperties());
        Assert.assertNotNull(situation.getProperties().get("customer"));
        Assert.assertEquals("customer1", situation.getProperties().get("customer"));
    }

}
