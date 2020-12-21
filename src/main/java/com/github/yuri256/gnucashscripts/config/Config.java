package com.github.yuri256.gnucashscripts.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public final class Config {

    private final Properties props;

    private Config() {
        String userHome = System.getProperty("user.home");
        if (userHome == null) {
            throw new RuntimeException("Could not determine home directory");
        }

        File homeDir = new File(userHome);
        if (!homeDir.exists()) {
            throw new RuntimeException("Home directory " + userHome + " does not exist");
        }

        File configDir = new File(homeDir, ".config");
        if (!configDir.exists()) {
            throw new RuntimeException("Directory " + configDir + " does not exist");
        }

        File gnuCashScriptsDir = new File(configDir, "gnuCashScripts");
        if (!gnuCashScriptsDir.exists()) {
            throw new RuntimeException("Directory " + gnuCashScriptsDir + " does not exist");
        }

        File configFile = new File(gnuCashScriptsDir, "gnuCashScripts.conf");
        if (!configFile.exists()) {
            throw new RuntimeException("Config file " + configFile + " does not exist");
        }

        props = new Properties();
        try (FileReader reader = new FileReader(configFile)) {
            props.load(reader);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Config file " + configFile + " does not exist");
        } catch (IOException e) {
            throw new RuntimeException("Error reading config file", e);
        }
    }

    public static Config load() {
        return new Config();
    }

    public String get(Property key) {
        String value = props.getProperty(key.getCode());

        if (value == null || value.trim().length() == 0) {
            return key.getDefaultValue();
        }
        return value.trim();
    }
}
