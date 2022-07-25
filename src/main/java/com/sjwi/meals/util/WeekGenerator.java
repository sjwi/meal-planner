/* (C)2022 https://stephenky.com */
package com.sjwi.meals.util;

import com.sjwi.meals.model.Week;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WeekGenerator {

  public final DayOfWeek weekStartDay;

  public static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();
  public static final int FUTURE_WEEKS = 10;

  public WeekGenerator(int startDay) {
    weekStartDay = DayOfWeek.of(startDay);
  }

  public List<Week> getWeeksForSelect(List<Week> weeks) {
    List<Week> combinedWeeks = new ArrayList<>(weeks);
    List<Week> futureWeeks = getXNumberOfWeeks(FUTURE_WEEKS);
    Set<String> existingWeekKeys =
        combinedWeeks.stream().map(w -> w.getKey()).collect(Collectors.toSet());
    futureWeeks.forEach(
        w -> {
          if (!existingWeekKeys.contains(w.getKey())) combinedWeeks.add(w);
        });
    combinedWeeks.sort((w1, w2) -> w2.getStart().compareTo(w1.getStart()));
    return combinedWeeks;
  }

  private List<Week> getXNumberOfWeeks(int number) {
    return revRange(-1, number)
        .mapToObj(i -> getWeekNWeeksInTheFuture(i))
        .collect(Collectors.toList());
  }

  private Week getWeekNWeeksInTheFuture(int number) {
    LocalDate now = LocalDate.now();
    LocalDate nextFriday = now.with(TemporalAdjusters.next(weekStartDay));
    return new Week(
        Date.from(nextFriday.plusDays(7 * number).atStartOfDay(DEFAULT_ZONE_ID).toInstant()),
        Date.from(nextFriday.plusDays(7 * number + 7).atStartOfDay(DEFAULT_ZONE_ID).toInstant()));
  }

  private IntStream revRange(int from, int to) {
    return IntStream.range(from, to).map(i -> to - i + from - 1);
  }
}
