package com.example.thepiratescodingtest.entity;

import com.example.thepiratescodingtest.utility.Holidays;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    public LocalDate getDeliveryDate(boolean beforeClosing, LocalDateTime now) {
        int plusDay1 = beforeClosing ? 0 : 1;
        int plusDay2 = value;

        LocalDate startDay = now.toLocalDate();
        // 현재 요청 당일이 주말 혹은 공휴일 인지 확인
        startDay = Holidays.weekendOrHoliday(startDay);
        // 오늘 날짜기준으로 마감시간 전에 시켰으면 + 0, 마김시간 후에 시켰으면 + 1
        startDay = LocalDate.from(startDay).plusDays(plusDay1);
        // 다시 주말 혹은 공휴일 인지 확인
        startDay = Holidays.weekendOrHoliday(startDay);
        // 익일 배송이면 + 1, 당일 배송이면 + 0
        startDay = LocalDate.from(startDay).plusDays(plusDay2);
        // 다시 주말 혹은 공휴일 인지 확인
        startDay = Holidays.weekendOrHoliday(startDay);
        // 수령일 = 배송 가능일 + 1
        startDay = LocalDate.from(startDay).plusDays(1);


        return startDay;
    }
}
