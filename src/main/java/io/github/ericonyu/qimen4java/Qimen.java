package io.github.ericonyu.qimen4java;

import com.google.gson.Gson;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Map;

public final class Qimen {
    private static final Gson GSON = new Gson();
    private static volatile Path engine;

    private Qimen() {}

    private static synchronized Path engine() throws IOException {
        if (engine == null) {
            engine = Files.createTempFile("qimen-engine-", ".cjs");
            try (InputStream in = Qimen.class.getResourceAsStream("/engine.cjs")) {
                if (in == null) throw new IOException("engine.cjs missing");
                Files.copy(in, engine, StandardCopyOption.REPLACE_EXISTING);
            }
            engine.toFile().deleteOnExit();
        }
        return engine;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> run(Map<String, Object> input) throws IOException, InterruptedException {
        Process process = new ProcessBuilder("node", engine().toString()).start();
        try (OutputStream out = process.getOutputStream()) {
            out.write(GSON.toJson(input).getBytes(StandardCharsets.UTF_8));
        }
        String stdout = new String(process.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        String stderr = new String(process.getErrorStream().readAllBytes(), StandardCharsets.UTF_8);
        int code = process.waitFor();
        Map<String, Object> envelope = GSON.fromJson(stdout, Map.class);
        if (code != 0 || !Boolean.TRUE.equals(envelope.get("ok"))) {
            throw new IOException(String.valueOf(envelope.getOrDefault("error", stderr)));
        }
        return envelope;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> calculate(Map<String, Object> input) throws IOException, InterruptedException {
        return (Map<String, Object>) run(input).get("data");
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> canonical(Map<String, Object> input) throws IOException, InterruptedException {
        return (Map<String, Object>) run(input).get("canonical");
    }
}
