package org.jboss.demo.fsw.angytweet.transform;

import javax.inject.Named;

import org.jboss.demo.fsw.angrytweet.model.ProviderServiceTicket;
import org.jboss.demo.fsw.angrytweet.model.ProviderServices;
import org.switchyard.annotations.Transformer;

import twitter4j.HashtagEntity;
import twitter4j.Status;

@Named("TwitterTransformer")
public class TwitterTransformer {
    
    @Transformer(from = "java:twitter4j.Status")
    public ProviderServiceTicket transform(Status status) {        
        ProviderServiceTicket ticket = new ProviderServiceTicket();
        ticket.setExtId("T" + Long.toString(status.getId()));
        ticket.setChannelIn("Twitter");
        ticket.setRequester(status.getUser().getScreenName());
        ticket.setSubmitted(status.getCreatedAt());
        ticket.setComments(status.getText());

            //service :take first hashtag from servicelist that is not #urgent
            //urgent : set to 1 if hashtag #urgent is found
            boolean found = false;
            for (HashtagEntity hashTag : status.getHashtagEntities()) {
                String test = hashTag.getText();
                if ("urgent".equalsIgnoreCase(test)) {
                    ticket.setUrgent(1);
                } else {
                    if (!found && ProviderServices.exists(test)) { 
                        ticket.setService(hashTag.getText());
                        found = true;
                    }
                }
            }
        if (ticket.getService() == null) {
            ticket.setService("N.A.");
        }
        return ticket;
    }

}
