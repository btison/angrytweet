package org.jboss.demo.fsw.angrytweet.transform;

import java.io.IOException;
import java.io.StringWriter;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.jboss.demo.fsw.angrytweet.model.ProviderServiceTicket;
import org.jboss.demo.fsw.angrytweet.model.ProviderServiceTicketEntity;
import org.jboss.demo.fsw.angrytweet.model.ProviderServiceTicketErrorEntity;
import org.switchyard.annotations.Transformer;

public final class ServiceTicketTransformer {

    @Transformer(from="java:org.jboss.demo.fsw.angrytweet.model.ProviderServiceTicket", to="java:org.jboss.demo.fsw.angrytweet.model.ProviderServiceTicketEntity")
    public ProviderServiceTicketEntity transformProviderServiceTicketToEntity(ProviderServiceTicket from) {
        if (from == null) {
            return null;
        }
        ProviderServiceTicketEntity entity = new ProviderServiceTicketEntity();
        entity.setExtId(from.getExtId());
        entity.setRequester(from.getRequester());
        entity.setAreaCode(from.getAreaCode());
        entity.setService(from.getService());
        entity.setComments(from.getComments());
        entity.setUrgent(from.getUrgent());
        entity.setSubmitted(from.getSubmitted());
        entity.setChannelIn(from.getChannelIn());
        entity.setCustomer(from.getCustomer());
        return entity;
    }
    
    @Transformer(from="java:org.jboss.demo.fsw.angrytweet.model.ProviderServiceTicket", to="java:org.jboss.demo.fsw.angrytweet.model.ProviderServiceTicketErrorEntity")
    public ProviderServiceTicketErrorEntity transformProviderServiceTicketToErrorEntity(ProviderServiceTicket from) throws JsonGenerationException, JsonMappingException, IOException {
        if (from == null) {
            return null;
        }
        ProviderServiceTicketErrorEntity entity = new ProviderServiceTicketErrorEntity();
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Inclusion.NON_NULL);
        
        StringWriter writer = new StringWriter();
        mapper.writeValue(writer, from);
        
        String result = writer.toString();
        entity.setTicket(result);
        
        return entity;
    }

}
