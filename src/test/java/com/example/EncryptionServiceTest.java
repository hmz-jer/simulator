package com.example;
import org.example.JwsJweService;
import org.example.KeyManagementUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertNotNull;
public class EncryptionServiceTest {

    @TempDir
    Path tempDir;

    @Test
    public void testEncryptService() throws Exception {
        // Créer un fichier JSON temporaire
        Path tempFile = tempDir.resolve("test.json");
        String jsonContent = "{\"name\":\"Test User\",\"age\":30}";
        Files.write(tempFile, Collections.singleton(jsonContent));

        // Ici, vous lisez le fichier et passez son contenu à votre service, similaire à votre cas d'utilisation réel

        // Simuler l'appel au service d'encryptage
        KeyPair keyPair = KeyManagementUtils.generateKeyPair(); // Assurez-vous que SecurityUtils est bien défini
        JwsJweService jwsJweService = new JwsJweService(); // Initialisez correctement votre service
        String encryptedContent = jwsJweService.signAndEncrypt("{\"name\":\"Test User\",\"age\":30}");

        // Vérifier que le contenu encrypté n'est pas null
        assertNotNull(encryptedContent, "Le contenu encrypté ne devrait pas être null");

    }


}

