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
	public StudentResponse addStudId(StudentRequest request) {
		List<Student> reqStuList = request.getStudentList();
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
		if(errMge != "錯誤訊息 : ") {
			return new StudentResponse(errMge);
		}
		studentDao.saveAll(reqStuList);
		return new StudentResponse(reqStuList ,"學生新增成功");
	}
	
	@Override
	public StudentResponse chooseCourse(StudentRequest request) {
		if(!StringUtils.hasText(request.getStudId())) {
			return new StudentResponse("請輸入學號");
		}
		List<String> CoList = request.getCourseCodeList();
		if(CoList.isEmpty()) {
			return new StudentResponse("至少要輸入一個課堂代碼");
		}
		List<SelectionSch> seleList = new ArrayList<>();
		List<SelectionSch> newSeleList = new ArrayList<>();
		List<CourseSch> couList = new ArrayList<>();
		String errMge = "錯誤訊息 : ";
		CourseSch cou;
		int totCre = 0;
		int CouSt = 0;
		if(!studentDao.existsById(request.getStudId())) {
			errMge += request.getStudId() + " : 學號不存在 ";
			return new StudentResponse(errMge);
		}
		seleList = selectionSchDao.findByStudId(request.getStudId());
		Student stu = studentDao.findById(request.getStudId()).get();
		totCre = stu.getTotalCredits();
		for(String Co : CoList) {
			if(!StringUtils.hasText(Co)) {
				return new StudentResponse("課堂代碼請確實填寫");
			}
			if(!courseSchDao.existsById(Co)) {
				errMge += Co + " : 暫無此課程代碼 ";
				return new StudentResponse(errMge);
			}
			int countC = 0;
			int countN = 0;
			int count = 0;
			for(String C : CoList) {
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
				if(co.getCourseName().equals(cou.getCourseName())) {
					countN++;
					if(countN == 1) {
						continue;
					}
					if(countN == 2) {
						return new StudentResponse(Co + "與" + C + "為名稱相同的課程 只能擇一選修" );
					}
				}
			}
			for(String C : CoList) {
				CourseSch co = courseSchDao.findById(C).get();
				if(!co.getTakeClassDay().equals(cou.getTakeClassDay())) {
					continue;
				} else if(!((co.getStartTime().compareTo(cou.getStartTime()) < 0
						&& co.getEndTime().compareTo(cou.getStartTime()) >= 0)
						|| (co.getStartTime().compareTo(cou.getEndTime()) <= 0
						&& co.getEndTime().compareTo(cou.getEndTime()) > 0))) {
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
			if(CouSt >= 3) {
				errMge += Co + " : 該課堂人數已達上限 ";
			}
			if(totCre >= 10) {
				return new StudentResponse("學生課程總學分已達上限");
			}
			if(!seleList.isEmpty()) {
				for(SelectionSch se : seleList) {
					CourseSch seleCou = courseSchDao.findById(se.getCourseCode()).get();
					if(Co.equals(se.getCourseCode())
						|| cou.getCourseName().equals(seleCou.getCourseName())) {
						errMge += Co + " : 相同或名稱相同的課程只能選擇一堂 ";
					}
					if(!cou.getTakeClassDay().equals(seleCou.getTakeClassDay())) {
						continue;
					} else if(!((cou.getStartTime().compareTo(seleCou.getStartTime()) < 0
							&& cou.getEndTime().compareTo(seleCou.getStartTime()) >= 0)
							|| (cou.getStartTime().compareTo(seleCou.getEndTime()) <= 0
							&& cou.getEndTime().compareTo(seleCou.getEndTime()) > 0))) {
						continue;
					} else {
							errMge += Co + " : 該課堂跟已加選的課程衝堂 ";
					}
				}
			}			
			couList.add(cou);
			totCre += cou.getCredits();
			if(totCre > 10) {
				return new StudentResponse("此次加選 學分超越上限 請重新加選(上限10學分)");
			}
		}
		if(errMge != "錯誤訊息 : ") {
			return new StudentResponse(errMge);
		}
		for(String Co : CoList) {
			cou = courseSchDao.findById(Co).get();
			CouSt = cou.getStuCount();
			CouSt++;
			cou.setStuCount(CouSt);
			courseSchDao.save(cou);
			SelectionSch sele = new SelectionSch(request.getStudId() + "選擇" + Co , request.getStudId() , Co);
			newSeleList.add(sele);
		}
		stu.setTotalCredits(totCre);
		studentDao.save(stu);
		selectionSchDao.saveAll(newSeleList);
		return new StudentResponse(couList, "本次所選課程如上 目前該學號所選課程總學分為 : ", totCre);
	}

	@Override
	public StudentResponse withdrawCourse(StudentRequest request) {
		String errMge = "錯誤訊息 : ";
		if(!StringUtils.hasText(request.getStudId())) {
			return new StudentResponse("需填寫欄位請確實填寫");
		}
		if(!studentDao.existsById(request.getStudId())) {
			errMge += request.getStudId() + " : 學號不存在";
			return new StudentResponse(errMge);
		}
		List<String> reqCoList = request.getCourseCodeList();
		if(reqCoList.isEmpty()) {
			return new StudentResponse("預退選課堂代碼至少要填入一個");
		}
		List<SelectionSch> seleSch = selectionSchDao.findByStudId(request.getStudId());
		List<SelectionSch> dropSeleList = new ArrayList<>();
		Student stu = studentDao.findById(request.getStudId()).get();
		int totCre = stu.getTotalCredits();
		int CouSt = 0;
		for(String dropACo : reqCoList) {
			if(!StringUtils.hasText(dropACo)) {
				return new StudentResponse("預退選課堂代碼請確實填寫");
			}
			for(SelectionSch sele : seleSch) {
				if(dropACo.equals(sele.getCourseCode())) {
					CourseSch cou = courseSchDao.findById(dropACo).get();
					totCre -= cou.getCredits();
					CouSt = cou.getStuCount();
					CouSt--;
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
			selectionSchDao.deleteAll(dropSeleList);
			return new StudentResponse("成功退選課程如下(如有退選但未退選成功 可能原因 : "
					+ "1 預退選的課程並未選修 2 沒有此課程) : \n目前總學分為 : " , totCre , dropSeleList);
		}
		return new StudentResponse("預退選的課程全部退選失敗");
	}

	@Override
	public StudentResponse deleteStudId(StudentRequest request) {
		String reqStId = request.getStudId();
		if(!StringUtils.hasText(reqStId)) {
			return new StudentResponse(reqStId , "預刪除的學號請確實填寫");
		}
		if(!studentDao.existsById(reqStId)) {
			return new StudentResponse(reqStId , "該學號不存在");
		}
		Student stu = studentDao.findById(reqStId).get();
		if(stu.getTotalCredits() > 0) {
			return new StudentResponse(stu , "該學生尚有選修中的課程 無法刪除學號");
		}
		studentDao.delete(stu);
		return new StudentResponse(stu , "學號刪除完畢");
	}
}
