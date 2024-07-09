package it.unife.cavicchidome.CircoloCulturale.models;

public enum Weekday {
    monday(1),
    tuesday(2),
    wednesday(3),
    thursday(4),
    friday(5),
    saturday(6),
    sunday(7);

    private final int dayNumber;

    Weekday(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public int getDayNumber() {
        return dayNumber;
    }
    // Method to get a Weekday by its day number
    public static Weekday fromDayNumber(int dayNumber) {
        for (Weekday day : Weekday.values()) {
            if (day.getDayNumber() == dayNumber) {
                return day;
            }
        }
        throw new IllegalArgumentException("Invalid day number: " + dayNumber);
    }
}