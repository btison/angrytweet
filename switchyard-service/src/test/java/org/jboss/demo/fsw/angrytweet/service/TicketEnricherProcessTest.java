package org.jboss.demo.fsw.angrytweet.service;

import java.util.HashMap;
import java.util.Map;

import org.jboss.demo.fsw.angrytweet.crm.model.Account;
import org.jboss.demo.fsw.angrytweet.crm.model.ContactInfo;
import org.jboss.demo.fsw.angrytweet.crm.model.GetAccountInfo;
import org.jboss.demo.fsw.angrytweet.crm.model.GetAccountInfoResponse;
import org.jboss.demo.fsw.angrytweet.model.ProviderServiceTicket;
import org.jbpm.test.JbpmJUnitBaseTestCase;
import org.jbpm.workflow.instance.impl.WorkflowProcessInstanceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.process.ProcessInstance;

public class TicketEnricherProcessTest extends JbpmJUnitBaseTestCase {
    
    public TicketEnricherProcessTest() {
        super(false,false);
    }
    
    @Test
    public void testProcess() {
        createRuntimeManager("process/TicketEnricherProcess.bpmn");
        RuntimeEngine runtimeEngine = getRuntimeEngine();
        KieSession session = runtimeEngine.getKieSession();
        MockSwitchyardServiceTaskHandler mockWih = new MockSwitchyardServiceTaskHandler();
        session.getWorkItemManager().registerWorkItemHandler("SwitchYard Service Task", mockWih);
        Map<String, Object> params = new HashMap<String, Object>();
        final ProviderServiceTicket ticket = new ProviderServiceTicket();
        ticket.setRequester("requester");
        ticket.setExtId("H123456789");
        params.put("Parameter", ticket);
        
        GetAccountInfoResponse getAccountInfoResponse = new GetAccountInfoResponse();
        getAccountInfoResponse.setResult(true);
        Account account = new Account();
        account.setCode("customer");
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setEmailAddress("customer@email.com");
        contactInfo.setPostalCode("12345");
        account.setContactInfo(contactInfo);
        getAccountInfoResponse.getAccounts().add(account);
        
        mockWih.whenServiceInvokedThenReturn("CrmService", getAccountInfoResponse);  
        
        mockWih.whenServiceInvokedThenInvokeCallback("ChannelOutService", new SwitchYardServiceTaskHandlerCallback() {
            @Override
            public void handle() {
                ticket.setChannelOut("Email");
            }
        });
        
        ProcessInstance processInstance = session.startProcess("org.jboss.demo.fsw.angrytweet.service.TicketEnricher", params);
        
        assertProcessInstanceCompleted(processInstance.getId(), session);
        
        assertProcessVarExists(processInstance, "GetAccountInfo"); 
        assertProcessVarExists(processInstance, "GetAccountInfoResponse");
        Assert.assertEquals(getAccountInfoResponse, ((WorkflowProcessInstanceImpl)processInstance).getVariable("GetAccountInfoResponse"));
        Assert.assertTrue(mockWih.verifyServiceInvoked("CrmService", 1));        
        Assert.assertNotNull(mockWih.getObjectPassedAsParameter("Parameter", "CrmService", 1));
        Assert.assertTrue(mockWih.getObjectPassedAsParameter("Parameter", "CrmService", 1) instanceof GetAccountInfo);
        
        Assert.assertTrue(mockWih.verifyServiceInvoked("StoreService", 1));
        Assert.assertNotNull(mockWih.getObjectPassedAsParameter("Parameter", "StoreService", 1));
        Assert.assertTrue(mockWih.getObjectPassedAsParameter("Parameter", "StoreService", 1) instanceof ProviderServiceTicket);
        Assert.assertEquals(ticket, mockWih.getObjectPassedAsParameter("Parameter", "StoreService", 1));
        
        Assert.assertTrue(mockWih.verifyServiceInvoked("ChannelOutService", 1));
        Assert.assertNotNull(mockWih.getObjectPassedAsParameter("Parameter", "ChannelOutService", 1));
        Assert.assertTrue(mockWih.getObjectPassedAsParameter("Parameter", "ChannelOutService", 1) instanceof ProviderServiceTicket);
        Assert.assertEquals(ticket, mockWih.getObjectPassedAsParameter("Parameter", "ChannelOutService", 1));
        
        Assert.assertTrue(mockWih.verifyServiceInvoked("RouterService", 1));
        Assert.assertNotNull(mockWih.getObjectPassedAsParameter("Parameter", "RouterService", 1));
        Assert.assertTrue(mockWih.getObjectPassedAsParameter("Parameter", "RouterService", 1) instanceof ProviderServiceTicket);
        Assert.assertEquals(ticket, mockWih.getObjectPassedAsParameter("Parameter", "RouterService", 1));
        
        Assert.assertEquals("customer", ticket.getCustomer());
        Assert.assertEquals("12345", ticket.getAreaCode());
        Assert.assertEquals("customer@email.com", ticket.getEmail());
        Assert.assertEquals("Email", ticket.getChannelOut());
        
    }

}
