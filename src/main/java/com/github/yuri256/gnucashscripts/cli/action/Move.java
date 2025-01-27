package com.github.yuri256.gnucashscripts.cli.action;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.config.Property;
import com.github.yuri256.gnucashscripts.filematcher.FileMatcher;
import com.github.yuri256.gnucashscripts.impl.abn.filematcher.AbnFileMatcher;
import com.github.yuri256.gnucashscripts.impl.abntxt.filematcher.AbnTxtFileMatcher;
import com.github.yuri256.gnucashscripts.impl.bunq.filematcher.BunqFileMatcher;
import com.github.yuri256.gnucashscripts.impl.ing.filematcher.IngFileMatcher;
import com.github.yuri256.gnucashscripts.impl.ingsavings.filematcher.IngSavingsFileMatcher;
import com.github.yuri256.gnucashscripts.impl.revolut.filematcher.RevolutFileMatcher;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@CommandLine.Command(name = "move", description = "Move files from 'download' directory to 'in' directory of appropriate processor")
public class Move implements Runnable {

    private final List<FileMatcher> processors = List.of(
            new IngFileMatcher(),
            new IngSavingsFileMatcher(),
            new BunqFileMatcher(),
            new RevolutFileMatcher(),
            new AbnFileMatcher(),
            new AbnTxtFileMatcher()
    );

    @Override
    public void run() {
        String downloadsDir = Config.load().get(Property.DOWNLOADS_DIR);
        if (downloadsDir == null) {
            System.out.println("downloadsDir not set");
            System.exit(1);
        }

        File downloadsDirFile = new File(downloadsDir);
        if (!downloadsDirFile.exists()) {
            System.out.println("Downloads dir does not exist: " + downloadsDir);
            System.exit(1);
        }

        try (var paths = Files.list(downloadsDirFile.toPath())) {
            paths.forEach(this::processFile);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private void processFile(Path filePath) {
        for (FileMatcher processor : processors) {
            processor.getTargetFileIfHandled(filePath).ifPresent(targetPath -> {
                System.out.println("Moving " + filePath + " to " + targetPath);
                try {
                    Files.move(filePath, targetPath);
                } catch (IOException e) {
                    System.out.println("Could not move file " + filePath + " to " + targetPath + ": " + e.getMessage());
                    System.exit(1);
                }
            });
            return;
        }
    }
}