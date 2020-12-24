package com.github.yuri256.gnucashscripts.cli.action;

import com.github.yuri256.gnucashscripts.impl.ing.job.IngJobFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "ingfile", description = "Convert ING CSV file to MT940 format accepted by GnuCash")
public class IngFile implements Runnable {

    @CommandLine.Option(names = {"-f", "--file"}, description = "File to convert", required = true)
    private String file;

    @Override
    public void run() {
        IngJobFactory.createSimpleFileJob().run(file);
    }

}
