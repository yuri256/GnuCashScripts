package com.github.yuri256.gnucashscripts.cli.action;

import com.github.yuri256.gnucashscripts.impl.abn.job.AbnJobFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "abnfile", description = "Convert ABN MT940 file to MT940 file accepted by GnuCash")
public class AbnFile implements Runnable {

    @CommandLine.Option(names = {"-f", "--file"}, description = "File to convert", required = true)
    private String file;

    @Override
    public void run() {
        AbnJobFactory.createSimpleFileJob().run(file);
    }
}
