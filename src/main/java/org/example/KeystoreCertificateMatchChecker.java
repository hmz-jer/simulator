package org.example;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.cert.Certificate;

public class KeystoreCertificateMatchChecker {

    public static void main(String[] args) throws Exception {
        String keystorePath = "chemin/vers/keystore.jks";
        String keystorePassword = "motDePasseDuKeystore";
        String alias = "aliasDuCertificat";

        // Charger le keystore
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream keystoreStream = new FileInputStream(keystorePath)) {
            keystore.load(keystoreStream, keystorePassword.toCharArray());
        }

        // Obtenir le certificat et la clé privée
        Certificate cert = keystore.getCertificate(alias);
        PrivateKey privateKey = (PrivateKey) keystore.getKey(alias, keystorePassword.toCharArray());

        if (cert == null || privateKey == null) {
            System.out.println("Certificat ou clé privée introuvable pour l'alias : " + alias);
            return;
        }

        // Comparer les clés publiques
        PublicKey publicKeyFromCert = cert.getPublicKey();
        PublicKey publicKeyFromPrivateKey = keystore.getCertificate(alias).getPublicKey();

        if (publicKeyFromCert.equals(publicKeyFromPrivateKey)) {
            System.out.println("La clé publique du certificat correspond à la clé publique de la clé privée.");
        } else {
            System.out.println("La clé publique du certificat ne correspond pas à la clé publique de la clé privée.");
        }
    }
}
