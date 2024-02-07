package org.example;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Service
public class JwsJweService {

    @Value("${jws.keystore.path}")
    private String jwsKeystorePath;

    @Value("${jws.keystore.password}")
    private String jwsKeystorePassword;

    @Value("${jws.key.alias}")
    private String jwsKeyAlias;

    @Value("${jws.key.password}")
    private String jwsKeyPassword;

    @Value("${jwe.encrypt.keystore.path}")
    private String jweEncryptKeystorePath;

    @Value("${jwe.encrypt.keystore.password}")
    private String jweEncryptKeystorePassword;

    @Value("${jwe.encrypt.key.alias}")
    private String jweEncryptKeyAlias;

    @Value("${jwe.decrypt.keystore.path}")
    private String jweDecryptKeystorePath;

    @Value("${jwe.decrypt.keystore.password}")
    private String jweDecryptKeystorePassword;

    @Value("${jwe.decrypt.key.alias}")
    private String jweDecryptKeyAlias;

    @Value("${jwe.decrypt.key.password}")
    private String jweDecryptKeyPassword;

    public String signAndEncrypt(String payload) throws Exception {
        // Charger la clé privée pour JWS
        PrivateKey jwsPrivateKey = loadPrivateKey(jwsKeystorePath, jwsKeystorePassword, jwsKeyAlias, jwsKeyPassword);

        // Charger la clé publique pour JWE
        PublicKey jwePublicKey = loadPublicKey(jweEncryptKeystorePath, jweEncryptKeystorePassword, jweEncryptKeyAlias);

        // Signature JWS
        JWSSigner signer = new RSASSASigner(jwsPrivateKey);
        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.RS256), new Payload(payload));
        jwsObject.sign(signer);

        // Encryptage JWE
        JWEObject jweObject = new JWEObject(new JWEHeader.Builder(JWEAlgorithm.RSA_OAEP_256, EncryptionMethod.A128GCM).contentType("JWT").build(), new Payload(jwsObject.serialize()));
        JWEEncrypter encrypter = new RSAEncrypter((RSAPublicKey) jwePublicKey);
        jweObject.encrypt(encrypter);

        return jweObject.serialize();
    }

    private PrivateKey loadPrivateKey(String keystorePath, String keystorePassword, String keyAlias, String keyPassword) throws Exception {
        KeyStore keystore = KeyStore.getInstance("JKS");
        try (FileInputStream keystoreStream = new FileInputStream(keystorePath)) {
            keystore.load(keystoreStream, keystorePassword.toCharArray());
        }
        return (PrivateKey) keystore.getKey(keyAlias, keyPassword.toCharArray());
    }

    private PublicKey loadPublicKey(String keystorePath, String keystorePassword, String keyAlias) throws Exception {
        KeyStore keystore = KeyStore.getInstance("JKS");
        try (FileInputStream keystoreStream = new FileInputStream(keystorePath)) {
            keystore.load(keystoreStream, keystorePassword.toCharArray());
        }
        Certificate cert = keystore.getCertificate(keyAlias);
        return cert.getPublicKey();
    }

    // Méthode pour décrypter et vérifier le JWS dans le JWE
    public String decryptAndVerify(String jweString) throws Exception {
        // Charger la clé privée pour le décryptage JWE
        PrivateKey jwePrivateKey = loadPrivateKey(jweDecryptKeystorePath, jweDecryptKeystorePassword, jweDecryptKeyAlias, jweDecryptKeyPassword);

        // Décryptage JWE
        JWEObject jweObject = JWEObject.parse(jweString);
        jweObject.decrypt(new RSADecrypter((RSAPrivateKey) jwePrivateKey));
        String decryptedJwsString = jweObject.getPayload().toString();

        // Charger la clé publique pour la vérification JWS
        PublicKey jwsPublicKey = loadPublicKey(jwsKeystorePath, jwsKeystorePassword, jwsKeyAlias);

        // Vérification JWS
        JWSObject jwsObject = JWSObject.parse(decryptedJwsString);
        JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) jwsPublicKey);
        if (!jwsObject.verify(verifier)) {
            throw new JOSEException("La vérification JWS a échoué.");
        }

        // Retourne le payload JWS décrypté et vérifié
        return jwsObject.getPayload().toString();
    }
}
