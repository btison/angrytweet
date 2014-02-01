package org.jboss.demo.fsw.angrytweet.service;

import org.apache.camel.builder.RouteBuilder;

public class CamelRouterService extends RouteBuilder {

    /**
     * The Camel route is configured via this method.  The from endpoint is required to be a SwitchYard service.
     */
    public void configure() {
        from("switchyard://RouterService")
           .log("Received message for 'RouterService' : ${body}")
           .choice()
               .when(simple("${body.channelOut} == \"Twitter\"")).to("direct:twitter")
               .when(simple("${body.channelOut} == \"Email\"")).to("direct:email")
           .end();
        
        from("direct:twitter")
            .log("transferring to twitter")
            .setBody(simple(getTwitterBody()))
            .to("switchyard://TwitterUpdateStatusService");
        
        from("direct:email")
            .log("transferring to email")
            .setHeader("Subject",constant("Thank you for your ticket"))
            .setHeader("To",simple("${body.email}"))
            .setBody(simple(getEmailBody()))
            .to("switchyard://EmailService");
    }
    
    private String getEmailBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("Dear Customer, \n\n")
          .append("Thank you for your ticket. ")
          .append("\n")
          .append("Your ticket reference is ")
          .append("${body.extId}")
          .append("\n")
          .append("The Customer Service Department");        
        return sb.toString();
    }
    
    private String getTwitterBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("@")
          .append("${body.requester} ")
          .append("Ticket ack. ")
          .append("Reference ")
          .append("${body.extId}");
        return sb.toString();
    }

}
