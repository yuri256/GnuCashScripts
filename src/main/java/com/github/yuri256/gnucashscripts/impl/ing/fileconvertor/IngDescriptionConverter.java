package com.github.yuri256.gnucashscripts.impl.ing.fileconvertor;

import com.github.yuri256.gnucashscripts.fileconvertor.DescriptionFilterFunction;
import com.github.yuri256.gnucashscripts.impl.ing.model.IngConstants;
import com.github.yuri256.gnucashscripts.impl.ing.model.IngRecord;

import java.util.Objects;
import java.util.StringTokenizer;
import java.util.function.Function;

public class IngDescriptionConverter implements Function<IngRecord, String> {

    private final DescriptionFilterFunction descriptionFilterFunction;

    public IngDescriptionConverter(DescriptionFilterFunction descriptionFilterFunction) {
        this.descriptionFilterFunction = descriptionFilterFunction;
    }

    @Override
    public String apply(IngRecord ingRecord) {
        String description = createDescription(ingRecord);

        // do final filtering
        return descriptionFilterFunction.apply(description);
    }

    private String createDescription(IngRecord ingRecord) {
        StringBuilder sb = new StringBuilder();

        // Replace known keys with spaces with version without spaces. This simplifies further processing a lot.
        // New keys with spaces should be added to the enum
        String notifications = ingRecord.getNotifications().trim();
        notifications = notifications.replaceAll("\\s+", " ");
        for (var key : IngConstants.NotificationKeys.values()) {
            notifications = notifications.replace(key.getOriginalKey() + ":", key.getNoSpaceKey() + ":");
        }

        // Add Name/Description only if there is no Name in Notifications, otherwise it will be duplicated
        if (!notifications.contains(IngConstants.NotificationKeys.NAME.getNoSpaceKey() + ":")) {
            sb.append(IngConstants.OUT_NAME_DESCRIPTION + ": ").append(ingRecord.getNameDescription()).append(" ");
        }

        boolean isTerminalPayment = IngConstants.TRANSACTION_TYPE_PAYMENT_TERMINAL.equals(ingRecord.getTransactionType());
        boolean isCashMachineWithdrawal = IngConstants.TRANSACTION_TYPE_CASH_MACHINE.equals(ingRecord.getTransactionType());
        if (isTerminalPayment || isCashMachineWithdrawal) {
            sb.append(getDescriptionTerminalPayment(notifications));
        } else {
            sb.append(notifications);
        }

        sb.append(" ").append(IngConstants.OUT_COUNTER_PARTY).append(": ").append(ingRecord.getCounterparty());
        sb.append(" ").append(IngConstants.OUT_TRANSACTION_TYPE).append(": ").append(ingRecord.getTransactionType());
        return sb.toString();
    }

    /*
    Terminal payment has specific fields and format that we need to change:
    - Add DateTime as separate filed. The value follows 'CardSequenceNo.'
    - Keep the rest of the fields. Removal of some of them is done in filter function
    */
    private static String getDescriptionTerminalPayment(String description) {
        Objects.requireNonNull(description);
        // Terminal event description, example:
        // CardSequenceNo.: 001 07-03-2020 09:34 Transaction: A111B2 Term: AB1234 ValueDate: 07-03-2020
        // CardSequenceNo.: 001 27-02-2022 11:07 Transaction: 05MABC Term: C108 Valuta: 492,38 SEK Koers: 0,0960555 Opslag: 0,66 EUR ValueDate: 18-08-2021
        StringBuilder sb = new StringBuilder();
        StringTokenizer tokenizer = new StringTokenizer(description, " ");
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token.equals(IngConstants.NotificationKeys.CARD_SEQUENCE_NO.getNoSpaceKey() + ":")) {
                String sequenceNumber = tokenizer.nextToken();
                if (!sequenceNumber.matches("^\\d+$")) {
                    throw new RuntimeException("Card sequence number is not an integer: " + description);
                }
                String date = tokenizer.nextToken();
                if (date.length() != 10) {
                    throw new RuntimeException("Bad date format: " + description);
                }
                date = date.replace("/", "-"); // English version started to use '/' as delimiter
                String time = tokenizer.nextToken();
                if (time.length() != 5) {
                    throw new RuntimeException("Bad time format: " + description);
                }
                sb.append(IngConstants.NotificationKeys.CARD_SEQUENCE_NO.getNoSpaceKey()).append(": ").append(sequenceNumber).append(" ");
                sb.append(IngConstants.OUT_DATE_TIME).append(": ").append(date).append(" ").append(time).append(" ");
            } else {
                sb.append(token).append(" ");
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
