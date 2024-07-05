package com.github.yuri256.gnucashscripts.impl.bunq.fileconvertor;

import com.github.yuri256.gnucashscripts.fileconvertor.DescriptionFilterFunction;
import com.github.yuri256.gnucashscripts.impl.bunq.model.BunqRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;


class BunqDescriptionConverterTest {

    @Test
    public void testCompleteRecord() {
        BunqRecord record = new BunqRecord("2001-03-04", "2001-03-05", "-1,000.00", "BunqAccount", "CountepartyAccount", "Mr. X", "Super purchase");
        String description = createConverter().apply(record);
        Assertions.assertEquals("Description: Super purchase Name: Mr. X Counterparty: CountepartyAccount", description);
    }

    @Test
    public void testNoDescription() {
        BunqRecord record = new BunqRecord("2001-03-04", "2001-03-05", "-1,000.00", "BunqAccount", "CountepartyAccount", "Mr. X", null);
        String description = createConverter().apply(record);
        Assertions.assertEquals("Name: Mr. X Counterparty: CountepartyAccount", description);
    }

    @Test
    public void testNoCounterparty() {
        BunqRecord record = new BunqRecord("2001-03-04", "2001-03-05", "-1,000.00", "BunqAccount", null, "Mr. X", "Super purchase");
        String description = createConverter().apply(record);
        Assertions.assertEquals("Description: Super purchase Name: Mr. X", description);
    }

    @Test
    public void testNameAlsoInTheBeginningOfTheDescription() {
        BunqRecord record = new BunqRecord("2001-03-04", "2001-03-05", "-1,000.00", "BunqAccount", "CountepartyAccount", "Mr. X", "Mr. X Super purchase");
        String description = createConverter().apply(record);
        Assertions.assertEquals("Description: Mr. X Super purchase Counterparty: CountepartyAccount", description);
    }


    private BunqDescriptionConverter createConverter() {
        DescriptionFilterFunction filterFunction = new DescriptionFilterFunction(new HashSet<>(), new HashSet<>());
        return new BunqDescriptionConverter(filterFunction);
    }

}