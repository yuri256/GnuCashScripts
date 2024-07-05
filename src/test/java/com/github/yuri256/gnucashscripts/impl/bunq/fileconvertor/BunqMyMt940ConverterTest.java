package com.github.yuri256.gnucashscripts.impl.bunq.fileconvertor;

import com.github.yuri256.gnucashscripts.fileconvertor.DescriptionFilterFunction;
import com.github.yuri256.gnucashscripts.impl.bunq.model.BunqRecord;
import com.github.yuri256.gnucashscripts.impl.ing.fileconvertor.MyMt940Record;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

class BunqMyMt940ConverterTest {
    @Test
    public void testFields() {
        DescriptionFilterFunction descriptionFilterFunction = new DescriptionFilterFunction(new HashSet<>(), new HashSet<>());
        BunqDescriptionConverter descriptionConverter = new BunqDescriptionConverter(descriptionFilterFunction);
        BunqRecord inout = new BunqRecord("2001-03-04", "2001-03-05", "-1,000.00", "BunqAccount", "CountepartyAccount", "Mr. X", "Super purchase");
        MyMt940Record record = new BunqMyMt940Converter(descriptionConverter).apply(inout);
        Assertions.assertAll(() -> Assertions.assertEquals("BunqAccount", record.getMT940().getField25().getValue()),
                () -> Assertions.assertEquals("010304D1000,00NTRFNONREF", record.getMT940().getField61().get(0).getValue()),
                () -> Assertions.assertEquals("Description: Super purchase Name: Mr. X Counterparty: CountepartyAccount", record.getMT940().getField86().get(0).getValue()));
    }
}