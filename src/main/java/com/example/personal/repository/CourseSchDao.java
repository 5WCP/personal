package com.example.personal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.personal.entity.CourseSch;

@Repository
public interface CourseSchDao extends JpaRepository<CourseSch, String> {

	List<CourseSch> findByCourseName(String courseName);
}
