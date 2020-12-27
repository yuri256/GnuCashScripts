package com.github.yuri256.gnucashscripts.impl.ing.model;

import java.util.Collections;
import java.util.Set;

public final class IngConstants {

    public static final String MUTATIESOORT_BETAALAUTOMAAT = "Betaalautomaat";
    public static final String MUTATIESOORT_GELDAUTOMAAT = "Geldautomaat";
    public static final String VALUTADATUM = "Valutadatum:";
    public static final String MUTATIESOORT_OVERSCHRIVING = "Overschrijving";
    public static final String NAAM_OMSCHRIJVING = "NaamOmschrijving";
    public static final String OMSCHRIJVING = "Omschrijving";
    public static final String NAAM = "Naam";
    public static final String DATUM_TIJD = "Datum/Tijd";
    public static final Set<String> DEFAULT_SKIP_FIELD_KEYS = Collections.unmodifiableSet(Set.of("Kenmerk", "Incassant ID", "Machtiging ID"));
    public static final Set<String> DEFAULT_SKIP_KEY_KEYS = Collections.unmodifiableSet(Set.of(NAAM_OMSCHRIJVING, NAAM, OMSCHRIJVING));
    public static final Set<String> KNOWN_FIELDS_WITH_SPACE = Collections.unmodifiableSet(Set.of("Incassant ID", "Machtiging ID"));
}
