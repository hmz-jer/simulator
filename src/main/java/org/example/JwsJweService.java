package org.example;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSAEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Service
public class JwsJweService {


    public String signAndEncrypt(String payload, KeyPair keyPair) throws JOSEException {
        try {
            // Signature JWS
            JWSSigner signer = new RSASSASigner((RSAPrivateKey) keyPair.getPrivate());
            JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.RS256), new Payload(payload));
            jwsObject.sign(signer);

            // Encryptage JWE
            JWEObject jweObject = new JWEObject(
                    new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128GCM)
                            .contentType("JWT") // Indique que le payload est un JWT
                            .build(),
                    new Payload(jwsObject.serialize()));

            RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic(); // Cast PublicKey en RSAPublicKey
            JWEEncrypter encrypter = new RSAEncrypter(rsaPublicKey);
            jweObject.encrypt(encrypter);

            // Renvoie le JWE sérialisé (JWS signé puis encrypté)
            return jweObject.serialize();
        } catch (JOSEException e) {
            // Gestion des erreurs de JOSE
            throw new JOSEException("Erreur lors de la signature ou de l'encryptage: " + e.getMessage(), e);
        }
    }
}

