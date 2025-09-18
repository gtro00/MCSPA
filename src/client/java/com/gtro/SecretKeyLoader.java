package com.gtro;

import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;

public class SecretKeyLoader {
    private static final String DIR = "config/MCSPA";
    private static final String FILE_NAME = "secret.key";

    public static String loadKey() throws IOException {
        Path dir = Paths.get(DIR);

        // 如果目录不存在，创建
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        File file = new File(DIR+"//"+FILE_NAME);
        if (!file.exists()) {
            // 生成 16字节 (128-bit) 随机密钥
            byte[] key = new byte[16];
            new SecureRandom().nextBytes(key);
            //String secret = new String(key);
            String secret=Base64.getEncoder().encodeToString(key);
            Files.write(file.toPath(), secret.getBytes());
            return secret;
        }
        return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8).trim();
    }
}

