package com.github.yuri256.gnucashscripts.impl.bunq.fileconvertor;

import com.github.yuri256.gnucashscripts.fileconvertor.DescriptionFilterFunction;
import com.github.yuri256.gnucashscripts.impl.bunq.model.BunqRecord;

import java.util.function.Function;

public class BunqDescriptionConverter implements Function<BunqRecord, String> {

    private final DescriptionFilterFunction descriptionFilterFunction;

    public BunqDescriptionConverter(DescriptionFilterFunction descriptionFilterFunction) {
        this.descriptionFilterFunction = descriptionFilterFunction;
    }

    @Override
    public String apply(BunqRecord record) {
        String description = createDescription(record);

        // do final filtering
        return descriptionFilterFunction.apply(description);
    }

    private String createDescription(BunqRecord record) {
        StringBuilder sb = new StringBuilder();
        boolean added = addNullable("Description", record.getDescription(), sb, false);
        if (!(record.getDescription() != null && record.getDescription().startsWith(record.getName()))) {
            added = addNullable("Name", record.getName(), sb, added) || added;
        }
        addNullable("Counterparty", record.getCounterparty(), sb, added);
        return sb.toString();
    }

    private boolean addNullable(String key, String value, StringBuilder sb, boolean added) {
        if (value == null) {
            return false;
        }
        if (added) {
            sb.append(" ");
        }
        sb.append(key).append(": ").append(value);
        return true;
    }

}
