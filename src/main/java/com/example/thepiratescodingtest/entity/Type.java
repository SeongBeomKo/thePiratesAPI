package com.example.thepiratescodingtest.entity;

import java.time.LocalDate;

public enum Type {

    FAST(1, "fast"), REGULAR(0, "regular");

    private final String delivery_type;
    private final int value;

    Type(int value, String type) {
        this.delivery_type = type;
        this.value = value;
    }

    public String getDelivery_type() {
        return this.delivery_type;
    }

    public LocalDate getDeliveryDate(boolean beforeClosing) {
        LocalDate now = LocalDate.now();
        int plusDay1 = beforeClosing ? 0 : 1;
        int plusDay2 = value;

        LocalDate startDay = LocalDate.from(now).plusDays(plusDay2);

        //주말
        if(startDay.getDayOfWeek().toString().equals("SUNDAY")) {
            startDay = LocalDate.from(now).plusDays(1);
        } else if(startDay.getDayOfWeek().toString().equals("SATURDAY")) {
            startDay = LocalDate.from(now).plusDays(2);
        }

        //공휴일

        return LocalDate.from(startDay).plusDays(plusDay1);
    }

}
