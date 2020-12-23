package com.github.yuri256.gnucashscripts.cli.action;

import com.github.yuri256.gnucashscripts.job.CompleteJob;
import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.config.Property;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@CommandLine.Command(name = "done", description = "Move ALL files from in to done directory")
public class Done implements Runnable {
    @Override
    public void run() {
        System.out.println("Moving files to 'done' dir");

        String baseDir = Config.load().get(Property.BASE_DIR);
        if (baseDir == null) {
            System.out.println("baseDir dir not set");
            System.exit(1);
        }
        File baseDirFile = new File(baseDir);
        if (!baseDirFile.exists()) {
            System.out.println("dir does not exits: " + baseDir);
            System.exit(1);
        }

        Path jobDir = Paths.get(baseDir, Config.load().get(Property.GNU_CASH_DIR_NAME));
        CompleteJob.checkExists(jobDir.toFile());

        File inDir = CompleteJob.getInDir(jobDir.toFile());
        File doneDir = CompleteJob.getDoneDir(jobDir.toFile());
        try {
            Files.list(inDir.toPath()).forEach(path -> {
                System.out.println("Moving file " + path);
                try {
                    Files.move(path, doneDir.toPath().resolve(path.getFileName()));
                } catch (IOException e) {
                    System.out.println("Could not move file " + path + " to " + doneDir + ": " + e);
                }
            });
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
