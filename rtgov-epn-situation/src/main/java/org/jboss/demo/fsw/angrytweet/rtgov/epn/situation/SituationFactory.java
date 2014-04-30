package org.jboss.demo.fsw.angrytweet.rtgov.epn.situation;

import org.overlord.rtgov.analytics.situation.Situation;

public class SituationFactory {
    
    private static SituationFactory instance = new SituationFactory();
    
    public static Situation createSituation(String type, RoutedTicketEvent event) {
        return instance.createSituationForType(type, event);
    }
    
    private Situation createSituationForType(String type, RoutedTicketEvent event) {
        AbstractSituationFactory factory = null;
        if ("ServiceAlert".equals(type)) {
            factory = new ServiceSituationFactory();            
        } else if ("CustomerAlert".equals(type)) {
            factory = new CustomerSituationFactory();
        }
        return factory.createSituation(event);
    }

}
