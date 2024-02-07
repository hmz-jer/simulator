package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class JsonGenerator {

    public String generateJsonString(int recurrence, String argument) throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();

        root.put("attribut1", "valeur1");
        root.put("attribut2", "valeur2");
        root.put("attribut3", "valeur3");
        root.put("attribut4", "valeur4");

        ArrayNode liste = root.putArray("liste");
        for (int i = 0; i < recurrence; i++) {
            ObjectNode item = mapper.createObjectNode();
            item.put("attributA", "valeurA");
            liste.add(item);
        }

        // Convertir le JSON en String
        String jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);

        // Préparer le préfixe en fonction de l'argument
        String prefix = "";
        if ("C".equals(argument)) {
            prefix = "token\n";
        } else if ("D".equals(argument)) {
            prefix = "/dse/costo\n";
        }

        // Concaténer le préfixe et le JSON
        return prefix + jsonString;
    }


}

