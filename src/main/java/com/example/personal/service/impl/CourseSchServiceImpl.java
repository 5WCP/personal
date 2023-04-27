package com.example.personal.service.impl;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.personal.entity.CourseSch;
import com.example.personal.entity.SelectionSch;
import com.example.personal.repository.CourseSchDao;
import com.example.personal.repository.SelectionSchDao;
import com.example.personal.request.CourseSchRequest;
import com.example.personal.response.CourseSchResponse;
import com.example.personal.service.ifs.CourseSchService;

@Service
public class CourseSchServiceImpl implements CourseSchService{

	@Autowired
	private CourseSchDao courseSchDao;
	
	@Autowired
	private SelectionSchDao selectionSchDao;
	
	@Override
	public CourseSchResponse addCourse(CourseSchRequest request) {
		Optional<CourseSch> checkCou = Optional.ofNullable(request.getCoursesch());
		if(!checkCou.isPresent()) { // 檢查是否完全未輸入
			return new CourseSchResponse("填寫欄位請勿空白");
		}
		CourseSch Cou = request.getCoursesch();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss"); 
		String timeFormat = "\\d{2}(:\\d{2}){2}";
		String dayFormat = "星期[日一二三四五六]";
		String CoFormat = "[ABC]\\d{2}";
		if(!StringUtils.hasText(Cou.getCourseCode()) // 檢查是否未輸入 ("" ," ")
			|| !StringUtils.hasText(Cou.getCourseName())
			|| !StringUtils.hasText(Cou.getTakeClassDay())
			|| !StringUtils.hasText(request.getStartTime())
			|| !StringUtils.hasText(request.getEndTime())) {
			return new CourseSchResponse("所需填寫欄位請確實填寫");
		}
		if(Cou.getCredits() < 1 || Cou.getCredits() > 3) {
			return new CourseSchResponse("學分數只能為1~3這個區間");
		}
		if(!Cou.getCourseCode().matches(CoFormat)) {
			return new CourseSchResponse("課程代碼格式錯誤 正確格式 : (A|B|C)&兩個阿拉伯數字");
		}
		if(courseSchDao.existsById(Cou.getCourseCode())) {
			return new CourseSchResponse(Cou.getCourseCode() , "課程代碼已存在");
		}
		if(!request.getStartTime().matches(timeFormat)
			|| !request.getEndTime().matches(timeFormat)) {
			return new CourseSchResponse("時間格式錯誤 正確格式 : HH:mm:ss");
		}
		if(!Cou.getTakeClassDay().matches(dayFormat)) {
			return new CourseSchResponse("上課日格式錯誤 正確格式 : 星期(日~六)");
		}
		LocalTime stTi = LocalTime.parse(request.getStartTime(), formatter);
		LocalTime enTi = LocalTime.parse(request.getEndTime(), formatter);
		if(stTi.compareTo(enTi) > 0 ) { // 檢查結束時間是否在開始時間之後
			return new CourseSchResponse("課程開始時間不得晚於結束時間 課程時間設定有誤");
		}
		Cou.setStartTime(stTi);
		Cou.setEndTime(enTi);
		int classtime = (int)ChronoUnit.HOURS.between(stTi, enTi); // 上課時間
		if(Cou.getCredits() != classtime) { // 上課時間比對學分數
			return new CourseSchResponse("一學分對應的課程時數為一小時 上課時長或學分設定有誤");
		}
		courseSchDao.save(Cou);
		return new CourseSchResponse(Cou, "課堂新增成功");
	}

	@Override
	public List<CourseSch> getAllCourse() {
		return courseSchDao.findAll();
	}

	@Override
	public CourseSchResponse getCourse(CourseSchRequest request) {
		String CoFormat = "[ABC]\\d{2}";
		String reqCo = request.getCoursecode();
		if(!StringUtils.hasText(reqCo)) {
			return new CourseSchResponse("搜尋的課程代碼請確實填寫");
		}
		if(!reqCo.matches(CoFormat)) {
			return new CourseSchResponse("課程代碼格式錯誤 正確格式 : (A|B|C)&兩個阿拉伯數字");
		}
		if(courseSchDao.existsById(reqCo)) {
			CourseSch getCou = courseSchDao.findById(reqCo).get();
			return new CourseSchResponse(getCou , "查詢課程成功");
		}
		return new CourseSchResponse(reqCo , "課程代碼不存在");
	}

	@Override
	public CourseSchResponse deleteCourse(CourseSchRequest request) {
		String CoFormat = "[ABC]\\d{2}";
		String reqCo = request.getCoursecode();
		if(!StringUtils.hasText(reqCo)) {
			return new CourseSchResponse("預刪除的課程代碼請確實填寫");
		}
		if(!reqCo.matches(CoFormat)) {
			return new CourseSchResponse("課程代碼格式錯誤 正確格式 : (A|B|C)&兩個阿拉伯數字");
		}
		if(!courseSchDao.existsById(reqCo)) {
			return new CourseSchResponse(reqCo, "查無此課程代碼");
		}
		CourseSch Cou = courseSchDao.findById(reqCo).get();
		if(Cou.getStuCount() > 0) { // 大於0代表有學生選修中
			String mes = "無法刪除課程 有" + Cou.getStuCount() + "名學生選修中";
			return new CourseSchResponse(reqCo , mes);
		}
		courseSchDao.delete(Cou);
		return new CourseSchResponse(Cou , "課程已刪除");
	}

	@Override
	public CourseSchResponse findCourseName(CourseSchRequest request) {
		String errMge = "錯誤訊息 : ";
		if(!StringUtils.hasText(request.getCoursename())) {
			return new CourseSchResponse("請確實填寫預查詢課程名稱");
		}
		String reqCouN = request.getCoursename();
		List<CourseSch> searchCouList = courseSchDao.findByCourseName(reqCouN);
		if(searchCouList.isEmpty()) {
			errMge = "選課項目中沒有" + reqCouN + "課程";
			return new CourseSchResponse(errMge);
		}
		return new CourseSchResponse(searchCouList , "查詢課程成功");
	}

	@Override
	public CourseSchResponse reviseCourse(CourseSchRequest request) {
		CourseSch Cou = request.getCoursesch();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss"); 
		String timeFormat = "\\d{2}(:\\d{2}){2}";
		String dayFormat = "星期[日一二三四五六]";
		String CoFormat = "[ABC]\\d{2}";
		if(!StringUtils.hasText(Cou.getCourseCode())
				|| !StringUtils.hasText(Cou.getCourseName())
				|| !StringUtils.hasText(Cou.getTakeClassDay())
				|| !StringUtils.hasText(request.getStartTime())
				|| !StringUtils.hasText(request.getEndTime())) {
				return new CourseSchResponse("所需填寫欄位請確實填寫");
		}
		if(Cou.getCredits() < 1 || Cou.getCredits() > 3) {
			return new CourseSchResponse("學分數只能為1~3這個區間");
		}
		if(!Cou.getCourseCode().matches(CoFormat)) {
			return new CourseSchResponse("課程代碼格式錯誤 正確格式 : (A|B|C)&兩個阿拉伯數字");
		}
		if(!courseSchDao.existsById(Cou.getCourseCode())) {
			return new CourseSchResponse(Cou.getCourseCode() , "課程代碼不存在");
		}
		if(!request.getStartTime().matches(timeFormat)
			|| !request.getEndTime().matches(timeFormat)) {
			return new CourseSchResponse("時間格式錯誤 正確格式 : HH:mm:ss");
		}
		if(!Cou.getTakeClassDay().matches(dayFormat)) {
			return new CourseSchResponse("上課日格式錯誤 正確格式 : 星期(日~六)");
		}
		LocalTime stTi = LocalTime.parse(request.getStartTime(), formatter);
		LocalTime enTi = LocalTime.parse(request.getEndTime(), formatter);
		if(stTi.compareTo(enTi) > 0 ) { // 檢查結束時間是否在開始時間之後
			return new CourseSchResponse("課程開始時間不得晚於結束時間 課程時間設定有誤");
		}
		Cou.setStartTime(stTi);
		Cou.setEndTime(enTi);
		int classtime = (int)ChronoUnit.HOURS.between(stTi, enTi); // 上課時間
		if(Cou.getCredits() != classtime) { // 上課時間比對學分數
			return new CourseSchResponse("一學分對應的課程時數為一小時 上課時長或學分設定有誤");
		}
		CourseSch oriCou = courseSchDao.findById(Cou.getCourseCode()).get();
		if(oriCou.getStuCount() == 0) {
			courseSchDao.save(Cou);
			return new CourseSchResponse(Cou, "課堂修改成功");
		}
		List<SelectionSch> seleCouList = selectionSchDao.findByCourseCode(Cou.getCourseCode()); // 拉出選修該堂課的選課表
		List<String> TCSL = new ArrayList<>(); // 找出選修該堂課的學生
		for(SelectionSch seleCou : seleCouList) {
			TCSL.add(seleCou.getStudID());
		}
		for(String TCS : TCSL) {
			List<SelectionSch> TCSSeleCouList = selectionSchDao.findByStudId(TCS);
			for(SelectionSch TCSSC : TCSSeleCouList) {
				if(TCSSC.getCourseCode().equals(Cou.getCourseCode())) {
					continue;
				}
				CourseSch TCSC = courseSchDao.findById(TCSSC.getCourseCode()).get();
				if(!TCSC.getTakeClassDay().equals(Cou.getTakeClassDay())) {
					continue;
				}
				if(TCSC.getEndTime().compareTo(Cou.getStartTime()) <= 0
					|| TCSC.getStartTime().compareTo(Cou.getEndTime()) >= 0) {
					continue;
				}
				else {
					return new CourseSchResponse("此次預修改課程的課程代碼 : " + Cou.getCourseCode() + 
						"與已選修該課程的學生(學號) : " + TCS + "所選的課程(課程代碼) : "
						+ TCSC.getCourseCode() + "有衝堂");
				}
			}
		}
		Cou.setStuCount(oriCou.getStuCount());
		courseSchDao.save(Cou);
		return new CourseSchResponse(Cou, "課堂修改成功");
	}
}
