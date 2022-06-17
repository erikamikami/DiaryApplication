package com.example.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Diary;
import com.example.repository.DiaryRepository;

@Component
@Transactional
public class DiaryService {

	@Autowired
	DiaryRepository repository;

	@Autowired
	private HttpSession session;

	// 日記の一覧表示（id指定）
	public List<Diary> findByMemberId(String memberid) {
		return repository.findByMemberId(memberid);
	}

	// 日記を1個だけ表示（Id指定）
	public Diary findById(Integer id) {
		return repository.findById(id);
	}

	// 日記を登録する（INSERT）
	public Diary insert(Diary diary) {
		return repository.insert(diary);
	}

	// 日記を編集する（UPDATE）（id指定）
	public void update(Diary diary) {
		repository.update(diary);
	}

	// 日記を削除する（DELETE）（id指定）
	public void delete(Integer id) {
		repository.delete(id);
	}
	
	// ある特定の人の日記をすべて削除する（member_id指定）
	public void deleteByMemberId(String memberId) {
		repository.deleteByMemberId(memberId);
	}

}
