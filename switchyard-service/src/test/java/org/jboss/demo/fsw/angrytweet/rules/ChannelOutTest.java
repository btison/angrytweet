package org.jboss.demo.fsw.angrytweet.rules;

import org.jboss.demo.fsw.angrytweet.model.ProviderServiceTicket;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.internal.io.ResourceFactory;

public class ChannelOutTest {
    
    private static KieContainer kc;
    
    private KieSession session;
    
    @BeforeClass
    public static void setupKContainer() {
        KieServices ks = KieServices.Factory.get();
        KieFileSystem kfs = ks.newKieFileSystem();
        Resource rules = ResourceFactory.newClassPathResource("rules/ChannelOut.drl");
        kfs.write(rules);
        KieBuilder kb = ks.newKieBuilder(kfs).buildAll();
        Assert.assertTrue(kb.getResults().getMessages().isEmpty());
        kc = ks.newKieContainer(ks.getRepository().getDefaultReleaseId());
    }
    
    @Before
    public void setup() {
        session = kc.newKieSession();
    }

    @After
    public void teardown() {
        session.dispose();
        session = null;
    }
    
    @Test
    public void testTwitter() {
        ProviderServiceTicket ticket = new ProviderServiceTicket();
        ticket.setChannelIn("Twitter");
        
        session.insert(ticket);
        session.fireAllRules();
        
        Assert.assertEquals("Twitter", ticket.getChannelOut());
    }
    
    @Test
    public void testHelpdesk() {
        ProviderServiceTicket ticket = new ProviderServiceTicket();
        ticket.setChannelIn("Helpdesk");
        
        session.insert(ticket);
        session.fireAllRules();
        
        Assert.assertEquals("Email", ticket.getChannelOut());
    }
    
    @Test
    public void testPortal() {
        ProviderServiceTicket ticket = new ProviderServiceTicket();
        ticket.setChannelIn("Portal");
        
        session.insert(ticket);
        session.fireAllRules();
        
        Assert.assertEquals("Email", ticket.getChannelOut());
    }
}
