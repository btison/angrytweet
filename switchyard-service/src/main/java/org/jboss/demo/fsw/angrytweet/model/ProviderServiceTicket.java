package org.jboss.demo.fsw.angrytweet.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name="ticket")
@SequenceGenerator(name="ticketIdSeq", sequenceName="TICKET_INSTANCE_INFO_ID_SEQ")
public class ProviderServiceTicket {
    
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator="ticketIdSeq")
    private Long id;
    
    @Column(name="external_id")
    private String extId;
    
    @Column(name="service")
    private String service;
    
    @Column(name="requester")
    private String requester;
    
    @Column(name="customer")
    private String customer;
    
    @Column(name="area_code")
    private String areaCode;
    
    @Column(name="comments")
    private String comments;
    
    @Column(name="channel_in")
    private String channelIn;
    
    @Column(name="submitted")
    @Temporal(TemporalType.TIMESTAMP)
    private Date submitted;
    
    @Transient
    private String channelOut;
    
    @Transient
    private String email;
    
    @Column(name="urgent")
    private int urgent;
    
    public long getId() {
        return id;
    }
    
    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getChannelIn() {
        return channelIn;
    }

    public void setChannelIn(String channelIn) {
        this.channelIn = channelIn;
    }

    public int getUrgent() {
        return urgent;
    }

    public void setUrgent(int urgent) {
        this.urgent = urgent;
    }

    public Date getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Date submitted) {
        this.submitted = submitted;
    }
    
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
    
    public String getChannelOut() {
        return channelOut;
    }

    public void setChannelOut(String channelOut) {
        this.channelOut = channelOut;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append("{")
            .append("id: \"").append(extId)
            .append("\", requester: \"").append(requester)
            .append("\", channelIn: \"").append(channelIn)
            .append("\", channelOut: \"").append(channelOut)
            .append("\", customer: \"").append(customer)
            .append("\", service: \"").append(service)
            .append("\", urgent: ").append(urgent)
            .append(", submitted: \"").append(submitted)
            .append("\", comments: \"").append(comments)
            .append("\", areacode: \"").append(areaCode)
            .append("\", email: \"").append(email).append("\"")
            .append("}").toString();
    }
    
    

}
