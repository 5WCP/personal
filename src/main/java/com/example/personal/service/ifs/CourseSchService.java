package com.example.personal.service.ifs;

import java.util.List;

import com.example.personal.entity.CourseSch;
import com.example.personal.request.CourseSchRequest;
import com.example.personal.response.CourseSchResponse;

public interface CourseSchService {
	
	public CourseSchResponse addCourse(CourseSchRequest request);
	
	public List<CourseSch> getAllCourse();
	
	public CourseSchResponse getCourse(CourseSchRequest request);
	
	public CourseSchResponse reviseCourse(CourseSchRequest request);
	
	public CourseSchResponse findCourseName(CourseSchRequest request);
}
