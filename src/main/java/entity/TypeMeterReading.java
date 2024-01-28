package entity;

public enum TypeMeterReading {
    COLD_WATER("Холодная вода"),
    HOT_WATER("Горячая вода"),
    HEATING("Отопление");

    private String title;

    TypeMeterReading(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}