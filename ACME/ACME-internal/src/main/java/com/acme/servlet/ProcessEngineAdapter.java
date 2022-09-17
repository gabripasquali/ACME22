package com.acme.servlet;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.util.Map;

public class ProcessEngineAdapter {

    private ProcessEngine processEngine;

    private boolean isCorrelationSuccessful;

    public ProcessEngineAdapter(ProcessEngine processEngine) {
        this.processEngine = processEngine;
        this.isCorrelationSuccessful = false;
    }

    public void setVariable(String processId, String varName, Object o) {
        this.processEngine.getRuntimeService().setVariable(processId, varName, o);
    }

    public Object getVariable(String processId, String varName) {

        RuntimeService service = this.processEngine.getRuntimeService();

        if (service.getActivityInstance(processId) != null)
            return service.getVariable(processId, varName);

        return this.processEngine.getHistoryService().createHistoricVariableInstanceQuery().processInstanceId(processId).variableName(varName).list().stream()
                .findFirst()
                .map(HistoricVariableInstance::getValue)
                .orElse(null);
    }

    public ProcessInstance startProcessInstanceByMessage(String message, Map<String, Object> variable) {

        return this.processEngine
                .getRuntimeService()
                .startProcessInstanceByMessage(message, variable);
    }

    public void correlate(String processId, String message) {
        try {
            this.processEngine.getRuntimeService()
                    .createMessageCorrelation(message)
                    .processInstanceId(processId)
                    .correlate();
            this.isCorrelationSuccessful = true;
        } catch (Exception e) {
            //e.printStackTrace();
            this.isCorrelationSuccessful = false;
        }
    }

    public void correlate(String message) {
        try {
            this.processEngine.getRuntimeService()
                    .createMessageCorrelation(message)
                    .correlate();
            this.isCorrelationSuccessful = true;
        } catch (Exception e) {
//            e.printStackTrace();
            this.isCorrelationSuccessful = false;
        }
    }

    public boolean isActive(String processId) {
        return this.processEngine.getRuntimeService().getActivityInstance(processId) != null;
    }

    public boolean isCorrelationSuccessful() {
        return isCorrelationSuccessful;
    }

}

