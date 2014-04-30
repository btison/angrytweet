package org.jboss.demo.fsw.angrytweet.rtgov.epn.situation;

public interface Event {
    
    public long getTstamp();
    
    public void setTstamp(long timeStamp);
    
    public String getId();

}
