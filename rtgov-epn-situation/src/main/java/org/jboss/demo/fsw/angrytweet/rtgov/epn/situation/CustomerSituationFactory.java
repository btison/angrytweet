package org.jboss.demo.fsw.angrytweet.rtgov.epn.situation;

import java.util.Date;

import org.overlord.rtgov.activity.model.Context;
import org.overlord.rtgov.activity.model.Context.Type;
import org.overlord.rtgov.analytics.situation.Situation;
import org.overlord.rtgov.analytics.situation.Situation.Severity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerSituationFactory implements AbstractSituationFactory {
    
    private static final Logger LOG = LoggerFactory.getLogger(CustomerSituationFactory.class);

    @Override
    public Situation createSituation(RoutedTicketEvent event) {
        
        LOG.info("Creating Customer Alert for customer '" + event.getCustomer() + "'" );
        
        Situation situation = new Situation();
        situation.setSubject(Situation.createSubject("Customer", event.getCustomer()));
        situation.setType("Customer Alert");
        situation.setDescription("Customer Alert for customer " + event.getCustomer());
        situation.setSeverity(Severity.High);
        situation.setTimestamp(new Date().getTime());
        Context context = new Context();
        context.setType(Type.Conversation);
        context.setValue(event.getCustomer());
        situation.getContext().add(context);
        situation.getProperties().put("customer", event.getCustomer());
        return situation;
    }

}
