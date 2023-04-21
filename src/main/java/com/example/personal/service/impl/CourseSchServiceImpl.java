package com.example.personal.service.impl;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.personal.entity.CourseSch;
import com.example.personal.repository.CourseSchDao;
import com.example.personal.request.CourseSchRequest;
import com.example.personal.response.CourseSchResponse;
import com.example.personal.service.ifs.CourseSchService;

@Service
public class CourseSchServiceImpl implements CourseSchService{

	@Autowired
	private CourseSchDao courseSchDao;
	
	@Override
	public CourseSchResponse addCourse(CourseSchRequest request) {
		Optional<CourseSch> checkCou = Optional.ofNullable(request.getCoursesch());
		if(!checkCou.isPresent()) {
			return new CourseSchResponse("填寫欄位請勿空白");
		}
		CourseSch Cou = request.getCoursesch();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		String timeFormat = "\\d{2}(:\\d{2}){2}";
		String dayFormat = "星期[日一二三四五六]";
		String CoFormat = "[ABC]\\d{2}";
		if(!StringUtils.hasText(Cou.getCourseCode())
			|| !StringUtils.hasText(Cou.getCourseName())
			|| !StringUtils.hasText(Cou.getTakeClassDay())
			|| Cou.getCredits()==0
			|| !StringUtils.hasText(request.getStartTime())
			|| !StringUtils.hasText(request.getEndTime())) {
			return new CourseSchResponse("所需填寫欄位請確實填寫");
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
		Cou.setStartTime(stTi);
		Cou.setEndTime(enTi);
		int classtime = (int)ChronoUnit.HOURS.between(stTi, enTi);
		if(Cou.getCredits() != classtime) {
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
		String reqCo = request.getCoursecode();
		if(!StringUtils.hasText(reqCo)) {
			return new CourseSchResponse("搜尋的課程代碼請確實填寫");
		}
		if(courseSchDao.existsById(reqCo)) {
			CourseSch getCou = courseSchDao.findById(reqCo).get();
			return new CourseSchResponse(getCou , "查詢課程成功");
		}
		return new CourseSchResponse(reqCo , "課程代碼不存在");
	}

	@Override
	public CourseSchResponse reviseCourse(CourseSchRequest request) {
		String reqCo = request.getCoursecode();
		if(!StringUtils.hasText(reqCo)) {
			return new CourseSchResponse("預刪除的課程代碼請確實填寫");
		}
		if(!courseSchDao.existsById(reqCo)) {
			return new CourseSchResponse(reqCo, "查無此課程代碼");
		}
		CourseSch Cou = courseSchDao.findById(reqCo).get();
		if(Cou.getStuCount() > 0) {
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

}
