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
import com.example.personal.request.StudentRequest;
import com.example.personal.response.StudentResponse;
import com.example.personal.service.ifs.StudentService;

@Service
public class StudentServiceImpl implements StudentService {
	
	@Autowired
	private CourseSchDao courseSchDao;
	
	@Autowired
	private SelectionSchDao selectionSchDao;
	
	@Autowired
	private StudentDao studentDao;
	
	@Override
	public StudentResponse addStudId(StudentRequest request) { // 新增多個學生
		List<Student> reqStuList = request.getStudentList(); // 預新增的學生列表
		String StudIdFormat = "[BCD]\\d{4}";
		String errMge = "錯誤訊息 : ";
		if(reqStuList.isEmpty()) {
			return new StudentResponse("請輸入至少一位學生");
		}
		for(Student reqStu : reqStuList) {
			if(!StringUtils.hasText(reqStu.getStudId())
				||!StringUtils.hasText(reqStu.getName())) {
				return new StudentResponse("需填寫欄位請確實填寫");
			}
			if(!reqStu.getStudId().matches(StudIdFormat)) {
				return new StudentResponse("學號格式錯誤 正確格式 : (B|C|D)&三個阿拉伯數字");
			}
			if(studentDao.existsById(reqStu.getStudId())) {
				errMge += reqStu.getStudId() + " : 此學號已存在 ";
			}
		}
		if(errMge != "錯誤訊息 : ") { // 預新增已存在的學號 顯示錯誤訊息
			return new StudentResponse(errMge);
		}
		studentDao.saveAll(reqStuList);
		return new StudentResponse(reqStuList ,"學生新增成功");
	}
	
	@Override
	public StudentResponse chooseCourse(StudentRequest request) { // 一個學號選多個課程
		if(!StringUtils.hasText(request.getStudId())) {
			return new StudentResponse("請輸入學號");
		}
		List<String> CoList = request.getCourseCodeList();
		if(CoList.isEmpty()) {
			return new StudentResponse("至少要輸入一個課堂代碼");
		}
		List<SelectionSch> seleList = new ArrayList<>(); // 該學生已選修課程列表
		List<SelectionSch> newSeleList = new ArrayList<>(); // 此次選修成功列表
		List<CourseSch> couList = new ArrayList<>(); // 課程選修成功列表
		String errMge = "錯誤訊息 : ";
		CourseSch cou;
		int totCre = 0;
		int CouSt = 0;
		String StudIdFormat = "[BCD]\\d{4}";
		if(!request.getStudId().matches(StudIdFormat)) {
			return new StudentResponse("學號格式錯誤 正確格式 : (B|C|D)&三個阿拉伯數字");
		}
		if(!studentDao.existsById(request.getStudId())) {
			errMge += request.getStudId() + " : 學號不存在 ";
			return new StudentResponse(errMge);
		}
		seleList = selectionSchDao.findByStudId(request.getStudId());
		Student stu = studentDao.findById(request.getStudId()).get(); // 找出學生資訊
		totCre = stu.getTotalCredits(); // 學生總學分
		for(String Co : CoList) {
			if(!StringUtils.hasText(Co)) {
				return new StudentResponse("課堂代碼請確實填寫");
			}
			if(!courseSchDao.existsById(Co)) {
				errMge += Co + " : 暫無此課程代碼 ";
				return new StudentResponse(errMge);
			}
			int countC = 0; // 課程代碼
			int countN = 0; // 課程名稱
			int count = 0; // 衝堂
			for(String C : CoList) { // 比對該次選課間是否有相同課程代碼的課程
				if(Co.equals(C)) {
					countC++;
					if(countC == 1) {
						continue;
					}
					if(countC == 2) {
						return new StudentResponse("重複選擇同一堂課");
					}
				}
			}
			cou = courseSchDao.findById(Co).get();
			for(String C : CoList) {
				CourseSch co = courseSchDao.findById(C).get();
				if(co.getCourseName().equals(cou.getCourseName())) { // 比對該次選課間是否有相同課程名稱的課程
					countN++;
					if(countN == 1) {
						continue;
					}
					if(countN == 2) {
						return new StudentResponse(Co + "與" + C + "為名稱相同的課程 只能擇一選修" );
					}
				}
			}
			for(String C : CoList) { // 比對該次選課間是否有衝堂
				CourseSch co = courseSchDao.findById(C).get();
				if(!co.getTakeClassDay().equals(cou.getTakeClassDay())) {
					continue;
				} else if(co.getEndTime().compareTo(cou.getStartTime()) <= 0
						|| co.getStartTime().compareTo(cou.getEndTime()) >= 0) {
					continue;
				}  else {
					count++;
					if(count == 1) {
						continue;
					}
					if(count == 2) {
						errMge += Co + "與" + C + "兩課程有衝堂 ";
						return new StudentResponse(errMge);
					}
				}
			}
			CouSt = cou.getStuCount();
			if(CouSt >= 3) { // 超過課堂人數上限不得選修
				errMge += Co + " : 該課堂人數已達上限 ";
			}
			if(totCre >= 10) { // 超過學分可選修總學分 不得選修
				return new StudentResponse("學生課程總學分已達上限");
			}
			if(!seleList.isEmpty()) { // 該學生如果已有選課
				for(SelectionSch se : seleList) {
					CourseSch seleCou = courseSchDao.findById(se.getCourseCode()).get();
					if(Co.equals(se.getCourseCode()) // 相同課程代碼或課程名稱相同
						|| cou.getCourseName().equals(seleCou.getCourseName())) {
						errMge += Co + " : 相同或名稱相同的課程只能選擇一堂 ";
					}
					if(!cou.getTakeClassDay().equals(seleCou.getTakeClassDay())) { // 衝堂
						continue;
					} else if(cou.getEndTime().compareTo(seleCou.getStartTime()) <= 0
							|| cou.getStartTime().compareTo(seleCou.getEndTime()) >= 0) {
						continue;
					} else {
							errMge += Co + " : 該課堂跟已加選的課程衝堂 ";
					}
				}
			}			
			couList.add(cou); // 本次迴圈的課程比對無誤 可選修
			totCre += cou.getCredits(); // 加入課堂學分至學生總學分
			if(totCre > 10) { // 新增至學生總學分後 比對是否超過學生選修學分上限
				return new StudentResponse("此次加選 學分超越上限 請重新加選(上限10學分)");
			}
		}
		if(errMge != "錯誤訊息 : ") { // 如有錯誤訊息
			return new StudentResponse(errMge);
		}
		for(String Co : CoList) { // 無錯誤訊息 新增選課資訊
			cou = courseSchDao.findById(Co).get();
			CouSt = cou.getStuCount();
			CouSt++; // 選修成功 課堂人數增加
			cou.setStuCount(CouSt);
			courseSchDao.save(cou);
			SelectionSch sele = new SelectionSch(request.getStudId() + "選擇" + Co , request.getStudId() , Co);
			newSeleList.add(sele);
		}
		stu.setTotalCredits(totCre); // 本次選修課程列表無異常 設定學生總學分
		studentDao.save(stu);
		selectionSchDao.saveAll(newSeleList);
		return new StudentResponse(couList, "本次所選課程如上 目前該學號所選課程總學分為 : ", totCre);
	}

	@Override
	public StudentResponse withdrawCourse(StudentRequest request) { // 一個學號退選多個課程
		String StudIdFormat = "[BCD]\\d{4}";
		String errMge = "錯誤訊息 : ";
		if(!StringUtils.hasText(request.getStudId())) {
			return new StudentResponse("需填寫欄位請確實填寫");
		}
		if(!request.getStudId().matches(StudIdFormat)) {
			return new StudentResponse("學號格式錯誤 正確格式 : (B|C|D)&三個阿拉伯數字");
		}
		if(!studentDao.existsById(request.getStudId())) {
			errMge += request.getStudId() + " : 學號不存在";
			return new StudentResponse(errMge);
		}
		List<String> reqCoList = request.getCourseCodeList(); // 預退選課程代碼列表
		if(reqCoList.isEmpty()) {
			return new StudentResponse("預退選課堂代碼至少要填入一個");
		}
		List<SelectionSch> seleSch = selectionSchDao.findByStudId(request.getStudId()); // 搜尋學生選課資訊
		List<SelectionSch> dropSeleList = new ArrayList<>(); // 退選成功列表
		Student stu = studentDao.findById(request.getStudId()).get();
		int totCre = stu.getTotalCredits(); // 學生選修總學分
		int CouSt = 0; // 課堂上課人數
		for(String dropACo : reqCoList) {
			if(!StringUtils.hasText(dropACo)) {
				return new StudentResponse("預退選課堂代碼請確實填寫");
			}
			for(SelectionSch sele : seleSch) {
				if(dropACo.equals(sele.getCourseCode())) { // 比對預退選課程是否存在於選課表中
					CourseSch cou = courseSchDao.findById(dropACo).get();
					totCre -= cou.getCredits(); // 退選課程 學生選修學分數減少
					CouSt = cou.getStuCount();
					CouSt--; // 學生退選 上課人數減少
					cou.setStuCount(CouSt);
					courseSchDao.save(cou);
					dropSeleList.add(sele);
					break;
				}
			}
		}
		if(!dropSeleList.isEmpty()) {
			stu.setTotalCredits(totCre);
			studentDao.save(stu);
			selectionSchDao.deleteAll(dropSeleList); // 刪除選課表資訊
			return new StudentResponse("成功退選課程如下(如有退選但未退選成功 可能原因 : "
					+ "1 預退選的課程並未選修 2 沒有此課程) : \n目前總學分為 : " , totCre , dropSeleList);
		}
		return new StudentResponse("預退選的課程全部退選失敗");
	}

	@Override
	public StudentResponse deleteStudId(StudentRequest request) { // 單個刪除學生
		String StudIdFormat = "[BCD]\\d{4}";
		String reqStId = request.getStudId();
		if(!StringUtils.hasText(reqStId)) {
			return new StudentResponse(reqStId , "預刪除的學號請確實填寫");
		}
		if(!reqStId.matches(StudIdFormat)) {
			return new StudentResponse("學號格式錯誤 正確格式 : (B|C|D)&三個阿拉伯數字");
		}
		if(!studentDao.existsById(reqStId)) {
			return new StudentResponse(reqStId , "該學號不存在");
		}
		Student stu = studentDao.findById(reqStId).get();
		if(stu.getTotalCredits() > 0) { // 學分大於0 代表有選修課程
			return new StudentResponse(stu , "該學生尚有選修中的課程 無法刪除學號");
		}
		studentDao.delete(stu);
		return new StudentResponse(stu , "學號刪除完畢");
	}

	@Override
	public StudentResponse reviseStudId(StudentRequest request) {
		String StudIdFormat = "[BCD]\\d{4}";
		List<Student> reqStuList = request.getStudentList();
		String errMge = "錯誤訊息 : ";
		if(reqStuList.isEmpty()) {
			return new StudentResponse("請至少輸入一位學生");
		}
		for(Student reqStu : reqStuList) {
			if(!StringUtils.hasText(reqStu.getStudId())
				|| !StringUtils.hasText(reqStu.getName())) {
				return new StudentResponse("需填寫欄位請確實填寫");
			}
			if(!reqStu.getStudId().matches(StudIdFormat)) {
				return new StudentResponse("學號格式錯誤 正確格式 : (B|C|D)&三個阿拉伯數字");
			}
			if(!studentDao.existsById(reqStu.getStudId())) {
				errMge += reqStu.getStudId() + " : 此學號已存在 ";
			}
		}
		if(errMge != "錯誤訊息 : ") { // 預修改不存在的學號 顯示錯誤訊息
			return new StudentResponse(errMge);
		}
		studentDao.saveAll(reqStuList);
		return new StudentResponse(reqStuList, "學生資料更改成功");
	}
}
