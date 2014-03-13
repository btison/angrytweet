package org.jboss.demo.fsw.angrytweet.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/serviceticket")
public interface ServiceTicketResource {
    
    @POST
    @Path("/")
    @Consumes("application/json")
    public void newTicket(ServiceTicket serviceTicket);

}
