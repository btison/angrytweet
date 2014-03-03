package org.jboss.demo.fsw.angrytweet.csv;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

@Named("CsvSplitter")
public class CsvSplitter {
    
    public List<String> splitBody(String body) {
        List<String> answer = new ArrayList<String>();
        String[] parts = body.split("\n");
        boolean first = true;
        for (String part : parts) {
            if (first) {
                first = false;
            } else {
                answer.add(part);
            }
        }
        return answer;
    }

}
