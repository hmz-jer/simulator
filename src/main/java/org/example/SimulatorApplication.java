package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.net.ssl.SSLContext;
import java.security.KeyPair;

@SpringBootApplication
public class SimulatorApplication implements CommandLineRunner {

    @Value("${keystore.path}")
    private String keystorePath;

    @Value("${keystore.password}")
    private String keystorePassword;

    @Value("${truststore.path}")
    private String truststorePath;

    @Value("${truststore.password}")
    private String truststorePassword;

    @Value("${application.scheme}")
    private String scheme;

    @Value("${application.host}")
    private String host;

    @Value("${application.port}")
    private int port;

    @Value("${application.basePath}")
    private String basePath;

    @Value("${secure.connection}")
    private boolean secureConnection;

    @Value("${min.attributes}")
    private int minAttributes;

    @Value("${max.attributes}")
    private int maxAttributes;


    @Autowired
    private JsonService jsonService;

    @Autowired
    private SSLConfiguration sslConfiguration;

    @Autowired
    private DataGeneratorService dataGeneratorService;

    private final JwsJweService jwsJweService;

    public SimulatorApplication(JwsJweService jwsJweService) {
        this.jwsJweService = jwsJweService;
    }

    public static void main(String[] args) {
        SpringApplication.run(SimulatorApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length < 1) {
            System.out.println("Veuillez fournir un argument pour l'opération. Utilisez 'T' pour tokenisation, 'S' pour suppression, 'D' pour détokenisation, ou 'R' pour récupération (traité comme tokenisation).");
            return;
        }


        String operationType = args[0];

        String operation;

        switch (operationType) {
            case "T":
            case "R": // 'R' est traité comme 'T'
                operation = "tokenisation";
                if ("R".equals(operationType)) {
                    System.out.println("R (récupération) - traité comme tokenisation.");
                }
                break;
            case "S":
                operation = "delete-token";
                break;
            case "D":
                operation = "costo/detokenisation";
                break;
            default:
                System.out.println("Argument non reconnu. Utilisez 'T' pour tokenisation, 'S' pour suppression, 'D' pour détokenisation, ou 'R' pour récupération (traité comme tokenisation).");
                return;
        }
        // Configuration optionnelle du SSLContext pour les connexions sécurisées
        SSLContext sslContext = secureConnection ? sslConfiguration.createSSLContext(keystorePath, keystorePassword, truststorePath, truststorePassword) : null;

        performOptimizedJsonRequest(operation, sslContext, secureConnection);
    }

    public void performOptimizedJsonRequest(String operation, SSLContext sslContext, boolean secureConnection) throws Exception {
        System.out.printf("Démarrage de l'opération '%s' avec une connexion %s.\n", operation, secureConnection ? "sécurisée" : "non sécurisée");
        long startTime = System.currentTimeMillis();

        // Construire l'URL complète
        String requestUrl = String.format("%s://%s:%d%s/%s", scheme, host, port, basePath, operation);
        System.out.println("URL complète: " + requestUrl);
        int min = 1;
        int max = 10;
        int lastSuccess = 0;
        int lastSuccessSize = 0; // Pour garder la taille du dernier succès
        while (min <= max) {
            int mid = (min + max) / 2;
            String jsonContent = dataGeneratorService.generateJsonStringWithPrefix(operation,mid);
            // Générer une paire de clés RSA
        //    KeyPair keyPair = KeyManagementUtils.generateKeyPair();
            // Signer et encrypter le contenu
            String encrypted = jwsJweService.signAndEncrypt(jsonContent);

            boolean isTooLarge = jsonService.sendJsonAndCheckFor413(requestUrl, sslContext, encrypted, false);

            if (isTooLarge) {
                max = mid - 1;
            } else {
                lastSuccess = mid;
                lastSuccessSize = jsonContent.getBytes().length; // Calculer la taille du message
                min = mid + 1;
            }
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        // Affichage des informations récapitulatives de l'opération
        System.out.printf("| Operation | IP Address | Port | Max Attributes | Message Size (bytes) | Execution Time (ms) |\n");
        System.out.printf("|-----------|------------|------|----------------|----------------------|---------------------|\n");
        System.out.printf("| %9s | %10s | %4d | %14d | %20d | %19d |\n", operation, host, port, lastSuccess, lastSuccessSize, executionTime);

    }
}
