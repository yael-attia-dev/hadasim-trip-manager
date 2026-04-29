package com.hadasim.hadasim_trip_manager.controllers;

import com.hadasim.hadasim_trip_manager.entities.Student;
import com.hadasim.hadasim_trip_manager.entities.StudentLocation;
import com.hadasim.hadasim_trip_manager.locations.StudentLocationJson;
import com.hadasim.hadasim_trip_manager.repositories.LocationRepository;
import com.hadasim.hadasim_trip_manager.repositories.StudentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173") // תוודאי שזה הפורט שבו הריאקט רץ אצלך
public class LocationController {


    private final LocationRepository locationRepository;
    private final StudentRepository studentRepository;

    public LocationController(LocationRepository locationRepository, StudentRepository studentRepository) {
        this.locationRepository = locationRepository;
        this.studentRepository = studentRepository; // <-- 3. להוסיף את ההשמה הזו
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateLocation(@RequestBody StudentLocationJson dto) {


        String lat = dto.getCoordinates().getLatitude().getDegrees() + "°" +
                dto.getCoordinates().getLatitude().getMinutes() + "'" +
                dto.getCoordinates().getLatitude().getSeconds() + "\"";

        String lon = dto.getCoordinates().getLongitude().getDegrees() + "°" +
                dto.getCoordinates().getLongitude().getMinutes() + "'" +
                dto.getCoordinates().getLongitude().getSeconds() + "\"";

        StudentLocation location = new StudentLocation(
                dto.getId(),
                lat,
                lon,
                LocalDateTime.now()
        );

        locationRepository.save(location);

        return ResponseEntity.ok("Location updated successfully for: " + dto.getId());
    }

    @GetMapping("/latest")
    public List<StudentLocation> getAllLatestLocations() {
        List<Student> students = studentRepository.findAll();
        List<StudentLocation> latestLocations = new ArrayList<>();

        for (Student s : students) {
            // אנחנו מבקשים את המיקום הכי חדש לכל סטודנט ספציפי
            StudentLocation lastLoc = locationRepository.findFirstByStudentIdOrderByTimestampDesc(s.getId());
            if (lastLoc != null) {
                latestLocations.add(lastLoc);
            }
        }
        return latestLocations;
    }

    @PostMapping("/locations")
    public ResponseEntity<String> receiveLocation(@RequestBody StudentLocationJson locationJson) {
        // 1. המרה של ה-JSON המורכב לנתונים פשוטים
        String studentId = locationJson.getId();

        // חישוב קו רוחב עשרוני
        double lat = Double.parseDouble(locationJson.getCoordinates().getLatitude().getDegrees()) +
                Double.parseDouble(locationJson.getCoordinates().getLatitude().getMinutes()) / 60.0 +
                Double.parseDouble(locationJson.getCoordinates().getLatitude().getSeconds()) / 3600.0;

        // חישוב קו אורך עשרוני
        double lng = Double.parseDouble(locationJson.getCoordinates().getLongitude().getDegrees()) +
                Double.parseDouble(locationJson.getCoordinates().getLongitude().getMinutes()) / 60.0 +
                Double.parseDouble(locationJson.getCoordinates().getLongitude().getSeconds()) / 3600.0;

        // 2. יצירת אובייקט Entity ושמירה ב-DB
        StudentLocation entity = new StudentLocation(
                studentId,
                String.valueOf(lat),
                String.valueOf(lng),
                LocalDateTime.now()
        );

        locationRepository.save(entity);

        return ResponseEntity.ok("Location received and saved");
    }
}