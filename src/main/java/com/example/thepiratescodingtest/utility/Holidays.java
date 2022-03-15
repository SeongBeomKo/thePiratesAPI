package com.example.thepiratescodingtest.utility;

import com.ibm.icu.util.ChineseCalendar;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class Holidays {

    static Set<String> holidaysSet = new HashSet<>();
    public static final int LD_SUNDAY = 7;
    public static final int LD_SATURDAY = 6;
    public static final int LD_MONDAY = 1;


    /**
     * 음력날짜를 양력날짜로 변환
     */
    public static String Lunar2Solar(String yyyymmdd) {
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

    // yyyy년도의 공휴일 찾기
    public static Set<String> holidayArray(String yyyy){
        holidaysSet.clear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        // 양력 휴일
        holidaysSet.add(yyyy+"0301");   // 삼일절
        holidaysSet.add(yyyy+"0815");   // 광복절
        holidaysSet.add(yyyy+"1003");   // 개천절
        holidaysSet.add(yyyy+"1009");   // 한글날
        holidaysSet.add(yyyy+"0505");   // 어린이날
        extraHoliday();
        holidaysSet.add(yyyy+"0101");   // 신정
        holidaysSet.add(yyyy+"0606");   // 현충일
        holidaysSet.add(yyyy+"1225");   // 성탄절

        // 음력 휴일

        String prev_seol = LocalDate.parse(Lunar2Solar(yyyy+"0101"), formatter).minusDays(1).toString().replace("-","");
        holidaysSet.add(yyyy+prev_seol.substring(4));        // ""
        holidaysSet.add(yyyy+SolarDays(yyyy, "0101"));  // 설날
        holidaysSet.add(yyyy+SolarDays(yyyy, "0102"));  // ""
        holidaysSet.add(yyyy+SolarDays(yyyy, "0408"));  // 석탄일
        holidaysSet.add(yyyy+SolarDays(yyyy, "0814"));  // ""
        holidaysSet.add(yyyy+SolarDays(yyyy, "0815"));  // 추석
        holidaysSet.add(yyyy+SolarDays(yyyy, "0816"));  // ""


        try {
            // 설날 대체공휴일 검사
            String lastYear = String.valueOf(Integer.parseInt(yyyy) + 1);
            if(LocalDate.parse(Lunar2Solar(lastYear+"1231"),formatter).getDayOfWeek().getValue() == LD_SUNDAY) {    // 일
                holidaysSet.add(Lunar2Solar(yyyy+"0103"));
            }
            if(LocalDate.parse(Lunar2Solar(yyyy+"0101"),formatter).getDayOfWeek().getValue() == LD_MONDAY) {    // 월
                holidaysSet.add(Lunar2Solar(yyyy+"0103"));
            }
            if(LocalDate.parse(Lunar2Solar(yyyy+"0102"),formatter).getDayOfWeek().getValue() == LD_SUNDAY) {    // 일
                holidaysSet.add(Lunar2Solar(yyyy+"0103"));
            }

            // 추석 대체공휴일 검사
            if(LocalDate.parse(Lunar2Solar(yyyy+"0814"), formatter).getDayOfWeek().getValue() == LD_SUNDAY) {
                holidaysSet.add(Lunar2Solar(yyyy+"0817"));
            }
            if(LocalDate.parse(Lunar2Solar(yyyy+"0815"), formatter).getDayOfWeek().getValue() == LD_SUNDAY) {
                holidaysSet.add(Lunar2Solar(yyyy+"0817"));
            }
            if(LocalDate.parse(Lunar2Solar(yyyy+"0816"), formatter).getDayOfWeek().getValue() == LD_SUNDAY) {
                holidaysSet.add(Lunar2Solar(yyyy+"0817"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return holidaysSet;
    }

    private static String SolarDays(String yyyy, String date){
        return Lunar2Solar(yyyy+date).substring(4);
    }

    // 대체 공휴일 확인
    private static void extraHoliday() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        Set<String> extraHoliday = new HashSet<>();

        for(String yyyymmdd : holidaysSet) {
            int holiday = LocalDate.parse(yyyymmdd, formatter).getDayOfWeek().getValue();
            if(yyyymmdd.contains("0505")) { // 어린이날 대체공휴일 검사 : 어린이날은 토요일, 일요일인 경우 그 다음 평일을 대체공유일로 지정
                if (holiday == LD_SUNDAY) {      // 일요일
                    extraHoliday.add(yyyymmdd.substring(0,4) + "0506");
                }
                if (holiday == LD_SATURDAY) {  // 토요일
                    extraHoliday.add(yyyymmdd.substring(0,4)  + "0507");
                }
            } else if (yyyymmdd.contains("0815")) { // 광복절
                if (holiday == LD_SUNDAY) {      // 일요일
                    extraHoliday.add(yyyymmdd.substring(0,4) + "0816");
                }
                if (holiday == LD_SATURDAY) {  // 토요일
                    extraHoliday.add(yyyymmdd.substring(0,4)  + "0817");
                }
            } else if (yyyymmdd.contains("1003")) { // 개천절
                if (holiday == LD_SUNDAY) {      // 일요일
                    extraHoliday.add(yyyymmdd.substring(0,4) + "1004");
                }
                if (holiday == LD_SATURDAY) {  // 토요일
                    extraHoliday.add(yyyymmdd.substring(0,4)  + "1005");
                }
            } else if (yyyymmdd.contains("1009")) { //한글날
                if (holiday == LD_SUNDAY) {      // 일요일
                    extraHoliday.add(yyyymmdd.substring(0,4) + "1010");
                }
                if (holiday == LD_SATURDAY) {  // 토요일
                    extraHoliday.add(yyyymmdd.substring(0,4)  + "1011");
                }
            } else if (yyyymmdd.contains("0301")) { //삼일절
                if (holiday == LD_SUNDAY) {      // 일요일
                    extraHoliday.add(yyyymmdd.substring(0,4) + "0302");
                }
                if (holiday == LD_SATURDAY) {  // 토요일
                    extraHoliday.add(yyyymmdd.substring(0,4)  + "0303");
                }
            }
        }
        holidaysSet.addAll(extraHoliday);
    }

    public static LocalDate weekendOrHoliday(LocalDate startDay) {
        //가장 빠르게 수령 가능 한 날짜가 주말이면 (토요일 + 2일 / 일요일 + 1일)
        if(startDay.getDayOfWeek().toString().equals("SUNDAY")) {
            startDay = LocalDate.from(startDay).plusDays(1);
        } else if(startDay.getDayOfWeek().toString().equals("SATURDAY")) {
            startDay = LocalDate.from(startDay).plusDays(2);

        }
        //가장 빠르게 수령 가능 한 날짜가 공휴일이면 + 1일 (추석/설날 처럼 연달아 쉬는날이 있기 때문에 while loop)
        while(Holidays.holidayArray(String.valueOf(LocalDate.now().getYear()))
                .contains(startDay.toString().replaceAll("-", ""))) {
            startDay = LocalDate.from(startDay).plusDays(1);
        }
        return startDay;
    }
}
