package com.github.yuri256.gnucashscripts.impl.bunq.filematcher;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.filematcher.FileMatcher;
import com.github.yuri256.gnucashscripts.impl.bunq.job.BunqJobFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BunqFileMatcher implements FileMatcher {

    private static final Pattern BUNQ_FILENAME_PATTERN = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2})\\s(\\d{4}-\\d{2}-\\d{2})\\s-\\sExport Statement.*\\.csv$");

    private final Path targetDir;

    public BunqFileMatcher() {
        this.targetDir = BunqJobFactory.createCompleteJob(Config.load()).getInDir().toPath();
    }

    @Override
    public Optional<Path> getTargetFileIfHandled(Path filePath) {
        String fileName = filePath.getFileName().toString();
        Matcher matcher = BUNQ_FILENAME_PATTERN.matcher(fileName);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        String accountCode = getAccountCode(filePath);
        String targetFileName = "bunq_" + accountCode + "_" + matcher.group(1) + "_" + matcher.group(2) + ".csv";
        return Optional.of(targetDir.resolve(targetFileName));
    }

    private String getAccountCode(Path path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line = "";
            for (int i = 0; i < 2; i++) {
                line = reader.readLine();
                if (line == null) {
                    return "";
                }
            }
            var separator = line.contains("\";\"") ? "\";\"" : "\",\"";
            return line.split(separator)[3]; // IBAN is the 4th field
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}