package com.hadasim.hadasim_trip_manager.entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//all the function from JpaRepository, the object type is student, the key is String.

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
}


