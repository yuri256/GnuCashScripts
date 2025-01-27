package com.github.yuri256.gnucashscripts.impl.ing.filematcher;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.filematcher.FileMatcher;
import com.github.yuri256.gnucashscripts.impl.ing.job.IngJobFactory;

import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IngFileMatcher implements FileMatcher {
    private static final Pattern ING_FILENAME_PATTERN = Pattern.compile("^NL\\d{2}INGB\\d{10}_(\\d{2}-\\d{2}-\\d{4})_(\\d{2}-\\d{2}-\\d{4})\\.csv$");

    private final Path targetDir;

    public IngFileMatcher() {
        this.targetDir = IngJobFactory.createCompleteJob(Config.load()).getInDir().toPath();
    }


    @Override
    public Optional<Path> getTargetFileIfHandled(Path filePath) {
        String fileName = filePath.getFileName().toString();
        Matcher matcher = ING_FILENAME_PATTERN.matcher(fileName);
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String iban = fileName.substring(0, 18);
        String dateStart = flipDate(matcher.group(1));
        String dateEnd = flipDate(matcher.group(2));
        String targetFileName = "ing_" + iban + "_" + dateStart + "_" + dateEnd + ".csv";
        return Optional.of(targetDir.resolve(targetFileName));
    }

    public static String flipDate(String dateString) {
        String[] parts = dateString.split("-");
        return parts[2] + "-" + parts[1] + "-" + parts[0];
    }
}