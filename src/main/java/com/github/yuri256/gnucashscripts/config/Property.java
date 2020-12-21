package com.github.yuri256.gnucashscripts.config;

import com.github.yuri256.gnucashscripts.job.ing.model.IngConstants;

public enum Property {
    BASE_DIR("baseDir", null),
    ING_JOB_DIR_NAME("ingDirName", "ing"),
    ING_DESCRIPTION_SKIP_FIELDS("ingDescriptionSkipFields", String.join(",", IngConstants.DEFAULT_REMOVE_FIELDS_KEYS)),
    GNU_CASH_DIR_NAME("gnuCashDirName", "gnuCash"),
    ABN_JOB_DIR_NAME("abnDirName", "abn"),
    DOWNLOADS_DIR("downloadsDir", null);

    private final String code;
    private final String defaultValue;

    Property(String code, String defaultValue) {
        this.code = code;
        this.defaultValue = defaultValue;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
