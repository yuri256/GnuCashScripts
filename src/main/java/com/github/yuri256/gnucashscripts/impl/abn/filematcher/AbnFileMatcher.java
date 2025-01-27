package com.github.yuri256.gnucashscripts.impl.abn.filematcher;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.filematcher.FileMatcher;
import com.github.yuri256.gnucashscripts.impl.abn.job.AbnJobFactory;

import java.nio.file.Path;
import java.util.Optional;

public class AbnFileMatcher implements FileMatcher {

    private final Path targetDir;

    public AbnFileMatcher() {
        this.targetDir = AbnJobFactory.createCompleteJob(Config.load()).getInDir().toPath();
    }

    @Override
    public Optional<Path> getTargetFileIfHandled(Path filePath) {
        String fileName = filePath.getFileName().toString();
        if (!(fileName.startsWith("MT940") && fileName.endsWith(".STA"))) {
            return Optional.empty();
        }

        String date = fileName.substring(5, 11);
        String time = fileName.substring(11, 17);
        String targetFileName = "abn_20" + date + "_" + time + ".STA";
        return Optional.of(targetDir.resolve(targetFileName));
    }
}