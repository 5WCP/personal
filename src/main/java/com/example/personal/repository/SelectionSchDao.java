package com.example.personal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.personal.entity.SelectionSch;

@Repository
public interface SelectionSchDao extends JpaRepository<SelectionSch, String> {
	
	List<SelectionSch> findByStudId(String studId);
	
	List<SelectionSch> findByCourseCode(String courseCode);
}
