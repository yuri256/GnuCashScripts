package com.github.yuri256.gnucashscripts.impl.ingsavings.filematcher;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.filematcher.FileMatcher;
import com.github.yuri256.gnucashscripts.impl.ing.filematcher.IngFileMatcher;
import com.github.yuri256.gnucashscripts.impl.ingsavings.job.IngSavingsJobFactory;

import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IngSavingsFileMatcher implements FileMatcher {

    private static final Pattern ING_SAVINGS_FILENAME_PATTERN = Pattern.compile("^Alle_rekeningen_(\\d{2}-\\d{2}-\\d{4})_(\\d{2}-\\d{2}-\\d{4})\\.csv$");

    private final Path targetDir;

    public IngSavingsFileMatcher() {
        this.targetDir = IngSavingsJobFactory.createCompleteJob(Config.load())
                .getInDir()
                .toPath();
    }


    @Override
    public Optional<Path> getTargetFileIfHandled(Path filePath) {
        String fileName = filePath.getFileName().toString();
        Matcher matcher = ING_SAVINGS_FILENAME_PATTERN.matcher(fileName);
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String dateStart = IngFileMatcher.flipDate(matcher.group(1));
        String dateEnd = IngFileMatcher.flipDate(matcher.group(2));
        String targetFileName = "ing_savings_" + dateStart + "_" + dateEnd + ".csv";
        return Optional.of(targetDir.resolve(targetFileName));
    }

}