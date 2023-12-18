package org.example;

// necessary imports
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.keyverifier.AcceptAllServerKeyVerifier;
import org.apache.sshd.client.keyverifier.KnownHostsServerKeyVerifier;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.config.keys.FilePasswordProvider;
import org.apache.sshd.common.config.keys.loader.openssh.OpenSSHKeyPairResourceParser;
import org.apache.sshd.common.util.io.resource.PathResource;
import org.apache.sshd.sftp.client.SftpClient;
import org.apache.sshd.sftp.client.SftpClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// optional imports to handle EdDSA provider error
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.Security;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.util.Collection;

public class App {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final String user = "user";
    private static final String host = "localhost";
    private static final int port = 2222;
    private static final Path privateKeyPath = Paths.get("/Users/leslieasamoa/CrushDrive/data_over_multi_sftp/config/keys/sftp-user-key");

    public static void main(String[] args) throws IOException {
        try (SshClient client = createSshClient()) {
            connectToSession(client);
        }
    }

    private static SshClient createSshClient() {
        SshClient client = SshClient.setUpDefaultClient();
        client.start();

//        FilePasswordProvider filePasswordProvider = FilePasswordProvider.EMPTY;

        // If server's key not in known hosts, accept it (unsafe! only for this example)
        Path knownHostsPath = Paths.get(System.getProperty("user.home"), ".ssh", "known_hosts");
        KnownHostsServerKeyVerifier knownHostsVerifier = new KnownHostsServerKeyVerifier(AcceptAllServerKeyVerifier.INSTANCE, knownHostsPath);
        client.setServerKeyVerifier(knownHostsVerifier);

        return client;
    }

    private static void connectToSession(SshClient client) {
        try (ClientSession session = client.connect(user, host, port).verify(10000).getSession()) {
            authenticateSession(session);

            SftpClient sftpClient = SftpClientFactory.instance().createSftpClient(session);
            String remoteDirectory = "/";

            for (SftpClient.DirEntry entry : sftpClient.readDir(remoteDirectory)) {
                logger.info(entry.getFilename());
            }

        } catch (Exception e) {
            logger.error("An error occurred", e);
        }
    }

    private static void authenticateSession(ClientSession session) {
        try {
            Collection<KeyPair> keys = OpenSSHKeyPairResourceParser.INSTANCE.loadKeyPairs(session, new PathResource(privateKeyPath), FilePasswordProvider.EMPTY);
            session.addPublicKeyIdentity(keys.iterator().next());
            session.auth().verify(10000);
        } catch (IOException | GeneralSecurityException e) {
            logger.error("An error occurred during authentication", e);
        }
    }
}