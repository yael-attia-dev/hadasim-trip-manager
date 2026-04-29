package com.hadasim.hadasim_trip_manager.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;
import com.hadasim.hadasim_trip_manager.entities.Teacher;
import com.hadasim.hadasim_trip_manager.services.TeacherService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin(origins = "http://localhost:5173") // תוודאי שזה הפורט שבו הריאקט רץ אצלך
public class TeacherController {

    private final TeacherService teacherService;

    // Constructor Injection
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    /**
     * Get all teachers
     */
    @GetMapping
    public List<Teacher> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    /**
     * Get teacher by ID
     */
    @GetMapping("/{id}")
    public Teacher getTeacherById(@PathVariable String id) {
        return teacherService.getTeacherById(id);
    }

    /**
     * Add a new teacher
     */
    @PostMapping
    public Teacher addTeacher( @Valid @RequestBody Teacher teacher) {
        return teacherService.addTeacher(teacher);
    }

    /**
     * Update an existing teacher
     */
    @PutMapping
    public Teacher updateTeacher( @Valid @RequestBody Teacher teacher) {
        return teacherService.updateTeacher(teacher);
    }

    /**
     * Delete a teacher
     */
    @DeleteMapping("/{id}")
    public void deleteTeacher(@PathVariable String id) {
        teacherService.deleteTeacher(id);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        String id = loginData.get("id");
        String password = loginData.get("password");

        // במקום רק בוליאני (isValid), נבקש מהסרוויס את אובייקט המורה
        Teacher teacher = teacherService.getTeacherIfValid(id, password);

        if (teacher != null) {
            // מחזירים את כל האובייקט של המורה (כולל השם!)
            return ResponseEntity.ok(teacher);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid id or password");
        }
    }
}
