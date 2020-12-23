package com.github.yuri256.gnucashscripts.cli.action;

import com.github.yuri256.gnucashscripts.job.ing.IngFileConverter;
import com.github.yuri256.gnucashscripts.job.ing.IngJob;
import com.github.yuri256.gnucashscripts.job.ing.model.IngConstants;
import picocli.CommandLine;

import java.io.File;
import java.io.IOException;

@CommandLine.Command(name = "ingfile", description = "Convert ING CSV file to MT940 format accepted by GnuCash")
public class IngFile implements Runnable {

    @CommandLine.Option(names = {"-f", "--file"}, description = "File to convert", required = true)
    private String file;

    @Override
    public void run() {
        File inFile = new File(this.file);
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
            new IngFileConverter(IngConstants.DEFAULT_REMOVE_FIELDS_KEYS).apply(inFile.toPath(), outFile.toPath());
        } catch (IOException e) {
            throw new RuntimeException("Error during run: " + e.getMessage());
        }

        if (outFile.exists()) {
            System.out.println("Created file " + outFile.getPath());
        }
    }
}
