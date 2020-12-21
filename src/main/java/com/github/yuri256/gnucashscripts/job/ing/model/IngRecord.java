package com.github.yuri256.gnucashscripts.job.ing.model;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

public class IngRecord {
    @CsvBindByName(column = "Datum", required = true)
    private String datum;

    @CsvBindByName(column = "Naam / Omschrijving", required = true)
    private String naamOmschrijving; // nameDescription

    @CsvBindByName(column = "Rekening", required = true)
    private String rekening; // account

    @CsvBindByName(column = "Tegenrekening")
    private String tegenRekening; // opposite? account

    @CsvBindByName(column = "Code", required = true)
    private String code; // code: GT, BA, IC

    @CsvCustomBindByName(converter = AfBijAbstractBeanField.class,column = "Af Bij", required = true)
    private AfBij afBij; // out/in

    @CsvBindByName(column = "Bedrag (EUR)", required = true)
    private String bedragEUR; // amount EUR

    @CsvBindByName(column = "MutatieSoort", required = true)
    private String mutatieSoort; // change type: Online bankieren, Betaalautomaat, Incasso

    @CsvBindByName(column = "Mededelingen", required = true)
    private String mededelingen; // statement, notice

    @CsvBindByName(column = "Saldo na mutatie", required = true)
    private String saldoNaMutatie; // saldo after mutation

    @CsvBindByName(column = "Tag")
    private String tag;

    public IngRecord() {
        // deserializer
    }

    public IngRecord(String datum, String naamOmschrijving, String rekening, String tegenRekening, String code, AfBij afBij, String bedragEUR, String mutatieSoort, String mededelingen) {
        this.datum = datum;
        this.naamOmschrijving = naamOmschrijving;
        this.rekening = rekening;
        this.tegenRekening = tegenRekening;
        this.code = code;
        this.afBij = afBij;
        this.bedragEUR = bedragEUR;
        this.mutatieSoort = mutatieSoort;
        this.mededelingen = mededelingen;
    }

    public String getDatum() {
        return datum;
    }

    public String getNaamOmschrijving() {
        return naamOmschrijving;
    }

    public String getRekening() {
        return rekening;
    }

    public String getTegenRekening() {
        return tegenRekening;
    }

    public String getCode() {
        return code;
    }

    public AfBij getAfBij() {
        return afBij;
    }

    public String getBedragEUR() {
        return bedragEUR;
    }

    public String getMutatieSoort() {
        return mutatieSoort;
    }

    public String getMededelingen() {
        return mededelingen;
    }

    public String getSaldoNaMutatie() {
        return saldoNaMutatie;
    }

    public String getTag() {
        return tag;
    }

    public static class AfBijAbstractBeanField extends AbstractBeanField<AfBij> {

        @Override
        protected AfBij convert(String value) throws CsvDataTypeMismatchException {
            if (value == null) {
                return null;
            }
            try {
                return AfBij.valueOf(value);
            } catch (IllegalArgumentException e) {
                throw new CsvDataTypeMismatchException(value, AfBij.class,"Could not convert to enum");
            }
        }

    }
    @Override
    public String toString() {
        return "Record{" +
                "datum='" + datum + '\'' +
                ", naamOmschrijving='" + naamOmschrijving + '\'' +
                ", rekening='" + rekening + '\'' +
                ", tegenRekening='" + tegenRekening + '\'' +
                ", code='" + code + '\'' +
                ", afBij='" + afBij + '\'' +
                ", bedragEUR='" + bedragEUR + '\'' +
                ", mutatieSoort='" + mutatieSoort + '\'' +
                ", mededelingen='" + mededelingen + '\'' +
                '}';
    }
}
