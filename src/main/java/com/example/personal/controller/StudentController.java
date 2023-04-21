package com.example.personal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.personal.request.StudentRequest;
import com.example.personal.response.StudentResponse;
import com.example.personal.service.ifs.StudentService;

@RestController
public class StudentController {
	
	@Autowired
	private StudentService studentService;
	
	@PostMapping("add_stu_id")
	public StudentResponse addStudId(@RequestBody StudentRequest request) {
		return studentService.addStudId(request);
	}
	
	@PostMapping("choose_course")
	public StudentResponse chooseCourse(@RequestBody StudentRequest request) {
		return studentService.chooseCourse(request);
	}
	
	@PostMapping("withdraw_course")
	public StudentResponse withdrawCourse(@RequestBody StudentRequest request) {
		return studentService.withdrawCourse(request);
	}
	
	@PostMapping("delete_stud_id")
	public StudentResponse deleteStudId(@RequestBody StudentRequest request) {
		return studentService.deleteStudId(request);
	}
}
