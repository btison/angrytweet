package org.jboss.demo.fsw.angrytweet.rtgov.epn.situation;

import org.overlord.rtgov.analytics.situation.Situation;

public interface AbstractSituationFactory {
    
    Situation createSituation(RoutedTicketEvent event);

}
