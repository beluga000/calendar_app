package com.example.calendar_app.controller;

import com.example.calendar_app.model.Calendar;
import com.example.calendar_app.service.CalendarService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.calendar_app.service.NotificationService;



import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/calendars")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    @Operation(summary = "새로운 일정 생성", description = "POST 요청을 통해 새로운 일정을 생성합니다.")
    public ResponseEntity<?> createCalendar(@RequestBody Calendar calendar) {
        if (calendar == null || calendar.getTitle() == null || calendar.getTitle().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("일정 정보가 유효하지 않습니다. 제목이 필요합니다.");
        }
        try {
            // 일정 생성 시 알림 설정 여부 확인
            if (calendar.isNotifyBeforeEvent() && calendar.getNotificationTime() != null) {
                notificationService.scheduleNotification(calendar); // 알림 스케줄링
            }
            
            Calendar createdCalendar = calendarService.createCalendar(calendar);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCalendar);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("일정 생성 중 오류 발생: " + e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "모든 일정 조회", description = "GET 요청을 통해 모든 일정을 조회합니다.")
    public ResponseEntity<?> getAllCalendars() {
        try {
            List<Calendar> calendars = calendarService.getAllCalendars();
            return ResponseEntity.ok(calendars);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("일정 조회 중 오류 발생: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "특정 일정 조회", description = "GET 요청을 통해 특정 ID의 일정을 조회합니다.")
    public ResponseEntity<?> getCalendarById(@PathVariable Long id) {
        try {
            Optional<Calendar> calendar = calendarService.getCalendarById(id);
            return calendar.map(ResponseEntity::ok)
                           .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                           .body(new Calendar())); // 빈 Calendar 객체를 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("일정 조회 중 오류 발생: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "일정 수정", description = "PUT 요청을 통해 특정 ID의 일정을 수정합니다.")
    public ResponseEntity<?> updateCalendar(@PathVariable Long id, @RequestBody Calendar calendar) {
        try {
            Calendar updatedCalendar = calendarService.updateCalendar(id, calendar);
            return updatedCalendar != null ? ResponseEntity.ok(updatedCalendar) 
                                            : ResponseEntity.status(HttpStatus.NOT_FOUND)
                                            .body("ID가 " + id + "인 일정을 찾을 수 없습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("일정 수정 중 오류 발생: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "일정 삭제", description = "DELETE 요청을 통해 특정 ID의 일정을 삭제합니다.")
    public ResponseEntity<?> deleteCalendar(@PathVariable Long id) {
        try {
            calendarService.deleteCalendar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("일정 삭제 중 오류 발생: " + e.getMessage());
        }
    }
}