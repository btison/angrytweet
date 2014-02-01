package org.jboss.demo.fsw.angrytweet.model;

import java.util.ArrayList;
import java.util.List;

public class ProviderServices {
    
    private static List<String> services;
    
    static {
        services = new ArrayList<String>();
        services.add("internet");
        services.add("cable");
        services.add("tel");
    }
    
    public static boolean exists(String service) {
        if (service == null) {
            return false;
        }
        if (services.contains(service.toLowerCase())) {
            return true;
        }
        return false;
    }

}
