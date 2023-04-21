package com.example.personal.response;

import java.util.List;

import com.example.personal.entity.CourseSch;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseSchResponse {

	@JsonProperty("course_sch")
	private CourseSch coursesch;
	
	@JsonProperty("course_sch_list")
	private List<CourseSch> courseSchList;
	
	@JsonProperty("courser_code")
	private String courseCode;	
	
	private String message;

	public CourseSch getCoursesch() {
		return coursesch;
	}

	public void setCoursesch(CourseSch coursesch) {
		this.coursesch = coursesch;
	}

	public List<CourseSch> getCourseSchList() {
		return courseSchList;
	}

	public void setCourseSchList(List<CourseSch> courseSchList) {
		this.courseSchList = courseSchList;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public CourseSchResponse(CourseSch coursesch, String message) {
		this.coursesch = coursesch;
		this.message = message;
	}

	public CourseSchResponse(List<CourseSch> courseSchList, String message) {
		this.courseSchList = courseSchList;
		this.message = message;
	}

	public CourseSchResponse(String message) {
		this.message = message;
	}

	public CourseSchResponse() {
		
	}

	public CourseSchResponse(String courseCode, String message) {
		this.courseCode = courseCode;
		this.message = message;
	}
	
}
