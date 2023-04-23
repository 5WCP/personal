package com.example.personal.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.personal.entity.CourseSch;
import com.example.personal.entity.SelectionSch;
import com.example.personal.entity.Student;
import com.example.personal.repository.CourseSchDao;
import com.example.personal.repository.SelectionSchDao;
import com.example.personal.repository.StudentDao;
import com.example.personal.request.SelectionSchRequest;
import com.example.personal.response.SelectionSchResponse;
import com.example.personal.response.StudentResponse;
import com.example.personal.service.ifs.SelectionSchService;

@Service
public class SelectionSchServiceImpl implements SelectionSchService {

	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private CourseSchDao courseSchDao;
	
	@Autowired
	private SelectionSchDao selectionSchDao;
	
	@Override
	public SelectionSchResponse searchStuSeleCour(SelectionSchRequest request) {
		String reqStId = request.getStudId();
		String StudIdFormat = "[BCD]\\d{4}";
		List<CourseSch> stIdCouSchList = new ArrayList<>(); // 該學號所選的課程列表
		List<SelectionSch> stIdSeleSchList = selectionSchDao.findByStudId(reqStId); // 學生所選的課
		if(!StringUtils.hasText(reqStId)) {
			return new SelectionSchResponse("查詢學號請確實填寫");
		}
		if(!reqStId.matches(StudIdFormat)) {
			return new SelectionSchResponse("學號格式錯誤 正確格式 : (B|C|D)&三個阿拉伯數字");
		}
		if(!studentDao.existsById(reqStId)) {
			return new SelectionSchResponse(reqStId + " : 學號不存在");
		}
		if(stIdSeleSchList.isEmpty()) {
			return new SelectionSchResponse(reqStId , " : 此學號尚未選課");
		}
		for(SelectionSch stIdSeleSch : stIdSeleSchList) { // 選課表中找出學號所選課程代碼 再由課程代碼找出課程詳細資訊
			String Co = stIdSeleSch.getCourseCode();
			CourseSch cou = courseSchDao.findById(Co).get();
			stIdCouSchList.add(cou);
		}
		Student stu = studentDao.findById(reqStId).get();
		return new SelectionSchResponse(stu, stIdCouSchList, "載入學生所選課程");
	}

}
