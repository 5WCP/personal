package com.example.personal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.personal.entity.Student;

@Repository
public interface StudentDao extends JpaRepository<Student, String> {

}
