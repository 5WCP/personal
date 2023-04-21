package com.example.personal.response;

import java.util.List;

import com.example.personal.entity.CourseSch;
import com.example.personal.entity.SelectionSch;
import com.example.personal.entity.Student;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentResponse {
	
	@JsonProperty("student_list")
	private List<Student> studentList;	
	
	@JsonProperty("course_sch_list")
	private List<CourseSch> courseSchList;	
	
	@JsonProperty("student")
	private Student student;
	
	@JsonProperty("stud_id")
	private String studID;
	
	private String message;	
	
	@JsonProperty("total_credits")
	private Integer totalCredits;
	
	@JsonProperty("drop_courses_list")
	private List<SelectionSch> dropCrousesList;	

	public List<CourseSch> getCourseSchList() {
		return courseSchList;
	}

	public void setCourseSchList(List<CourseSch> courseSchList) {
		this.courseSchList = courseSchList;
	}

	public Integer getTotalCredits() {
		return totalCredits;
	}

	public void setTotalCredits(Integer totalCredits) {
		this.totalCredits = totalCredits;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Student> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<Student> studentList) {
		this.studentList = studentList;
	}

	public List<SelectionSch> getDropCrousesList() {
		return dropCrousesList;
	}

	public void setDropCrousesList(List<SelectionSch> dropCrousesList) {
		this.dropCrousesList = dropCrousesList;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public StudentResponse() {
		
	}

	public StudentResponse(List<CourseSch> courseSchList, String message, Integer totalCredits) {
		this.courseSchList = courseSchList;
		this.message = message;
		this.totalCredits = totalCredits;
	}

	public StudentResponse(String message) {
		this.message = message;
	}

	public StudentResponse(List<Student> studentList, String message) {
		this.studentList = studentList;
		this.message = message;
	}

	public StudentResponse(String message, Integer totalCredits, List<SelectionSch> dropCrousesList) {
		this.message = message;
		this.totalCredits = totalCredits;
		this.dropCrousesList = dropCrousesList;
	}

	public StudentResponse(Student student, String message) {
		this.student = student;
		this.message = message;
	}

	public StudentResponse(String studID, String message) {
		this.studID = studID;
		this.message = message;
	}

}
