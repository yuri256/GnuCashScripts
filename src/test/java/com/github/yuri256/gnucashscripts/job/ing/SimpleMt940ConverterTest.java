package com.github.yuri256.gnucashscripts.job.ing;

import com.github.yuri256.gnucashscripts.job.common.DescriptionFilterFunction;
import com.github.yuri256.gnucashscripts.job.ing.model.AfBij;
import com.github.yuri256.gnucashscripts.job.ing.model.IngConstants;
import com.github.yuri256.gnucashscripts.job.ing.model.IngRecord;
import com.github.yuri256.gnucashscripts.model.SimpleMt940Record;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class SimpleMt940ConverterTest {
    @Test
    public void testFields() {
        final HashSet<String> removeFieldKeys = new HashSet<>();
        Set<String> removeKeyKeys = Set.of(IngConstants.NAAM_OMSCHRIJVING, IngConstants.NAAM, IngConstants.OMSCHRIJVING);
        DescriptionFilterFunction descriptionFilterFunction = new DescriptionFilterFunction(removeFieldKeys, removeKeyKeys);
        DescriptionConverter descriptionConverter = new DescriptionConverter(descriptionFilterFunction);
        SimpleMt940Record record = new SimpleMt940Converter(descriptionConverter).apply(new IngRecord("20010304", "Test Name-Desc", "TestRekening", "TestTegenRekening", "BA", AfBij.Af, "12,34", "Betaalautomaat", "Pasvolgnr: 001 07-03-2020 09:34 Transactie: A111B2 Term: AB1234 Valutadatum: 07-03-2020"));
        Assertions.assertAll(() -> Assertions.assertEquals("TestRekening", record.getMT940().getField25().getValue()),
                () -> Assertions.assertEquals("010304D12,34NTRFNONREF", record.getMT940().getField61().get(0).getValue()),
                () -> Assertions.assertEquals("Test Name-Desc Datum/Tijd: 07-03-2020 09:34", record.getMT940().getField86().get(0).getValue()));
    }

}