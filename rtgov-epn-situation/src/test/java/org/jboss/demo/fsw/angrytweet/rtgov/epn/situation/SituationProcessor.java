package org.jboss.demo.fsw.angrytweet.rtgov.epn.situation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.overlord.rtgov.ep.EventProcessor;

public class SituationProcessor extends EventProcessor {
    
    private List<Serializable> situations = new ArrayList<Serializable>();

    @Override
    public Serializable process(String source, Serializable event, int retriesLeft) throws Exception {
        situations.add(event);
        
        return null;
    }

    public List<Serializable> getSituations() {
        return situations;
    }

}
