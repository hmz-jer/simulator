package org.example;

import org.apache.commons.lang3.RandomStringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Service;


@Service
public class DataGeneratorService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateJsonStringWithPrefix(String argument, int operationsCount) {
        ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.put("CustMsgId", "0123456789012345");
        rootNode.put("CustCnxId", "123456654321");

        ArrayNode operationsList = rootNode.putArray("OperationsList");

        switch (argument) {
            case "tokenisation":
                populateOperationsListForT(operationsList, operationsCount);
                return "tokenisation\n" + serializeJson(rootNode);
            case "delete-token":
                populateOperationsListForS(operationsList, operationsCount);
                return "delete-token\n" + serializeJson(rootNode);
            case "costo/detokenisation":
                populateOperationsListForD(operationsList, operationsCount);
                return "costo/detokenisation\n" + serializeJson(rootNode);
            default:
                throw new IllegalArgumentException("Argument non supporté: " + argument);
        }
    }

    private void populateOperationsListForT(ArrayNode operationsList, int operationsCount) {
        for (int i = 0; i < operationsCount; i++) {
            ObjectNode operationNode = operationsList.addObject();
            operationNode.put("OperationId", RandomStringUtils.randomAlphanumeric(16));
            operationNode.put("SensitiveData", RandomStringUtils.randomAlphanumeric(20));
            operationNode.put("SensitiveDataType", "01");
        }
    }

    private void populateOperationsListForS(ArrayNode operationsList, int operationsCount) {
        for (int i = 0; i < operationsCount; i++) {
            ObjectNode operationNode = operationsList.addObject();
            operationNode.put("OperationId", RandomStringUtils.randomAlphanumeric(16));
            operationNode.put("TokenisedData", RandomStringUtils.randomAlphanumeric(130));
            operationNode.put("SensitiveData", RandomStringUtils.randomAlphanumeric(20));
            operationNode.put("SensitiveDataType", "01");
        }
    }

    private void populateOperationsListForD(ArrayNode operationsList, int operationsCount) {
        for (int i = 0; i < operationsCount; i++) {
            ObjectNode operationNode = operationsList.addObject();
            operationNode.put("OperationId", RandomStringUtils.randomAlphanumeric(16));
            operationNode.put("TokenisedData", RandomStringUtils.randomAlphanumeric(130));
        }
    }

    private String serializeJson(ObjectNode rootNode) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
