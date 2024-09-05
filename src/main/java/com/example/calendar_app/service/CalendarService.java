package com.example.calendar_app.service;

import com.example.calendar_app.model.Calendar;
import com.example.calendar_app.repository.CalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarService {

    @Autowired
    private CalendarRepository calendarRepository;

    public Calendar createCalendar(Calendar calendar) {
        if (calendar.isRecurring()) {
            List<Calendar> recurringEvents = generateRecurringEvents(calendar);
            calendarRepository.saveAll(recurringEvents);
            return recurringEvents.get(0);  // 첫 번째 일정을 반환
        } else {
            return calendarRepository.save(calendar);
        }
    }

    private List<Calendar> generateRecurringEvents(Calendar calendar) {
        List<Calendar> recurringEvents = new ArrayList<>();
        LocalDateTime currentDateTime = calendar.getStartTime();
        LocalDateTime endDateTime = calendar.getEndTime();

        while (!currentDateTime.isAfter(endDateTime)) {
            Calendar recurringEvent = new Calendar();
            recurringEvent.setTitle(calendar.getTitle());
            recurringEvent.setDescription(calendar.getDescription());
            recurringEvent.setStartTime(currentDateTime);
            recurringEvent.setEndTime(currentDateTime);  // 반복 일정의 종료 시간도 동일하게 설정
            recurringEvent.setRecurring(false);
            recurringEvents.add(recurringEvent);

            // 반복 유형에 따른 날짜 및 시간 증가
            switch (calendar.getRecurrenceType()) {
                case "DAILY":
                    currentDateTime = currentDateTime.plusDays(calendar.getRecurrenceInterval());
                    break;
                case "WEEKLY":
                    currentDateTime = currentDateTime.plusWeeks(calendar.getRecurrenceInterval());
                    break;
                case "MONTHLY":
                    currentDateTime = currentDateTime.plusMonths(calendar.getRecurrenceInterval());
                    break;
                default:
                    throw new IllegalArgumentException("잘못된 반복 유형: " + calendar.getRecurrenceType());
            }
        }

        return recurringEvents;
    }

    public List<Calendar> getAllCalendars() {
        return calendarRepository.findAll();
    }

    public Optional<Calendar> getCalendarById(Long id) {
        return calendarRepository.findById(id);
    }

    public Calendar updateCalendar(Long id, Calendar calendar) {
        if (calendarRepository.existsById(id)) {
            calendar.setId(id);
            return calendarRepository.save(calendar);
        }
        return null;
    }

    public void deleteCalendar(Long id) {
        calendarRepository.deleteById(id);
    }
}