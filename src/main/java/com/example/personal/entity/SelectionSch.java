package com.example.personal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "selectionsch")
public class SelectionSch {
	
	@Id
	@Column(name = "selectionord")
	private String selectionord;
	
	@Column(name = "studid")
	private String studId;
	
	@Column(name = "coursecode")
	private String courseCode;
	
	public String getStudID() {
		return studId;
	}

	public void setStudID(String studId) {
		this.studId = studId;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getSelectionord() {
		return selectionord;
	}

	public void setSelectionord(String selectionord) {
		this.selectionord = selectionord;
	}

	public SelectionSch() {
		
	}

	public SelectionSch(String selectionord, String studId, String courseCode) {
		this.selectionord = selectionord;
		this.studId = studId;
		this.courseCode = courseCode;
	}
	
}
