package com.example;

import org.example.DataGeneratorService;
import org.example.JwsJweService;
import org.example.KeyManagementUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.security.KeyPair;

class JsonAndEncryptionTest {

    @Test
    void testJsonGenerationAndEncryption() throws Exception {
        DataGeneratorService generator = new DataGeneratorService();
        int recurrence = 5; // Exemple de récurrence
        String argument = "tokenisation"; // Exemple d'argument

        // Génération de la chaîne JSON avec préfixe
        String content = generator.generateJsonStringWithPrefix(argument,recurrence);

        System.out.println(content);
        assertNotNull(content, "La chaîne générée ne devrait pas être null");

        // Génération de la paire de clés pour le test
        KeyPair keyPair = KeyManagementUtils.generateKeyPair(); // Simulez ou générez une vraie paire de clés pour le test

        // Encryptage et signature (Assurez-vous d'avoir une implémentation mock ou réelle pour le test)
        JwsJweService jwsJweService = new JwsJweService(); // Utilisez Mockito pour mocker cette dépendance si nécessaire
        String encryptedSignedContent = jwsJweService.signAndEncrypt(content, keyPair);

        // Vérifiez que le contenu retourné est encrypté et signé (selon ce que vous pouvez tester sans déchiffrer)
        assertNotNull(encryptedSignedContent, "Le contenu encrypté et signé ne devrait pas être null");
    }
}
