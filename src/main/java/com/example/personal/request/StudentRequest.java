package com.example.personal.request;

import java.util.List;

import com.example.personal.entity.Student;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StudentRequest {

	@JsonProperty("student_list")
	private List<Student> studentList;
	
	@JsonProperty("course_code_list")
	private List<String> courseCodeList;
	
	@JsonProperty("stud_id")
	private String StudId;
	
	public String getStudId() {
		return StudId;
	}

	public void setStudId(String studId) {
		StudId = studId;
	}

	public List<String> getCourseCodeList() {
		return courseCodeList;
	}

	public void setCourseCodeList(List<String> courseCodeList) {
		this.courseCodeList = courseCodeList;
	}

	public List<Student> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<Student> studentList) {
		this.studentList = studentList;
	}

}
