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


    public Teacher addTeacher(Teacher newTeacher) {

        //  בדיקת תעודת זהות (אורך ותקינות מספרים)
        String idStr = String.valueOf(newTeacher.getId());
        if (idStr == null || idStr.length() != 9 || !idStr.matches("\\d+")) {
            throw new RuntimeException("שגיאה: תעודת זהות חייבת להכיל בדיוק 9 ספרות");
        }

        //  בדיקת שם (לוודא שלא ריק וארוך מספיק)
        if (newTeacher.getFirstName() == null || newTeacher.getFirstName().trim().length() < 2) {
            throw new RuntimeException("שגיאה: שם פרטי חייב להכיל לפחות 2 תווים");
        }
        if (newTeacher.getLastName() == null || newTeacher.getLastName().trim().length() < 2) {
            throw new RuntimeException("שגיאה: שם משפחה חייב להכיל לפחות 2 תווים");
        }

        //  בדיקת סיסמה (אורך מינימלי לבטיחות)
        if (newTeacher.getPassword() == null || newTeacher.getPassword().length() < 6) {
            throw new RuntimeException("שגיאה: סיסמה חייבת להכיל לפחות 6 תווים");
        }

        //  בדיקת כפילות תעודת זהות (מורה)
        if (teacherRepository.existsById(newTeacher.getId())) {
            throw new RuntimeException("שגיאה: מורה עם תעודת זהות זו כבר קיים במערכת");
        }

        //  בדיקת כפילות תעודת זהות (תלמידה)
        if (studentRepository.existsById(newTeacher.getId())) {
            throw new RuntimeException("שגיאה: תעודת זהות זו כבר רשומה כתלמידה במערכת");
        }

        //  בדיקת כפילות כיתה (למנוע מצב של שתי מורות לאותה כיתה)
        if (teacherRepository.existsByClassroom(newTeacher.getClassroom().trim())) {
            throw new RuntimeException("שגיאה: כבר קיימת מורה המוגדרת לכיתה " + newTeacher.getClassroom());
        }

        // בדיקת פורמט כיתה (אות אחת בין א-ת ולאחריה ספרה בין 1-9)
        if (newTeacher.getClassroom() == null || !newTeacher.getClassroom().trim().matches("^[א-ח][1-9]$")) {
            throw new RuntimeException("שגיאה: פורמט כיתה לא תקין. יש להזין אות וספרה (לדוגמה: א1, ב2)");
        }

        // ניקוי רווחים מיותרים לפני השמירה
        newTeacher.setFirstName(newTeacher.getFirstName().trim());
        newTeacher.setLastName(newTeacher.getLastName().trim());
        newTeacher.setClassroom(newTeacher.getClassroom().trim());

        return teacherRepository.save(newTeacher);
    }


    public Teacher updateTeacher(Teacher teacher) {
        if (teacherRepository.existsById(teacher.getId())) {
            return teacherRepository.save(teacher);
        } else {
            throw new EntityNotFoundException("Cannot update. Teacher not found.");
        }
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    public void deleteTeacher(String id) {
        if (teacherRepository.existsById(id)) {
            teacherRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Cannot delete. Teacher with ID " + id + " not found.");
        }
    }

    public Teacher getTeacherById(String id) {
        return teacherRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Teacher with ID " + id + " not found."));
    }

    public boolean login(String id, String password) {
        return teacherRepository.findById(id)
                .map(teacher -> teacher.getPassword().equals(password))
                .orElse(false);
    }

    public List<Student> getMyStudents(String teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("teacher not found"));

        String classroomName = teacher.getClassroom();

        return studentRepository.findByClassroom(classroomName);
    }

    public Teacher getTeacherIfValid(String id, String password) {
        return teacherRepository.findById(id)
                .filter(teacher -> teacher.getPassword().equals(password))
                .orElse(null);
    }
}