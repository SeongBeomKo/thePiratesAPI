package com.example.thepiratescodingtest.entity;

import com.example.thepiratescodingtest.utility.Holidays;

import java.time.LocalDate;

public enum Type {

    FAST(0, "fast"), REGULAR(1, "regular");

    private final String delivery_type;
    private final int value;

    Type(int value, String type) {
        this.delivery_type = type;
        this.value = value;
    }

    public String getDelivery_type() {
        return this.delivery_type;
    }

    // 배송 타입 기준으로 수령가능한 가장 이른 날짜를 반환
    public LocalDate getDeliveryDate(boolean beforeClosing) {
        LocalDate now = LocalDate.now();
        int plusDay1 = beforeClosing ? 0 : 1;
        int plusDay2 = value;

        // 수령일 = 배송 가능일 + 1
        LocalDate startDay = LocalDate.from(now).plusDays(1);

        // 익일 배송이면 + 1, 당일 배송이면 + 0
        startDay = LocalDate.from(startDay).plusDays(plusDay2);
        //System.out.println(startDay.toString().replaceAll("-", ""));

        // 오늘 날짜기준으로 마감시간 전에 시켰으면 + 0, 마김시간 후에 시켰으면 + 1
        startDay = LocalDate.from(startDay).plusDays(plusDay1);

        //가장 빠르게 수령 가능 한 날짜가 주말이면 (토요일 + 2일 / 일요일 + 1일)
        if(startDay.getDayOfWeek().toString().equals("SUNDAY")) {
            startDay = LocalDate.from(now).plusDays(1);
        } else if(startDay.getDayOfWeek().toString().equals("SATURDAY")) {
            startDay = LocalDate.from(now).plusDays(2);
          //가장 빠르게 수령 가능 한 날짜가 공휴일이면 + 1일
        } else if(Holidays.holidayArray(String.valueOf(LocalDate.now().getYear()))
                .contains(startDay.toString().replaceAll("-", ""))) {
            startDay = LocalDate.from(now).plusDays(1);
        }

        return startDay;
    }

}
