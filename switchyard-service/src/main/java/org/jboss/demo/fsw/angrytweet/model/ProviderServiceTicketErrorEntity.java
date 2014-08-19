package org.jboss.demo.fsw.angrytweet.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="ticket_error")
@SequenceGenerator(name="ticketErrorIdSeq", sequenceName="TICKET_ERROR_ID_SEQ")
public class ProviderServiceTicketErrorEntity {
    
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator="ticketErrorIdSeq")
    private Long id;
    
    @Lob
    @Column(length=2147483647)
    private String ticket;
    
    
    public long getId() {
        return id;
    }
    
    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return ticket;
    }
    
    

}
