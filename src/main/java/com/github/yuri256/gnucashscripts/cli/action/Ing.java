package com.github.yuri256.gnucashscripts.cli.action;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.impl.ing.job.IngJobFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "ing", description = "Convert ING csv file to MT940 file")
public class Ing implements Runnable {
    @Override
    public void run() {
        IngJobFactory.createCompleteJob(Config.load()).run();
    }
}
