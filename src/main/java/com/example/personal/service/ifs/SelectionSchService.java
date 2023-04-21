package com.example.personal.service.ifs;

import com.example.personal.request.SelectionSchRequest;
import com.example.personal.response.SelectionSchResponse;

public interface SelectionSchService {
	
	public SelectionSchResponse searchStuSeleCour(SelectionSchRequest request);
}
