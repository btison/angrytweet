package org.jboss.demo.fsw.angrytweet.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kie.api.runtime.process.WorkItem;
import org.kie.api.runtime.process.WorkItemHandler;
import org.kie.api.runtime.process.WorkItemManager;
import org.switchyard.common.lang.Strings;

public class MockSwitchyardServiceTaskHandler implements WorkItemHandler {
    
    private Map<String, List<Map<String, Object>>> invocations = new HashMap<String, List<Map<String,Object>>>();
    
    private Map<String, Object> results = new HashMap<String, Object>();
    
    private Map<String, SwitchYardServiceTaskHandlerCallback> callbacks = new HashMap<String, SwitchYardServiceTaskHandlerCallback>();
    
    public MockSwitchyardServiceTaskHandler() {
        
    }

    @Override
    public void abortWorkItem(WorkItem arg0, WorkItemManager arg1) {
        
    }

    @Override
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        Map<String, Object> parameters = workItem.getParameters();
        String invokedService;
        String serviceName = (String) workItem.getParameter("ServiceName");
        String operationName = (String) workItem.getParameter("OperationName");
        invokedService = (operationName != null) ? serviceName + "_" + operationName : serviceName;  
        if (invokedService == null) {
            manager.completeWorkItem(workItem.getId(), null);
        }
        if (!invocations.containsKey(invokedService)) {
            List<Map<String, Object>> invocationsList = new ArrayList<Map<String,Object>>();
            invocations.put(invokedService, invocationsList);
        }
        Map<String, Object> invocationParams = new HashMap<String, Object>();
        invocations.get(invokedService).add(invocationParams);
        
        String parameterName = getParameterName(parameters);
        invocationParams.put(parameterName, parameterName);
        
        String resultName = getResultName(parameters);
        invocationParams.put("ResultName", resultName);
        
        Object input = parameters.get(parameterName);
        invocationParams.put(parameterName + "Input", input);
        
        if (callbacks.containsKey(invokedService)) {
            callbacks.get(invokedService).handle();
        }
        
        Map<String, Object> wihResults = workItem.getResults();
        wihResults.put(resultName, results.get(invokedService));
        
        manager.completeWorkItem(workItem.getId(), wihResults);
        
    }
    
    private String getParameterName(Map<String, Object> parameters) {
        return getString("ParameterName", parameters, "Parameter");
    }
    
    private String getResultName(Map<String, Object> parameters) {
        return getString("ResultName", parameters, "Result");
    }
    
    protected String getString(String parameterName, Map<String, Object> parameters, String defaultValue) {
        String value = null;
        Object p = parameters.get(parameterName);
        if (p != null) {
            value = Strings.trimToNull(String.valueOf(p));
        }
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    public Map<String, Object> getResults() {
        return results;
    }

    public Map<String, List<Map<String, Object>>> getInvocations() {
        return invocations;
    }
    
    public void whenServiceInvokedThenInvokeCallback(String service, SwitchYardServiceTaskHandlerCallback callback) {
        callbacks.put(service, callback);
    }
    
    public void whenServiceInvokedThenReturn(String service, Object result) {
        results.put(service, result);
    }
    
    public boolean verifyServiceInvoked(String service, int numberInvocations) {
        if (!invocations.containsKey(service)) {
            return false;
        }
        int i = invocations.get(service).size();
        if (i == numberInvocations) {
            return true;
        }
        return false;
    }
    
    public Object getObjectPassedAsParameter(String parameter, String service, int invocation) {
        if (!invocations.containsKey(service)) {
            return null;
        }
        if (invocation > invocations.get(service).size()) {
            return null;
        }
        return invocations.get(service).get(invocation-1).get(parameter + "Input");
    }    
}
