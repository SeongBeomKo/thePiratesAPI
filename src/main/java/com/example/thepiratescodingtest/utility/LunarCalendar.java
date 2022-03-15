package com.example.thepiratescodingtest.utility;

import com.ibm.icu.util.ChineseCalendar;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class LunarCalendar {

    static Set<String> holidaysSet = new HashSet<>();
    public static final int LD_SUNDAY = 7;
    public static final int LD_SATURDAY = 6;
    public static final int LD_MONDAY = 1;


    /**
     * 음력날짜를 양력날짜로 변환
     */
    public String Lunar2Solar(String yyyymmdd) {
        ChineseCalendar cc = new ChineseCalendar();

        if (yyyymmdd == null)
            return null;

        String date = yyyymmdd.trim();
        if (date.length() != 8) {
            if (date.length() == 4)
                date = date + "0101";
            else if (date.length() == 6)
                date = date + "01";
            else if (date.length() > 8)
                date = date.substring(0, 8);
            else
                return null;
        }

        cc.set(ChineseCalendar.EXTENDED_YEAR, Integer.parseInt(date.substring(0, 4)) + 2637);   // 년, year + 2637
        cc.set(ChineseCalendar.MONTH, Integer.parseInt(date.substring(4, 6)) - 1);              // 월, month -1
        cc.set(ChineseCalendar.DAY_OF_MONTH, Integer.parseInt(date.substring(6)));              // 일

        LocalDate solar = Instant.ofEpochMilli(cc.getTimeInMillis()).atZone(ZoneId.of("UTC")).toLocalDate();

        int y = solar.getYear();
        int m = solar.getMonth().getValue();
        int d = solar.getDayOfMonth();

        StringBuilder ret = new StringBuilder();
        ret.append(String.format("%04d", y));
        ret.append(String.format("%02d", m));
        ret.append(String.format("%02d", d));

        return ret.toString();
    }


    public Set<String> holidayArray(String yyyy){
        holidaysSet.clear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        // 양력 휴일
        holidaysSet.add(yyyy+"-01-01");   // 신정
        holidaysSet.add(yyyy+"-03-01");   // 삼일절
        holidaysSet.add(yyyy+"-05-05");   // 어린이날
        holidaysSet.add(yyyy+"-06-06");   // 현충일
        holidaysSet.add(yyyy+"-08-15");   // 광복절
        holidaysSet.add(yyyy+"-10-03");   // 개천절
        holidaysSet.add(yyyy+"-10-09");   // 한글날
        holidaysSet.add(yyyy+"-12-25");   // 성탄절

        // 음력 휴일

        String prev_seol = LocalDate.parse(Lunar2Solar(yyyy+"0101"), formatter).minusDays(1).toString().replace("-","");
        holidaysSet.add(yyyy+prev_seol.substring(4));        // ""
        holidaysSet.add(yyyy+SolarDays(yyyy, "-01-01"));  // 설날
        holidaysSet.add(yyyy+SolarDays(yyyy, "-01-02"));  // ""
        holidaysSet.add(yyyy+SolarDays(yyyy, "-04-08"));  // 석탄일
        holidaysSet.add(yyyy+SolarDays(yyyy, "-08-14"));  // ""
        holidaysSet.add(yyyy+SolarDays(yyyy, "-08-15"));  // 추석
        holidaysSet.add(yyyy+SolarDays(yyyy, "-08-16"));  // ""


        try {
            // 어린이날 대체공휴일 검사 : 어린이날은 토요일, 일요일인 경우 그 다음 평일을 대체공유일로 지정

            int childDayChk = LocalDate.parse(yyyy+"0505", formatter).getDayOfWeek().getValue();
            if(childDayChk == LD_SUNDAY) {      // 일요일
                holidaysSet.add(yyyy+"0506");
            }
            if(childDayChk == LD_SATURDAY) {  // 토요일
                holidaysSet.add(yyyy+"0507");
            }

            // 설날 대체공휴일 검사
            if(LocalDate.parse(Lunar2Solar(yyyy+"-01-01"),formatter).getDayOfWeek().getValue() == LD_SUNDAY) {    // 일
                holidaysSet.add(Lunar2Solar(yyyy+"-01-03"));
            }
            if(LocalDate.parse(Lunar2Solar(yyyy+"-01-01"),formatter).getDayOfWeek().getValue() == LD_MONDAY) {    // 월
                holidaysSet.add(Lunar2Solar(yyyy+"-01-03"));
            }
            if(LocalDate.parse(Lunar2Solar(yyyy+"-01-02"),formatter).getDayOfWeek().getValue() == LD_SUNDAY) {    // 일
                holidaysSet.add(Lunar2Solar(yyyy+"-01-03"));
            }

            // 추석 대체공휴일 검사
            if(LocalDate.parse(Lunar2Solar(yyyy+"-08-14"), formatter).getDayOfWeek().getValue() == LD_SUNDAY) {
                holidaysSet.add(Lunar2Solar(yyyy+"-08-17"));
            }
            if(LocalDate.parse(Lunar2Solar(yyyy+"-08-15"), formatter).getDayOfWeek().getValue() == LD_SUNDAY) {
                holidaysSet.add(Lunar2Solar(yyyy+"-08-17"));
            }
            if(LocalDate.parse(Lunar2Solar(yyyy+"-08-16"), formatter).getDayOfWeek().getValue() == LD_SUNDAY) {
                holidaysSet.add(Lunar2Solar(yyyy+"-08-17"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return holidaysSet;
    }

    private String SolarDays(String yyyy, String date){
        return Lunar2Solar(yyyy+date).substring(4);
    }
}
