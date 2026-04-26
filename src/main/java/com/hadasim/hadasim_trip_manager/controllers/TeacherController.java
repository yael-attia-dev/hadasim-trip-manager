package com.hadasim.hadasim_trip_manager.controllers;

import com.hadasim.hadasim_trip_manager.entities.Teacher;
import com.hadasim.hadasim_trip_manager.services.TeacherService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@CrossOrigin
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
    public Teacher addTeacher(@RequestBody Teacher teacher) {
        return teacherService.addTeacher(teacher);
    }

    /**
     * Update an existing teacher
     */
    @PutMapping
    public Teacher updateTeacher(@RequestBody Teacher teacher) {
        return teacherService.updateTeacher(teacher);
    }

    /**
     * Delete a teacher
     */
    @DeleteMapping("/{id}")
    public void deleteTeacher(@PathVariable String id) {
        teacherService.deleteTeacher(id);
    }
}