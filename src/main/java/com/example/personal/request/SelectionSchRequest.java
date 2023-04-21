package com.example.personal.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SelectionSchRequest {
	@JsonProperty("stud_id")
	private String studId;

	public String getStudId() {
		return studId;
	}

	public void setStudId(String studId) {
		this.studId = studId;
	}
	
}
