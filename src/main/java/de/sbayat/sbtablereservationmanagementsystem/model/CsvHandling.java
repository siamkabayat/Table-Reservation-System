package de.sbayat.sbtablereservationmanagementsystem.model;

/**
 * Stellt das Regelewerk fuer
 * die CSV-Daten verarbeitung dar.
 * Jede Klasse die CSV-Daten generieren
 * oder extrahieren moechte muss dieses
 * Interface implementieren.
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

