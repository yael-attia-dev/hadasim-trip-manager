package com.hadasim.hadasim_trip_manager.services;

import com.hadasim.hadasim_trip_manager.entities.Student;
import com.hadasim.hadasim_trip_manager.repositories.StudentRepository;
import com.hadasim.hadasim_trip_manager.repositories.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public StudentService(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;

    }

    public Student addStudent(Student newStudent) {

        //  בדיקת תעודת זהות (אורך ותקינות מספרים בלבד)
        String idStr = String.valueOf(newStudent.getId());
        if (idStr == null || idStr.length() != 9 || !idStr.matches("\\d+")) {
            throw new RuntimeException("שגיאה: תעודת זהות חייבת להכיל בדיוק 9 ספרות");
        }

        // בדיקת שם (לוודא שלא ריק וארוך מספיק)
        if (newStudent.getFirstName() == null || newStudent.getFirstName().trim().length() < 2) {
            throw new RuntimeException("שגיאה: שם פרטי חייב להכיל לפחות 2 תווים");
        }
        if (newStudent.getLastName() == null || newStudent.getLastName().trim().length() < 2) {
            throw new RuntimeException("שגיאה: שם משפחה חייב להכיל לפחות 2 תווים");
        }

        // בדיקת פורמט כיתה (אות א-ת וספרה 1-9, בדיוק כמו אצל המורה)
        if (newStudent.getClassroom() == null || !newStudent.getClassroom().trim().matches("^[א-ח][1-9]$")) {
            throw new RuntimeException("שגיאה: פורמט כיתה לא תקין. יש להזין אות וספרה (לדוגמה: א1, ב2)");
        }

        // בדיקה אם התלמידה כבר קיימת במערכת
        if (studentRepository.existsById(newStudent.getId())) {
            throw new RuntimeException("שגיאה: תלמידה עם תעודת זהות זו כבר רשומה במערכת");
        }

        // מניעת כפילות תפקידים (בדיקה אם הת"ז רשומה כמורה
        if (teacherRepository.existsById(newStudent.getId())) {
            throw new RuntimeException("שגיאה: תעודת זהות זו כבר רשומה כמורה במערכת");
        }

        // ניקוי רווחים מיותרים לפני השמירה (סטנדרטיזציה של הדאטה)
        newStudent.setFirstName(newStudent.getFirstName().trim());
        newStudent.setLastName(newStudent.getLastName().trim());
        newStudent.setClassroom(newStudent.getClassroom().trim());

        return studentRepository.save(newStudent);
    }

    public Student updateStudent(Student student) {

        if (studentRepository.existsById(student.getId())) {
            return studentRepository.save(student);
        } else {
            throw new EntityNotFoundException("Cannot update. Student not found.");
        }
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }


    public void deleteStudent(String id) {

        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Cannot delete. Student with ID " + id + " not found.");
        }
    }

    public Student getStudentById(String id) {

        return studentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Student with ID " + id + " not found."));
    }


    public List<Student> getStudentsByClass(String classroom) {
        return studentRepository.findByClassroom(classroom);
    }
}