package com.github.yuri256.gnucashscripts.cli.action;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.impl.revolut.job.RevolutJobFactory;
import picocli.CommandLine;

@CommandLine.Command(name = "revolut", description = "Convert Revolut csv file to MT940 file")
public class Revolut implements Runnable {
    @Override
    public void run() {
        RevolutJobFactory.createCompleteJob(Config.load()).run();
    }
}
