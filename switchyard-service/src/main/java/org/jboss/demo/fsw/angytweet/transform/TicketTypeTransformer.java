package org.jboss.demo.fsw.angytweet.transform;

import java.util.Date;
import java.util.Random;

import javax.inject.Named;

import org.jboss.demo.fsw.angrytweet.model.ProviderServiceTicket;
import org.jboss.demo.fsw.angrytweet.model.ProviderServices;
import org.switchyard.annotations.Transformer;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@Named("TicketTransformer")
public final class TicketTypeTransformer {
    
    private Random random = new Random(System.currentTimeMillis());

    @Transformer(from = "{urn:switchyard-services:ticketservice:1.0}submitTicket")
    public ProviderServiceTicket transformSubmitTicketToProviderServiceTicket(Element from) {
        
        ProviderServiceTicket ticket = new ProviderServiceTicket();
        ticket.setExtId("P" + Long.toString(Math.abs(random.nextLong())));
        Element customer = (Element)from.getElementsByTagName("customer").item(0);
        String customerCode = getElementValue(customer, "customerCode");
        String areaCode = getElementValue(customer, "areaCode");
        ticket.setRequester(customerCode);
        ticket.setAreaCode(areaCode);
        Element service = (Element)from.getElementsByTagName("service").item(0);
        String servicename = getElementValue(service, "serviceName");
        if (ProviderServices.exists(servicename)) {
            ticket.setService(servicename);
        } else {
            ticket.setService("N.A.");
        }
        String problem = getElementValue(service, "problemDescription");
        ticket.setComments(problem);
        String urgent = getElementValue(service, "urgent");
        ticket.setUrgent(Math.max(0, toInteger(urgent)));
        ticket.setSubmitted(new Date());
        ticket.setChannelIn("Portal");
        return ticket;
    }
    
    private String getElementValue(Element parent, String elementName) {
        String value = null;
        NodeList nodes = parent.getElementsByTagName(elementName);
        if (nodes.getLength() > 0) {
            value = nodes.item(0).getChildNodes().item(0).getNodeValue();
        }
        return value;
    }
    
    private int toInteger(String string) {
        int value = 0;
        if (string == null || string.isEmpty()) {
            return value;
        }
        try {
            value = Integer.parseInt(string);
        } catch (NumberFormatException ignore) {};        
        return value;
    }

}
