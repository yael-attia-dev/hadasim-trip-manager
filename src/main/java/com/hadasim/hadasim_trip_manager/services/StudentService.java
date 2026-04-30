package com.hadasim.hadasim_trip_manager.services;

import com.hadasim.hadasim_trip_manager.entities.Student;
import com.hadasim.hadasim_trip_manager.repositories.StudentRepository;
import com.hadasim.hadasim_trip_manager.repositories.TeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService
{

    private final StudentRepository studentRepository;
    private  final TeacherRepository teacherRepository;

    public StudentService(StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;

    }
    /**
     * this method adds a new student.
     * it checks if the student ID already exists to avoid overwriting data.
     */
    public Student addStudent(Student newStudent) {
        // 1. בדיקת אורך תעודת זהות (בדיוק 9 ספרות)
        String idStr = String.valueOf(newStudent.getId());
        if (idStr.length() != 9) {
            throw new RuntimeException("שגיאה: תעודת זהות חייבת להכיל בדיוק 9 ספרות");
        }

        // 2. בדיקה אם הת"ז כבר קיימת בטבלת התלמידות
        if (studentRepository.existsById(newStudent.getId())) {
            throw new RuntimeException("שגיאה: תלמידה עם תעודת זהות זו כבר רשומה במערכת");
        }

        // 3. בדיקה אם הת"ז רשומה כבר כמורה (מניעת כפילות תפקידים)
        if (teacherRepository.existsById(newStudent.getId())) {
            throw new RuntimeException("שגיאה: תעודת זהות זו כבר רשומה כמורה במערכת");
        }

        return studentRepository.save(newStudent);
    }

    /**
     * this method updates an existing student.
     * it only works if the student ID is already in the database.
     */
    public Student updateStudent(Student student) {
        // check if student exists before updating
        if (studentRepository.existsById(student.getId())) {
            return studentRepository.save(student);
        } else {
            throw new EntityNotFoundException("Cannot update. Student not found.");
        }
    }

    /**
     * this method returns a list of all students from the database.
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }


    /**
     * this method deletes a student by their ID.
     * it checks if the student exists before trying to delete.
     */
    public void deleteStudent(String id) {
        //check if the student exists
        if (studentRepository.existsById(id)) {
            //delete the student
            studentRepository.deleteById(id);
        } else {
            //throw an error if not found
            throw new EntityNotFoundException("Cannot delete. Student with ID " + id + " not found.");
        }
    }

    /**
     * this method returns a specific student by id.
     */
    public Student getStudentById(String id) {
        // we search for the student. findById returns an "Optional" container.
        return studentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Student with ID " + id + " not found."));
    }

    /* get students by class name */
    public List<Student> getStudentsByClass(String classroom) {
        return studentRepository.findByClassroom(classroom);
    }
}