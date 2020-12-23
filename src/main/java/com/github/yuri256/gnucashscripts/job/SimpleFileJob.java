package com.github.yuri256.gnucashscripts.job;

import java.io.File;
import java.io.IOException;

public class SimpleFileJob {
    private final FileConverter ingFileConverter;

    public SimpleFileJob(FileConverter ingFileConverter) {
        this.ingFileConverter = ingFileConverter;
    }

    public void run(String file) {
        File inFile = new File(file);
        if (!inFile.exists()) {
            throw new RuntimeException("File " + inFile + " not found.");
        }
        File outFile = new File(inFile + ".mt940");
        if (outFile.exists()) {
            System.out.println("Output file " + outFile.getPath() + " exists, will remove it.");
            if (!outFile.delete()) {
                throw new RuntimeException("Could not delete file " + outFile.getPath() + ".");
            }
        }
        try {
            ingFileConverter.apply(inFile.toPath(), outFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Error during run: " + e.getMessage());
        }

        if (outFile.exists()) {
            System.out.println("Created file " + outFile.getPath());
        }
    }
}
