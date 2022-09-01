package com.acme.acmefrontend.utils;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.util.Map;

public class AcmeProcessEngine {
    private ProcessEngine processEngine;
    private boolean isCorrelationSuccessful;

    public AcmeProcessEngine(ProcessEngine processEngine){
        this.processEngine = processEngine;
        this.isCorrelationSuccessful = false;
    }

    public void setVariable(String processId, String varName, Object o) {
        this.processEngine.getRuntimeService().setVariable(processId, varName, o);
    }

    public Object getVariable(String processId, String varName){
        RuntimeService service = this.processEngine.getRuntimeService();

        if(service.getActivityInstance(processId) != null) {
            return service.getVariable(processId, varName);
        }

        return this.processEngine.getHistoryService().createHistoricVariableInstanceQuery()
                .processInstanceId(processId).variableId(varName).list().stream()
                .findFirst().map(HistoricVariableInstance::getValue).orElse(null);
    }

    public ProcessInstance startProcessByMessage(String message, Map<String, Object> variable) {
        return this.processEngine
                .getRuntimeService()
                .startProcessInstanceByMessage(message, variable);
    }

    public void correlate(String processId, String message){
        try{
            this.processEngine.getRuntimeService()
                    .createMessageCorrelation(message)
                    .processInstanceId(processId)
                    .correlate();
        } catch (Exception e) {
            this.isCorrelationSuccessful = true;
        }
    }

    public void correlate(String message) {
        try{
            this.processEngine.getRuntimeService()
                    .createMessageCorrelation(message)
                    .correlate();
            this.isCorrelationSuccessful = true;
        } catch (Exception e){
            this.isCorrelationSuccessful = false;
        }
    }

    public boolean isCorrelationSuccessful() {
        return isCorrelationSuccessful;
    }
}
