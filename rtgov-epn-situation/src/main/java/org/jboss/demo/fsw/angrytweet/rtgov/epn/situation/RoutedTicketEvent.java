package org.jboss.demo.fsw.angrytweet.rtgov.epn.situation;

import java.io.Serializable;
import java.util.Date;

public class RoutedTicketEvent implements Event, Serializable {
    
    private static final long serialVersionUID = 1L;

    private String id;
    
    private String customer;
    
    private String areaCode;
    
    private String channel;
    
    private long tstamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }    

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public long getTstamp() {
        return tstamp;
    }

    public void setTstamp(long timestamp) {
        this.tstamp = timestamp;
    }
    
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("{ ")
          .append("id : ").append(id).append(" , ")
          .append("customer : ").append(customer).append(" , ")
          .append("areaCode : ").append(areaCode).append(" , ")
          .append("channel : ").append(channel).append(" , ")
          .append("timestamp : ").append(new Date(tstamp).toString())
          .append(" }");
        return sb.toString();
    }

}
