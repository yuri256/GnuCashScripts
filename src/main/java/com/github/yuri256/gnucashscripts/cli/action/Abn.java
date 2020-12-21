package com.github.yuri256.gnucashscripts.cli.action;

import com.github.yuri256.gnucashscripts.config.Config;
import com.github.yuri256.gnucashscripts.job.abn.AbnJob;
import picocli.CommandLine;

@CommandLine.Command(name = "abn", description = "Fix ABN MT940 file so it can be imported by GnuCash")
public class Abn implements Runnable {
    @Override
    public void run() {
        System.out.println("Running ABN converter");
        new AbnJob(Config.load()).run();
    }
}
