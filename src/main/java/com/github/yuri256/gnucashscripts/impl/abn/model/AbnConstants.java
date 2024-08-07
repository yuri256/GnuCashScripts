package com.github.yuri256.gnucashscripts.impl.abn.model;

import java.util.Collections;
import java.util.Set;

public class AbnConstants {
    public static final Set<String> DEFAULT_SKIP_FIELD_KEYS = Collections.unmodifiableSet(Set.of("BIC", "MARF", "EREF", "CSID"));
    public static final Set<String> DEFAULT_SKIP_KEY_KEYS = Collections.unmodifiableSet(Set.of("REMI", "NAME", "TRTP"));
    public static final Set<String> KNOWN_KEYS_WITH_SPACE = Collections.unmodifiableSet(Set.of());
}
