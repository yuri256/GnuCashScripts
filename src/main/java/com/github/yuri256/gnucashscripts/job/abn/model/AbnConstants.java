package com.github.yuri256.gnucashscripts.job.abn.model;

import java.util.Collections;
import java.util.Set;

public class AbnConstants {
    public static final Set<String> DEFAULT_REMOVE_FIELD_KEYS = Collections.unmodifiableSet(Set.of("BIC"));
    public static final Set<String> DEFAULT_REMOVE_KEY_KEYS = Collections.unmodifiableSet(Set.of());
    public static final Set<String> KNOWN_FIELDS_WITH_SPACE = Collections.unmodifiableSet(Set.of());
}
