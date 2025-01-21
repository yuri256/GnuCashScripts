package com.github.yuri256.gnucashscripts.cli.action;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.config.Property;
import com.github.yuri256.gnucashscripts.impl.abn.job.AbnJobFactory;
import com.github.yuri256.gnucashscripts.impl.bunq.job.BunqJobFactory;
import com.github.yuri256.gnucashscripts.impl.ing.job.IngJobFactory;
import com.github.yuri256.gnucashscripts.impl.revolut.job.RevolutJobFactory;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CommandLine.Command(name = "move", description = "Move files from 'download' directory to 'in' directory of appropriate processor")
public class Move implements Runnable {

    private static final Pattern ING_FILENAME_PATTERN = Pattern.compile("^NL\\d{2}INGB\\d{10}_(\\d{2}-\\d{2}-\\d{4})_(\\d{2}-\\d{2}-\\d{4})\\.csv$");
    private static final Pattern BUNQ_FILENAME_PATTERN = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2})\\s(\\d{4}-\\d{2}-\\d{2})\\s-\\sExport Statement.*\\.csv$");
    private static final Pattern REVOLUT_FILENAME_PATTERN = Pattern.compile("^account-statement_\\d{4}-\\d{2}-\\d{2}_\\d{4}-\\d{2}-\\d{2}_.*\\.csv$");

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
        Path bunqInDir = BunqJobFactory.createCompleteJob(Config.load()).getInDir().toPath();
        Path revolutInDir = RevolutJobFactory.createCompleteJob(Config.load()).getInDir().toPath();
        Path abnInDir = AbnJobFactory.createCompleteJob(Config.load()).getInDir().toPath();

        try {
            Files.list(downloadsDirFile.toPath()).forEach(path -> {
                String fileName = path.getFileName().toString();
                Matcher filenameMatcher;

                // ING
                filenameMatcher = ING_FILENAME_PATTERN.matcher(fileName);
                if (filenameMatcher.matches()) {
                    try {
                        String targetFileName = "ing_" + fileName.substring(0, 18) + "_" + flipDateIng(filenameMatcher.group(1)) + "_" + flipDateIng(filenameMatcher.group(2)) + ".csv";
                        Path targetPath = ingInDir.resolve(targetFileName);
                        System.out.println("Moving " + path + " to " + targetPath);
                        Files.move(path, targetPath);
                    } catch (IOException e) {
                        System.out.println("Could not move file " + path + " to " + ingInDir + ": " + e);
                    }
                    return;
                }

                // BUNQ
                filenameMatcher = BUNQ_FILENAME_PATTERN.matcher(fileName);
                if (filenameMatcher.matches()) {
                    try {
                        String accountCode = getBunqAccountCode(path);
                        String targetFileName = "bunq_" + accountCode + "_" + filenameMatcher.group(1) + "_" + filenameMatcher.group(2) + ".csv";
                        Path targetPath = bunqInDir.resolve(targetFileName);
                        System.out.println("Moving " + path + " to " + targetPath);
                        Files.move(path, targetPath);
                    } catch (IOException e) {
                        System.out.println("Could not move file " + path + " to " + ingInDir + ": " + e);
                    }
                    return;
                }

                // REVOLUT
                filenameMatcher = REVOLUT_FILENAME_PATTERN.matcher(fileName);
                if (filenameMatcher.matches()) {
                    try {
                        String targetFileName = "revolut_" + fileName.substring(18);
                        Path targetPath = revolutInDir.resolve(targetFileName);
                        System.out.println("Moving " + path + " to " + targetPath);
                        Files.move(path, targetPath);
                    } catch (IOException e) {
                        System.out.println("Could not move file " + path + " to " + ingInDir + ": " + e);
                    }
                    return;
                }

                // ABN
                if (fileName.startsWith("MT940") && fileName.endsWith(".STA")) {
                    System.out.println("abn = " + path);
                    try {
                        Files.move(path, abnInDir.resolve("abn_" + path.getFileName()));
                    } catch (IOException e) {
                        System.out.println("Could not move file " + path + " to " + abnInDir + ": " + e);
                    }
                }
            });
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Extract account code as IBAN from the second line
     */
    private String getBunqAccountCode(Path path) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line = "";
            for (int i = 0; i < 2; i++) {
                line = reader.readLine();
                // empty or no record files should be ok at this stage
                if (line == null) {
                    return "";
                }
            }
            var separator = line.contains("\";\"") ? "\";\"" : "\",\"";
            // IBAN is the 4th field of csv
            return line.split(separator)[3];
        }
    }

    private String flipDateIng(String dateString) {
        String[] parts = dateString.split("-");
        return parts[2] + "-" + parts[1] + "-" + parts[0];
    }
}
