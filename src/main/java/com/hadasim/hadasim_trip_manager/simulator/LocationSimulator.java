package com.hadasim.hadasim_trip_manager.simulator;

import com.hadasim.hadasim_trip_manager.entities.Student;
import com.hadasim.hadasim_trip_manager.locations.Coordinates;
import com.hadasim.hadasim_trip_manager.locations.DMSLocation;
import com.hadasim.hadasim_trip_manager.locations.StudentLocationJson;
import com.hadasim.hadasim_trip_manager.repositories.StudentRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class LocationSimulator {

    private final RestTemplate restTemplate = new RestTemplate();
    private final StudentRepository studentRepository;

    // הזרקת ה-Repository של התלמידות כדי שנדע למי לשלוח מיקומים
    public LocationSimulator(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // ירוץ כל 60000 מילי-שניות (דקה אחת)
    @Scheduled(fixedRate = 60000)
    public void sendLocationsForAllStudents() {
        // 1. שליפת כל התלמידות הרשומות במערכת
        List<Student> allStudents = studentRepository.findAll();

        if (allStudents.isEmpty()) {
            System.out.println("No students found in DB. Skipping simulation...");
            return;
        }

        System.out.println("--- Starting location simulation for " + allStudents.size() + " students ---");

        for (Student student : allStudents) {

            // עבור קווי רוחב (lat): ננוע בין דקה 45 ל-48 (טווח של כ-5 ק"מ סה"כ)
            String randomMinutesLat = String.valueOf(45 + (int)(Math.random() * 4));
            String randomSecondsLat = String.valueOf((int)(Math.random() * 60));
            DMSLocation lat = new DMSLocation("31", randomMinutesLat, randomSecondsLat);

            // עבור קווי אורך (lng): ננוע בין דקה 12 ל-15
            String randomMinutesLng = String.valueOf(12 + (int)(Math.random() * 4));
            String randomSecondsLng = String.valueOf((int)(Math.random() * 60));
            DMSLocation lng = new DMSLocation("35", randomMinutesLng, randomSecondsLng);

            Coordinates coords = new Coordinates(lng, lat);

            // 3. בניית ה-JSON לפי המבנה שדרשנו ב-Controller
            StudentLocationJson locationJson = new StudentLocationJson(
                    student.getId(), // משתמש ב-ID האמיתי מה-DB
                    coords,
                    LocalDateTime.now().toString()
            );

            // 4. שליחה ב-POST ל-Endpoint שיצרנו ב-LocationController
            try {
                restTemplate.postForEntity("http://localhost:8080/api/locations", locationJson, String.class);
                System.out.println("Sent location for: " + student.getFirstName() + " " + student.getLastName());
            } catch (Exception e) {
                System.err.println("Failed to send location for student " + student.getId() + ": " + e.getMessage());
            }
        }

        System.out.println("--- Simulation cycle completed ---");
    }
}