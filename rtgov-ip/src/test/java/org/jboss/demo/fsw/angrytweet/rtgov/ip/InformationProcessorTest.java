package org.jboss.demo.fsw.angrytweet.rtgov.ip;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.jboss.demo.fsw.angrytweet.model.ProviderServiceTicket;
import org.junit.Assert;
import org.junit.Test;
import org.overlord.rtgov.activity.model.ActivityType;
import org.overlord.rtgov.activity.model.Context;
import org.overlord.rtgov.activity.processor.InformationProcessor;
import org.overlord.rtgov.activity.util.InformationProcessorUtil;

public class InformationProcessorTest {
    
    @Test
    public void testInformationProcessorLoader() throws Exception {
        List<InformationProcessor> informationProcessors = loadInformationProcessor();
        Assert.assertNotNull(informationProcessors);
        Assert.assertEquals(1, informationProcessors.size());
        
        InformationProcessor informationProcessor = informationProcessors.get(0);
        Assert.assertEquals("AngryTweetIP", informationProcessor.getName());
        Assert.assertEquals("1",informationProcessor.getVersion());
        Assert.assertNotNull(informationProcessor.getTypeProcessors());
        Assert.assertEquals(1,informationProcessor.getTypeProcessors().size());
    }
    
    @Test
    public void testTypeProviderServiceTicket() throws Exception {
        ProviderServiceTicket ticket = new ProviderServiceTicket();
        ticket.setExtId("X123456789");
        ticket.setSubmitted(new Date());
        ticket.setCustomer("customer");
        ticket.setAreaCode("areaCode");
        ticket.setChannelIn("someChannel");
        
        ActivityType activityType = new ActivityType() {
        };
        
        InformationProcessor informationProcessor = loadInformationProcessor().get(0);
        informationProcessor.init();
        informationProcessor.process(ProviderServiceTicket.class.getName(), ticket, null, activityType);
        
        Assert.assertNotNull(activityType.getContext());
        Assert.assertEquals(1, activityType.getContext().size());
        Context context = null;
        for (Context ctx : activityType.getContext()) {
            context = ctx;
            break;
        }
        Assert.assertNotNull(context);
        Assert.assertEquals(Context.Type.Conversation, context.getType());
        Assert.assertEquals("X123456789", context.getValue());
        
        Assert.assertNotNull(activityType.getProperties());
        Assert.assertEquals(5, activityType.getProperties().size());
        
        String customerProperty = activityType.getProperties().get("customer");
        Assert.assertNotNull(customerProperty);
        Assert.assertEquals("customer", customerProperty);
        
        String areaCodeProperty = activityType.getProperties().get("areaCode");
        Assert.assertNotNull(areaCodeProperty);
        Assert.assertEquals("areaCode", areaCodeProperty);
        
        String idProperty = activityType.getProperties().get("ticketId");
        Assert.assertNotNull(idProperty);
        Assert.assertEquals("X123456789", idProperty);
        
        String submittedProperty = activityType.getProperties().get("submitted");
        Assert.assertNotNull(submittedProperty);
        
        String channelProperty = activityType.getProperties().get("channel");
        Assert.assertNotNull(channelProperty);
        Assert.assertEquals("someChannel", channelProperty);
        
    }
    
    @Test
    public void testTypeProviderServiceTicketOptional() throws Exception {
        ProviderServiceTicket ticket = new ProviderServiceTicket();
        ticket.setExtId("X123456789");
        ticket.setSubmitted(new Date());
        
        ActivityType activityType = new ActivityType() {
        };
        
        InformationProcessor informationProcessor = loadInformationProcessor().get(0);
        informationProcessor.init();
        informationProcessor.process(ProviderServiceTicket.class.getName(), ticket, null, activityType);
        
        Assert.assertNotNull(activityType.getContext());
        Assert.assertEquals(1, activityType.getContext().size());
        Context context = null;
        for (Context ctx : activityType.getContext()) {
            context = ctx;
            break;
        }
        Assert.assertNotNull(context);
        Assert.assertEquals(Context.Type.Conversation, context.getType());
        Assert.assertEquals("X123456789", context.getValue());
        
        Assert.assertNotNull(activityType.getProperties());
        Assert.assertEquals(2, activityType.getProperties().size());
        
        String submittedProperty = activityType.getProperties().get("submitted");
        Assert.assertNotNull(submittedProperty);
        
    }
    
    private List<InformationProcessor> loadInformationProcessor() throws Exception {
        InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream("ip.json");
        byte[] b=new byte[is.available()];
        is.read(b);
        is.close();
        List<InformationProcessor> informationProcessors = InformationProcessorUtil.deserializeInformationProcessorList(b);
        return informationProcessors;        
    }

}
