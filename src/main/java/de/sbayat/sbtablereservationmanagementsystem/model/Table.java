package de.sbayat.sbtablereservationmanagementsystem.model;

public class Table implements CsvHandling {

    private static final int SPLIT_INDEX_NUMBER       = 0;
    private static final int SPLIT_INDEX_CAPACITY     = 1;
    private static final int SPLIT_INDEX_AVAILABILITY = 2;
    private static final int SPLIT_INDEX_LOCATION     = 3;

    private int     number;
    private int     capacity;
    private boolean isAvailable;
    private String  location;

    public Table(int number, int capacity, boolean isAvailable, String location) {
        this.number      = number;
        this.capacity    = capacity;
        this.isAvailable = isAvailable;
        this.location    = location;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "Table{" +
                "number=" + number +
                ", capacity=" + capacity +
                ", isAvailable=" + isAvailable +
                ", location='" + location + '\'' +
                '}';
    }

    @Override
    public String getAllAttributesAsCsvLine() {
        return getNumber() + CSV_SEPARATOR +
                getCapacity() + CSV_SEPARATOR +
                isAvailable() + CSV_SEPARATOR +
                getLocation() + "\n";
    }

    @Override
    public void setAllAttributesFromCsvLine(String csvLine) {
        String[] allAttributes = csvLine.split(CSV_SEPARATOR);

        setNumber(Integer.parseInt(allAttributes[SPLIT_INDEX_NUMBER]));
        setCapacity(Integer.parseInt(allAttributes[SPLIT_INDEX_CAPACITY]));

        setAvailable(Boolean.parseBoolean(allAttributes[SPLIT_INDEX_AVAILABILITY]));
        setLocation(allAttributes[SPLIT_INDEX_LOCATION]);

    }

    public boolean canAccommodate(int numberOfGuests) {
        return this.isAvailable && numberOfGuests <= this.capacity;
    }

    public void reserve() {
        this.isAvailable = false;
    }

    public void free() {
        this.isAvailable = true;
    }
}
