package org.jboss.demo.fsw.angrytweet.transform;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.dom.DOMResult;

import org.jboss.demo.fsw.angrytweet.crm.model.GetAccountInfo;
import org.jboss.demo.fsw.angrytweet.crm.model.GetAccountInfoResponse;
import org.switchyard.annotations.Transformer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class CrmTransformer {

    @Transformer(to = "{urn:crm:account-service:1.0}getAccountInfo")
    public Element transformGetAccountInfoToGetAccountInfo(GetAccountInfo from) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(GetAccountInfo.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        DOMResult result = new DOMResult();
        marshaller.marshal(from, result);
        return ((Document)result.getNode()).getDocumentElement();
        
    }

    @Transformer(from = "{urn:crm:account-service:1.0}getAccountInfoResponse")
    public GetAccountInfoResponse transformGetAccountInfoResponseToGetAccountInfoResponse(Element from) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(GetAccountInfoResponse.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        GetAccountInfoResponse response = (GetAccountInfoResponse) unmarshaller.unmarshal(from);
        return response;
    }

}
