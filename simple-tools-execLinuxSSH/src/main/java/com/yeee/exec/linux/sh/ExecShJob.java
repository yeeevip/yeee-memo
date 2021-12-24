package com.yeee.exec.linux.sh;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.util.security.SecurityUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.time.Duration;
import java.util.EnumSet;

/**
 * description ...
 *
 * @author yeeeee
 * @since 2021/12/27 10:24
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ExecShJob extends QuartzJobBean {

    private final ShConfiguration.Properties properties;

    private long getTimeoutWithDefault(JobDataMap jobDataMap, String key, Duration defaultValue) {
        Object timeout = jobDataMap.get(key);
        if (timeout == null) {
            return defaultValue.toMillis();
        }
        if (timeout instanceof Long) {
            return (long) timeout;
        }
        try {
            return Long.parseLong(timeout.toString());
        } catch (Exception e) {
            return defaultValue.toMillis();
        }
    }

    @Override
    protected void executeInternal(JobExecutionContext jobContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobContext.getMergedJobDataMap();
        String host = jobDataMap.getString("host");
        int port = jobDataMap.getInt("port");
        String user = jobDataMap.getString("user");
        String password = jobDataMap.getString("password");
        String privateKey = jobDataMap.getString("privateKey");
        String command = jobDataMap.getString("command");
        long connectTimeout = getTimeoutWithDefault(jobDataMap, "connectTimeout", properties.getTimeouts().getConnect());
        long authTimeout = getTimeoutWithDefault(jobDataMap, "authTimeout", properties.getTimeouts().getAuth());
        long openChannelTimeout = getTimeoutWithDefault(jobDataMap, "openChannelTimeout", properties.getTimeouts().getAuth());
        long executionTimeout = getTimeoutWithDefault(jobDataMap, "executionTimeout", properties.getTimeouts().getAuth());

        try (SshClient sshClient = SshClient.setUpDefaultClient()) {
            sshClient.start();
            try (ClientSession session = sshClient.connect(user, host, port).verify(connectTimeout).getSession()) {
                if (StringUtils.hasText(password)) {
                    session.addPasswordIdentity(password);
                }
                if (StringUtils.hasText(privateKey)) {
                    Iterable<KeyPair> keyPairIterable = SecurityUtils.loadKeyPairIdentities(session, null, new ByteArrayInputStream(privateKey.getBytes(StandardCharsets.UTF_8)), null);
                    if (keyPairIterable == null || !keyPairIterable.iterator().hasNext()) {
                        throw new GeneralSecurityException("Failed to load the private key");
                    }
                    session.addPublicKeyIdentity(keyPairIterable.iterator().next());
                }
                session.auth().verify(authTimeout);

                try (ClientChannel channel = session.createExecChannel(command); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                    channel.setOut(byteArrayOutputStream);
                    channel.open().verify(openChannelTimeout);
                    channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), executionTimeout);
                    Integer status = channel.getExitStatus();
                    String shellOutput = byteArrayOutputStream.toString();
                    log.info("---------------- Shell Output --------------\n {}", shellOutput);
                    if (status != 0) {
                        String message = String.format("Failed to execute command: %s, exit status: %d", command, status);
                        throw new JobExecutionException(message);
                    }
                }
            } finally {
                sshClient.stop();
            }
        } catch (IOException | GeneralSecurityException e) {
            throw new JobExecutionException(String.format("Failed to connect/operate %s@%s:%d", user, host, port));
        }

    }
}
