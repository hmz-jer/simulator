package com.example;

import org.example.DataGeneratorService;
import org.example.JwsJweService;
import org.example.KeyManagementUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.util.AssertionErrors.assertEquals;

import java.security.KeyPair;

class JsonAndEncryptionTest {

    @Autowired
    private JwsJweService jwsJweService;

    @Test
    public void testSignAndEncrypt() {
        String payload = "Hello, world!";
        try {
            String result = jwsJweService.signAndEncrypt(payload);
            assertNotNull(result);
            System.out.println("JWE Encrypted and Signed Result: " + result);
            // Plus de logique de validation ici, par exemple décrypter/valider le JWE/JWS pour vérifier le contenu
        } catch (Exception e) {
            e.printStackTrace();
            fail("Le test a échoué à cause d'une exception : " + e.getMessage());
        }
    }

    @Test
    public void testEncryptDecrypt() throws Exception {
        String originalPayload = "Hello, world!";
        String encryptedJwe = jwsJweService.signAndEncrypt(originalPayload);
        assertNotNull(encryptedJwe);

        String decryptedPayload = jwsJweService.decryptAndVerify(encryptedJwe);
        assertEquals(originalPayload, decryptedPayload, "Le payload décrypté et vérifié doit correspondre au payload original.");
    }
}
