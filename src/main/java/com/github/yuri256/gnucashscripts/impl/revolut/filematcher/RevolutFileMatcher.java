package com.github.yuri256.gnucashscripts.impl.revolut.filematcher;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.filematcher.FileMatcher;
import com.github.yuri256.gnucashscripts.impl.revolut.job.RevolutJobFactory;

import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Pattern;

public class RevolutFileMatcher implements FileMatcher {

    private static final Pattern REVOLUT_FILENAME_PATTERN = Pattern.compile("^account-statement_\\d{4}-\\d{2}-\\d{2}_\\d{4}-\\d{2}-\\d{2}_.*\\.csv$");

    private final Path targetDir;

    public RevolutFileMatcher() {
        this.targetDir = RevolutJobFactory.createCompleteJob(Config.load()).getInDir().toPath();
    }

    @Override
    public Optional<Path> getTargetFileIfHandled(Path filePath) {
        String fileName = filePath.getFileName().toString();
        if (!REVOLUT_FILENAME_PATTERN.matcher(fileName).matches()) {
            return Optional.empty();
        }

        String targetFileName = "revolut_" + fileName.substring(18);
        return Optional.of(targetDir.resolve(targetFileName));
    }
}