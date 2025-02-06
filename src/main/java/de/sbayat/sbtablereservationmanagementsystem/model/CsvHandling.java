package de.sbayat.sbtablereservationmanagementsystem.model;

/**
 * This interface defines the rules for CSV data processing.
 * Any class that wants to generate or extract CSV data
 * must implement this interface.
 */
public interface CsvHandling {

    //region 0. Konstanten
    String CSV_SEPARATOR = ";";
    //endregion

    //region 1. Methoden und Funktionen
    String getAllAttributesAsCsvLine();
    void setAllAttributesFromCsvLine(String csvLine);
    //endregion
}

