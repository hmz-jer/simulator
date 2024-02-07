package org.example;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.RSADecrypter;

import java.security.interfaces.RSAPrivateKey;
import java.text.ParseException;

public class JweDecryptService {
        public String decrypt(String jweString, RSAPrivateKey privateKey) throws JOSEException, ParseException {
            JWEObject jweObject = JWEObject.parse(jweString);
            jweObject.decrypt(new RSADecrypter(privateKey));
            return jweObject.getPayload().toString();
        }
}
