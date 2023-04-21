package com.example.personal.entity;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "coursesch")
public class CourseSch {

	@Column(name = "coursecode")
	@Id
	private String courseCode;

	@Column(name = "coursename")
	private String courseName;

	@Column(name = "credits")
	private int credits;

	@Column(name = "starttime")
	private LocalTime startTime;

	@Column(name = "endtime")
	private LocalTime endTime;

	@Column(name = "takeclassday")
	private String takeClassDay;

	@Column(name = "stucount")
	private int stuCount;

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public int getCredits() {
		return credits;
	}

	public void setCredits(int credits) {
		this.credits = credits;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public String getTakeClassDay() {
		return takeClassDay;
	}

	public void setTakeClassDay(String takeClassDay) {
		this.takeClassDay = takeClassDay;
	}

	public int getStuCount() {
		return stuCount;
	}

	public void setStuCount(int stuCount) {
		this.stuCount = stuCount;
	}

	public CourseSch() {

	}

}
