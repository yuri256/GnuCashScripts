package com.github.yuri256.gnucashscripts.config;

import com.github.yuri256.gnucashscripts.impl.abn.model.AbnConstants;
import com.github.yuri256.gnucashscripts.impl.ing.model.IngConstants;

public enum Property {
    BASE_DIR("baseDir", null),
    DOWNLOADS_DIR("downloadsDir", null),
    GNU_CASH_DIR_NAME("gnuCashDirName", "gnuCash"),
    ING_JOB_DIR_NAME("ingDirName", "ing"),
    ING_DESCRIPTION_REMOVE_FIELD_KEYS("ingDescriptionSkipFields", String.join(",", IngConstants.DEFAULT_REMOVE_FIELD_KEYS)),
    ING_DESCRIPTION_REMOVE_KEY_KEYS("ingDescriptionSkipKeys", String.join(",", IngConstants.DEFAULT_REMOVE_KEY_KEYS)),
    ABN_JOB_DIR_NAME("abnDirName", "abn"),
    ABN_SKIP_FIELD_KEYS("abnSkipFields", String.join(",", AbnConstants.DEFAULT_REMOVE_FIELD_KEYS)),
    ABN_SKIP_KEY_KEYS("abnSkipKeys", String.join(",", AbnConstants.DEFAULT_REMOVE_KEY_KEYS));

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
