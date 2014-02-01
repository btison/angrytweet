package org.jboss.demo.fsw.angrytweet.csv;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Named;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.jboss.demo.fsw.angrytweet.model.ProviderServiceTicket;
import org.jboss.demo.fsw.angrytweet.model.ProviderServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named("CsvRecordProcessor")
public class CsvRecordProcessor implements Processor {
    
    private static Logger logger = LoggerFactory.getLogger(CsvRecordProcessor.class);
    
    public void process(Exchange exchange) {
        
        String body = exchange.getIn().getBody(String.class);
        
        String[] tokens = body.split(";");
        
        ProviderServiceTicket ticket = new ProviderServiceTicket();
        ticket.setExtId("H" + normalize(tokens[0]));
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
        Date submitted = null;
        try {
            submitted = dateFormat.parse(normalize(tokens[1]));
        } catch (ParseException e) {
            logger.warn("Cannot parse date '" + tokens[1] + "'");
            submitted = new Date();
        }
        ticket.setSubmitted(submitted);
        ticket.setRequester(normalize(tokens[2]));
        String service = normalize(tokens[3]);
        if (ProviderServices.exists(service)) {
            ticket.setService(service);
        } else {
            ticket.setService("N.A.");
        }
        ticket.setComments(normalize(tokens[4]));
        ticket.setUrgent(Integer.parseInt(normalize(tokens[5])));
        ticket.setChannelIn("Helpdesk"); 
        
        exchange.getIn().setBody(ticket);
    }
    
    public String normalize(String s) {
        if (s.startsWith("'")) {
            return s.substring(1,s.length()-1);
        }
        return s;
    }

}
