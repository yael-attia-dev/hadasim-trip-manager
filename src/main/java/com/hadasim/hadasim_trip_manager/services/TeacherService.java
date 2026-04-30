package com.hadasim.hadasim_trip_manager.services;

import com.hadasim.hadasim_trip_manager.entities.Student;
import com.hadasim.hadasim_trip_manager.repositories.StudentRepository;
import com.hadasim.hadasim_trip_manager.entities.Teacher;
import com.hadasim.hadasim_trip_manager.repositories.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public TeacherService(TeacherRepository teacherRepository, StudentRepository studentRepository) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    /**
     * this method adds a new teacher
     * it checks if the teacher ID already exists to avoid overwriting data
     */
    public Teacher addTeacher(Teacher newTeacher) {
        // 1. בדיקה אם תעודת הזהות קיימת כבר בטבלת המורות

        String idStr = String.valueOf(newTeacher.getId());
        if (idStr.length() != 9) {
            throw new RuntimeException("שגיאה: תעודת זהות חייבת להכיל בדיוק 9 ספרות");
        }

        if (teacherRepository.existsById(newTeacher.getId())) {
            throw new RuntimeException("שגיאה: משתמש עם תעודת זהות זו כבר קיים במערכת (מורה)");
        }

        // 2. בדיקה אם תעודת הזהות קיימת בטבלת התלמידות
        if (studentRepository.existsById(newTeacher.getId())) {
            throw new RuntimeException("שגיאה: תעודת זהות זו כבר רשומה כתלמידה");
        }

        // 3. בדיקה שאין עוד מורה באותה כיתה (לפי הבקשה הקודמת שלך)
        if (teacherRepository.existsByClassroom(newTeacher.getClassroom())) {
            throw new RuntimeException("שגיאה: כבר קיימת מורה שהוגדרה לכיתה " + newTeacher.getClassroom());
        }

        // אם הכל תקין - שומרים
        return teacherRepository.save(newTeacher);
    }

    /**
     * this method updates an existing teacher
     * it only works if the teacher ID is already in the database
     */
    public Teacher updateTeacher(Teacher teacher) {
        // check if teacher exists before updating
        if (teacherRepository.existsById(teacher.getId())) {
            return teacherRepository.save(teacher);
        } else {
            throw new EntityNotFoundException("Cannot update. Teacher not found.");
        }
    }

    /**
     * this method returns a list of all teachers from the database
     */
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }


    /**
     * this method deletes a teacher by their ID
     * it checks if the teacher exists before trying to delete
     */
    public void deleteTeacher(String id) {
        //check if the teacher exists
        if (teacherRepository.existsById(id)) {
            //delete the teacher
            teacherRepository.deleteById(id);
        } else {
            //throw an error if not found
            throw new EntityNotFoundException("Cannot delete. Teacher with ID " + id + " not found.");
        }
    }

    /**
     * this method returns a specific teacher by id
     */
    public Teacher getTeacherById(String id) {
        // we search for the teacher. findById returns an "Optional" container.
        return teacherRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Teacher with ID " + id + " not found."));
    }

    /* check if teacher exists and password matches */
    public boolean login(String id, String password) {
        /* find the teacher by id */
        return teacherRepository.findById(id)
                .map(teacher -> teacher.getPassword().equals(password))
                .orElse(false); /* return false if teacher not found or password wrong */
    }


    /* get all students for a specific teacher based on her classroom */
    public List<Student> getMyStudents(String teacherId) {
        /*  find the teacher */
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("teacher not found"));

        /*  get her classroom name */
        String classroomName = teacher.getClassroom();

        /*  return all students in that classroom from the student repository */
        return studentRepository.findByClassroom(classroomName);
    }

    public Teacher getTeacherIfValid(String id, String password) {
        // 1. מחפשים את המורה ב-DB לפי ה-ID
        // נשתמש ב-Optional כי יכול להיות שהמורה לא קיימת
        return teacherRepository.findById(id)
                .filter(teacher -> teacher.getPassword().equals(password)) // 2. בודקים אם הסיסמה תואמת
                .orElse(null); // 3. אם לא נמצאה מורה או שהסיסמה שגויה - מחזירים null
    }
}
