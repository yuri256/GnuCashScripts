package com.github.yuri256.gnucashscripts.impl.abntxt.filematcher;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.filematcher.FileMatcher;
import com.github.yuri256.gnucashscripts.impl.abntxt.job.AbnTxtJobFactory;

import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AbnTxtFileMatcher implements FileMatcher {

    private static final Pattern ABN_TXT_FILENAME_PATTERN = Pattern.compile("^TXT(\\d{6})(\\d{6})\\.TAB$");

    private final Path targetDir;

    public AbnTxtFileMatcher() {
        this.targetDir = AbnTxtJobFactory.createCompleteJob(Config.load()).getInDir().toPath();
    }

    @Override
    public Optional<Path> getTargetFileIfHandled(Path filePath) {
        String fileName = filePath.getFileName().toString();
        Matcher matcher = ABN_TXT_FILENAME_PATTERN.matcher(fileName);
        if (!matcher.matches()) {
            return Optional.empty();
        }

        String date = matcher.group(1);
        String time = matcher.group(2);
        String targetFileName = "abn_20" + date + "_" + time + ".TAB";
        return Optional.of(targetDir.resolve(targetFileName));
    }
}