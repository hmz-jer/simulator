package com.example;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.DataGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataGeneratorServiceTest {

    private DataGeneratorService dataGeneratorService;

    @BeforeEach
    void setUp() {
        dataGeneratorService = new DataGeneratorService();
    }

    @Test
    void testGenerateJsonStringWithPrefixForTokenisation() {
        String argument = "tokenisation";
        int operationsCount = 1; // Exemple simple avec un élément
        String result = dataGeneratorService.generateJsonStringWithPrefix(argument, operationsCount);
        assertTrue(result.startsWith("tokenisation\n"), "Le résultat devrait commencer par 'tokenisation\\n'");
    }

    @Test
    void testGenerateJsonStringWithPrefixForDeleteToken() {
        String argument = "delete-token";
        int operationsCount = 1;
        String result = dataGeneratorService.generateJsonStringWithPrefix(argument, operationsCount);
        assertTrue(result.startsWith("delete-token\n"), "Le résultat devrait commencer par 'delete-token\\n'");
        assertTrue(result.contains("\"TokenisedData\""), "Le JSON devrait contenir TokenisedData");
    }

    @Test
    void testGenerateJsonStringWithPrefixForCostoDetokenisation() {
        String argument = "costo/detokenisation";
        int operationsCount = 1;
        String result = dataGeneratorService.generateJsonStringWithPrefix(argument, operationsCount);
        assertTrue(result.startsWith("costo/detokenisation\n"), "Le résultat devrait commencer par 'costo/detokenisation\\n'");
        assertTrue(result.contains("\"TokenisedData\""), "Le JSON devrait contenir TokenisedData");
    }
}
