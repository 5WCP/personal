package com.example.personal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.personal.request.SelectionSchRequest;
import com.example.personal.response.SelectionSchResponse;
import com.example.personal.service.ifs.SelectionSchService;

@RestController
public class SelectionSchController {

	@Autowired
	private SelectionSchService selectionSchService;
	
	@PostMapping("search_stu_sele_cour")
	public SelectionSchResponse searchStuSeleCour(@RequestBody SelectionSchRequest request) {
		return selectionSchService.searchStuSeleCour(request);
	}
}
