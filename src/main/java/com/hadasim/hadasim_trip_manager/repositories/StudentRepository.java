package com.hadasim.hadasim_trip_manager.repositories;

import com.hadasim.hadasim_trip_manager.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    List<Student> findByClassroom(String classroom);


}


