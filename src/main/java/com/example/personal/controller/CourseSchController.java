package com.example.personal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.personal.entity.CourseSch;
import com.example.personal.request.CourseSchRequest;
import com.example.personal.response.CourseSchResponse;
import com.example.personal.service.ifs.CourseSchService;

@RestController
public class CourseSchController {

	@Autowired
	private CourseSchService courseSchService;
	
	@PostMapping("add_course")
	public CourseSchResponse addCourse(@RequestBody CourseSchRequest request) {
		return courseSchService.addCourse(request);
	}
	
	@PostMapping("get_all_course")
	public List<CourseSch> getAllCourse(){
		return courseSchService.getAllCourse();
	}
	
	@PostMapping("get_course")
	public CourseSchResponse getCourse(@RequestBody CourseSchRequest request) {
		return courseSchService.getCourse(request);
	}
	
	@PostMapping("revise_course")
	public CourseSchResponse reviseCourse(@RequestBody CourseSchRequest request) {
		return courseSchService.reviseCourse(request);
	}
	
	@PostMapping("find_course_name")
	public CourseSchResponse findCourseName(@RequestBody CourseSchRequest request){
		return courseSchService.findCourseName(request);
	}
}
