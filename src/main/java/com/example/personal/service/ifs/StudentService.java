package com.example.personal.service.ifs;

import com.example.personal.request.StudentRequest;
import com.example.personal.response.StudentResponse;

public interface StudentService {
	
	public StudentResponse addStudId(StudentRequest request);
	
	public StudentResponse chooseCourse(StudentRequest request);
	
	public StudentResponse withdrawCourse(StudentRequest request);
	
	public StudentResponse deleteStudId(StudentRequest request);
}
