package com.github.yuri256.gnucashscripts.impl.ing.fileconvertor;

import com.github.yuri256.gnucashscripts.fileconvertor.DescriptionFilterFunction;
import com.github.yuri256.gnucashscripts.impl.ing.model.DebitCredit;
import com.github.yuri256.gnucashscripts.impl.ing.model.IngRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class IngMyMt940ConverterTest {
    @Test
    public void testFields() {
        Set<String> removeEntryKeys = Set.of("TransactionType", "Counterparty", "Term", "Transaction", "ValueDate", "CardSequenceNo.");
        Set<String> removeKeyKeys = Set.of("NameDescription", "Name", "Description");
        DescriptionFilterFunction descriptionFilterFunction = new DescriptionFilterFunction(removeEntryKeys, removeKeyKeys);
        IngDescriptionConverter descriptionConverter = new IngDescriptionConverter(descriptionFilterFunction);
        MyMt940Record record = new IngMyMt940Converter(descriptionConverter).apply(new IngRecord("20010304", "Test Name-Desc", "TestRekening", "TestTegenRekening", "BA", DebitCredit.Debit, "12,34", "Payment terminal", "Card sequence no.: 001 07-03-2020 09:34 Transaction: A111B2 Term: AB1234 Value date: 07-03-2020"));
        Assertions.assertAll(() -> Assertions.assertEquals("TestRekening", record.getMT940().getField25().getValue()),
                () -> Assertions.assertEquals("010304D12,34NTRFNONREF", record.getMT940().getField61().get(0).getValue()),
                () -> Assertions.assertEquals("Test Name-Desc DateTime: 07-03-2020 09:34", record.getMT940().getField86().get(0).getValue()));
    }

}