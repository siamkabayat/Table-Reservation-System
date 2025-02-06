package de.sbayat.sbtablereservationmanagementsystem.model;

public class DiningTable implements CsvHandling {

    private static final int SPLIT_INDEX_NUMBER       = 0;
    private static final int SPLIT_INDEX_CAPACITY     = 1;
    private static final int SPLIT_INDEX_LOCATION     = 2;

    private int     number;
    private int     capacity;
    private String  location;

    public DiningTable(int number, int capacity, String location) {
        this.number      = number;
        this.capacity    = capacity;
        this.location    = location;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "DiningTable{" +
                "number=" + number +
                ", capacity=" + capacity +
                ", location='" + location + '\'' +
                '}';
    }

    @Override
    public String getAllAttributesAsCsvLine() {
        return getNumber() + CSV_SEPARATOR +
                getCapacity() + CSV_SEPARATOR +
                getLocation() + "\n";
    }

    @Override
    public void setAllAttributesFromCsvLine(String csvLine) {
        String[] allAttributes = csvLine.split(CSV_SEPARATOR);

        setNumber(Integer.parseInt(allAttributes[SPLIT_INDEX_NUMBER]));
        setCapacity(Integer.parseInt(allAttributes[SPLIT_INDEX_CAPACITY]));
        setLocation(allAttributes[SPLIT_INDEX_LOCATION]);

    }
}
