package org.jboss.demo.fsw.angrytweet.rtgov.epn.situation;

import java.io.Serializable;
import java.util.LinkedList;

import org.overlord.rtgov.activity.model.ActivityType;
import org.overlord.rtgov.activity.model.ActivityUnit;
import org.overlord.rtgov.activity.model.soa.RequestSent;
import org.overlord.rtgov.ep.EventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoutedTicketEventGenerator extends EventProcessor {
    
    private static final Logger LOG = LoggerFactory.getLogger(CEPDroolsEventProcessor.class.getName());

    @SuppressWarnings("unchecked")
    @Override
    public Serializable process(String source, Serializable event, int retriesLeft) throws Exception {
        
        Serializable ret = new LinkedList<Serializable>();
        
        RoutedTicketEvent rte = null;
        
        //we're only interested in RequestSent events to the Router Service
        if (event instanceof ActivityUnit) {
            
            for (ActivityType at : ((ActivityUnit)event).getActivityTypes()) {
                if (at instanceof RequestSent 
                        && ((RequestSent) at).getServiceType().equals("{urn:org.jboss.fsw.demo:switchyard-angrytweet-service:1.0}TicketEnricherProcess/RouterService")) {
                    rte = new RoutedTicketEvent();
                    rte.setId(at.getProperties().get("ticketId"));
                    rte.setAreaCode(at.getProperties().get("areaCode"));
                    rte.setCustomer(at.getProperties().get("customer"));
                    rte.setChannel(at.getProperties().get("channel"));
                    //TODO change to submitted timestamp of original activity
                    rte.setTstamp(at.getTimestamp());
                    LOG.info("Generated RoutedTicketEvent : " + rte.toString());
                    ((LinkedList<Serializable>)ret).add(rte);
                }
            }
        }
        
        if (((LinkedList<Serializable>)ret).size() == 0) {
            ret = null;
        } else if (((LinkedList<Serializable>)ret).size() == 1) {
            ret = ((LinkedList<Serializable>)ret).getFirst();
        }
        
        return ret;
    }

}
