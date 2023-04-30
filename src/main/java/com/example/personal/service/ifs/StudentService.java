package com.example.personal.service.ifs;

import java.util.List;

import com.example.personal.entity.Student;
import com.example.personal.request.StudentRequest;
import com.example.personal.response.StudentResponse;

public interface StudentService {
	
	public StudentResponse addStudId(StudentRequest request);
	
	public StudentResponse chooseCourse(StudentRequest request);
	
	public StudentResponse withdrawCourse(StudentRequest request);
	
	public StudentResponse deleteStudId(StudentRequest request);
	
	public StudentResponse reviseStudId(StudentRequest request);
	
	public List<Student> getAllStu();
	
	public StudentResponse getStu(StudentRequest request);
	
	public StudentResponse findStuByName(StudentRequest request);
}
