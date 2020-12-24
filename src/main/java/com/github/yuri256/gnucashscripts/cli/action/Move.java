package com.github.yuri256.gnucashscripts.cli.action;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.config.Property;
import com.github.yuri256.gnucashscripts.impl.abn.job.AbnJobFactory;
import com.github.yuri256.gnucashscripts.impl.ing.job.IngJobFactory;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@CommandLine.Command(name = "move", description = "Move files from 'download' directory to 'in' directory of appropriate processor")
public class Move implements Runnable {
    @Override
    public void run() {
                String downloadsDir = Config.load().get(Property.DOWNLOADS_DIR);
        if (downloadsDir == null) {
            System.out.println("downloadsDir not set");
            System.exit(1);
        }
        File downloadsDirFile = new File(downloadsDir);
        if (!downloadsDirFile.exists()) {
            System.out.println("Downloads dir does not exits: " + downloadsDir);
            System.exit(1);
        }

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

        Path ingInDir = IngJobFactory.createCompleteJob(Config.load()).getInDir().toPath();
        Path abnInDir = AbnJobFactory.createCompleteJob(Config.load()).getInDir().toPath();

        try {
            Files.list(downloadsDirFile.toPath()).forEach(path -> {
                if (path.getFileName().toString().startsWith("NL03INGB")
                        && path.getFileName().toString().endsWith(".csv")) {
                    System.out.println("ing = " + path);

                    try {
                        Files.move(path, ingInDir.resolve(path.getFileName()));
                    } catch (IOException e) {
                        System.out.println("Could not move file " + path + " to " + ingInDir + ": " + e);
                    }
                }

                if (path.getFileName().toString().startsWith("MT940")
                        && path.getFileName().toString().endsWith(".STA")) {
                    System.out.println("abn = " + path);
                    try {
                        Files.move(path, abnInDir.resolve(path.getFileName()));
                    } catch (IOException e) {
                        System.out.println("Could not move file " + path + " to " + abnInDir + ": " + e);
                    }
                }
            });
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
