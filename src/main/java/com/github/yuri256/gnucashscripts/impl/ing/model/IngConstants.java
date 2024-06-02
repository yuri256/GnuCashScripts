package com.github.yuri256.gnucashscripts.impl.ing.model;

import java.util.Set;
import java.util.stream.Collectors;

public final class IngConstants {
    public enum NotificationKeys {
        VALUE_DATE("Value date"),
        CARD_SEQUENCE_NO("Card sequence no."),
        TRANSACTION("Transaction"), TERM("Term"),
        CREDITOR_ID("Creditor ID"),
        MANDATE_ID("Mandate ID"),
        REFERENCE("Reference"),
        NAME("Name"),
        DESCRIPTION("Description");;


        private final String originalKey;
        private final String noSpaceKey;


        NotificationKeys(String originalKey) {
            this.originalKey = originalKey;

            String[] split = originalKey.split(" ");
            StringBuilder sb = new StringBuilder();
            for (var part : split) {
                if (!part.isEmpty()) {
                    sb.append(part.substring(0, 1).toUpperCase());
                    if (part.length() > 1) {
                        sb.append(part.substring(1));
                    }
                }
            }
            this.noSpaceKey = sb.toString();
        }

        public String getNoSpaceKey() {
            return noSpaceKey;
        }

        public String getOriginalKey() {
            return originalKey;
        }

    }

    public static final String OUT_TRANSACTION_TYPE = "TransactionType";
    public static final String OUT_COUNTER_PARTY = "Counterparty";
    public static final String OUT_DATE_TIME = "DateTime";
    public static final String OUT_NAME_DESCRIPTION = "NameDescription";
    public static final String OUT_TAG = "Tag";

    public static final String TRANSACTION_TYPE_PAYMENT_TERMINAL = "Payment terminal";
    public static final String TRANSACTION_TYPE_CASH_MACHINE = "Cash machine";

    public static final Set<String> DEFAULT_SKIP_FIELD_KEYS = Set.of(
                    NotificationKeys.REFERENCE,
                    NotificationKeys.CREDITOR_ID,
                    NotificationKeys.MANDATE_ID,
                    NotificationKeys.VALUE_DATE,
                    NotificationKeys.TERM,
                    NotificationKeys.TRANSACTION,
                    NotificationKeys.CARD_SEQUENCE_NO
                    )
            .stream()
            .map(NotificationKeys::getNoSpaceKey)
            .collect(Collectors.toUnmodifiableSet());

    public static final Set<String> DEFAULT_SKIP_KEY_KEYS = Set.of(OUT_NAME_DESCRIPTION, NotificationKeys.NAME.getNoSpaceKey(), NotificationKeys.DESCRIPTION.getOriginalKey());
}
