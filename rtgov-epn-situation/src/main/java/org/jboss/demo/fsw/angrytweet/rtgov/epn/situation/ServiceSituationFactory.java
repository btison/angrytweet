package org.jboss.demo.fsw.angrytweet.rtgov.epn.situation;

import java.util.Date;

import org.overlord.rtgov.activity.model.Context;
import org.overlord.rtgov.activity.model.Context.Type;
import org.overlord.rtgov.analytics.situation.Situation;
import org.overlord.rtgov.analytics.situation.Situation.Severity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceSituationFactory implements AbstractSituationFactory {
    
    private static final Logger LOG = LoggerFactory.getLogger(ServiceSituationFactory.class);

    @Override
    public Situation createSituation(RoutedTicketEvent event) {
        
        LOG.info("Creating Service Alert for area code '" + event.getAreaCode() + "'" );
        
        Situation situation = new Situation();
        situation.setSubject(Situation.createSubject("AreaCode", event.getAreaCode()));
        situation.setType("Service Alert");
        situation.setDescription("Service Alert for Area Code " + event.getAreaCode());
        situation.setSeverity(Severity.High);
        situation.setTimestamp(new Date().getTime());
        Context context = new Context();
        context.setType(Type.Conversation);
        context.setValue(event.getAreaCode());
        situation.getContext().add(context);
        situation.getProperties().put("areaCode", event.getAreaCode());
        return situation;
    }
    
}
