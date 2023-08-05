package com.inmotionchat.core.models;

public class TimeOfDay {

    private int hour; // 24-hour clock

    private int minute;

    public TimeOfDay() {}

    public TimeOfDay(int hour, int minute) throws IllegalStateException {
        this.hour = hour;
        this.minute = minute;
        this.validate();
    }

    public TimeOfDay(String value) throws IllegalStateException, NumberFormatException {
        String[] parts = value.split(":");

        if (parts.length != 2)
            throw new IllegalStateException("Value provided is not of correct format.");

        this.hour = Integer.parseInt(parts[0]);
        this.minute = Integer.parseInt(parts[1]);
        this.validate();
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean isAfter(TimeOfDay other) {
        if (this.hour > other.getHour()) {
            return true;
        } else if (this.hour == other.getHour()) {
            return this.minute > other.getMinute();
        } else {
            return false;
        }
    }

    public boolean isBefore(TimeOfDay other) {
        return !this.equals(other) && !this.isAfter(other);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TimeOfDay other) {
            return other.getMinute() == minute && other.getHour() == hour;
        }
        return false;
    }

    @Override
    public String toString() {
        if (minute < 10) {
            return hour + ":0" + minute;
        }
        return hour + ":" + minute;
    }

    public void validate() throws IllegalStateException {
        if (hour < 0 || hour > 23) {
            throw new IllegalStateException("Hour cannot be less than 0 or greater than 23.");
        }

        if (minute < 0 || minute > 59) {
            throw new IllegalStateException("Minute cannot be less than 0 or greater than 59.");
        }
    }

}
