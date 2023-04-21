package com.example.personal.response;

import java.util.List;

import com.example.personal.entity.CourseSch;
import com.example.personal.entity.Student;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SelectionSchResponse {
	
	private Student student;
	
	@JsonProperty("course_sch_list")
	private List<CourseSch> courseSchList;
	
	@JsonProperty("stud_id")
	private String studId;
	
	private String message;

	public List<CourseSch> getCourseSchList() {
		return courseSchList;
	}

	public void setCourseSchList(List<CourseSch> courseSchList) {
		this.courseSchList = courseSchList;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public String getStudId() {
		return studId;
	}

	public void setStudId(String studId) {
		this.studId = studId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public SelectionSchResponse(Student student, List<CourseSch> courseSchList, String message) {
		this.student = student;
		this.courseSchList = courseSchList;
		this.message = message;
	}

	public SelectionSchResponse(String message) {
		this.message = message;
	}

	public SelectionSchResponse() {
	}

	public SelectionSchResponse(String studId, String message) {
		this.studId = studId;
		this.message = message;
	}
	
}
