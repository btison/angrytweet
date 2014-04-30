package org.jboss.demo.fsw.angrytweet.service;

import twitter4j.Status;

public interface TwitterStatusPollingService {
    
    void process(Status status);

}
