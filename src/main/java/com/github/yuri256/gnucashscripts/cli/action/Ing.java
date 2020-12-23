package com.github.yuri256.gnucashscripts.cli.action;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.job.ing.IngJob;
import picocli.CommandLine;

@CommandLine.Command(name = "ing", description = "Convert ING csv file to MT940 file")
public class Ing implements Runnable {
    @Override
    public void run() {
        IngJob.create(Config.load()).run();
    }
}
