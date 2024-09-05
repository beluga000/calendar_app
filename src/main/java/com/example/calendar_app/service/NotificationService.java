package com.example.calendar_app.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

import com.example.calendar_app.model.Calendar;

@Service
public class NotificationService {

    private final CalendarService calendarService;
    private final JavaMailSender mailSender;

    public NotificationService(CalendarService calendarService, JavaMailSender mailSender) {
        this.calendarService = calendarService;
        this.mailSender = mailSender;
    }

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void checkAndSendNotifications() {
        List<Calendar> upcomingEvents = calendarService.getAllCalendars();

        for (Calendar event : upcomingEvents) {
            if (event.isNotifyBeforeEvent()) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime notificationTime = event.getStartTime().minus(event.getNotificationTime());

                if (now.isAfter(notificationTime) && now.isBefore(event.getStartTime())) {
                    sendEmailNotification(event);
                }
            }
        }
    }

    public void scheduleNotification(Calendar event) {
        LocalDateTime notificationTime = event.getStartTime().minus(event.getNotificationTime());
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(notificationTime)) {
            System.out.println("알림이 예약되었습니다: " + event.getTitle() + " - 알림 시간: " + notificationTime);
            
        } else {
            System.out.println("알림을 즉시 전송합니다: " + event.getTitle());
            sendEmailNotification(event);
        }
    }

    private void sendEmailNotification(Calendar event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("user@gmail.com"); 
            message.setSubject("이벤트 알림: " + event.getTitle());
            message.setText("이벤트 " + event.getTitle() + "가 곧 시작됩니다.\n" +
                            "시작 시간: " + event.getStartTime().toString() + "\n" +
                            "설명: " + event.getDescription());
            mailSender.send(message);
            System.out.println("이메일 전송 완료: " + event.getTitle());
        } catch (Exception e) {
            System.err.println("이메일 전송 중 오류 발생: " + e.getMessage());
        }
    }
}