package com.mySpringBoot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mySpringBoot.bean.Book;
import com.mySpringBoot.bean.Dept;
import com.mySpringBoot.bean.User;
import com.mySpringBoot.dao.MainDao;


@Service
public class MainService {

	@Autowired
	MainDao mainDao;
	
	public User getUserById(Integer userId) {
		return mainDao.getUserById(userId);
	}
	
	public int queryUserCount(String tj) {
		return mainDao.queryUserCount(tj);
	}
	
	public List<Book> querySome(String tj,int start,int end){
		return mainDao.querySome(tj,start, end);
	}
	
	public List<Dept> findDept() {
		return mainDao.findDept();
	}
}