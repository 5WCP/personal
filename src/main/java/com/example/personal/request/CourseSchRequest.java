package com.example.personal.request;

import com.example.personal.entity.CourseSch;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseSchRequest {

	@JsonProperty("course_sch")
	private CourseSch coursesch;
	
	@JsonProperty("start_time")
	private String startTime;
	
	@JsonProperty("end_time")
	private String endTime;
	
	@JsonProperty("course_name")
	private String coursename;
	
	@JsonProperty("course_code")
	private String coursecode;

	public CourseSch getCoursesch() {
		return coursesch;
	}

	public void setCoursesch(CourseSch coursesch) {
		this.coursesch = coursesch;
	}

	public String getCoursename() {
		return coursename;
	}

	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}

	public String getCoursecode() {
		return coursecode;
	}

	public void setCoursecode(String coursecode) {
		this.coursecode = coursecode;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}
