package com.github.yuri256.gnucashscripts.cli.action;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.impl.bunq.job.BunqJobFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "bunq", description = "Convert BUNQ csv file to MT940 file")
public class Bunq implements Runnable {
    @Override
    public void run() {
        BunqJobFactory.createCompleteJob(Config.load()).run();
    }
}
