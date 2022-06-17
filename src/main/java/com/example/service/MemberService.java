package com.example.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Member;
import com.example.repository.MemberRepository;

@Component
@Transactional
public class MemberService {

	@Autowired
	MemberRepository repository;

	@Autowired
	private HttpSession session;

	// ログイン認証機能
	public Member login(Member member) {
		String id = member.getId();
		String password = member.getPassword();
		Member memberX = new Member();
		memberX = repository.loadIdPassword(id, password);
		return memberX;
	}

	// 新規会員登録
	public Member insert(Member member) {
		repository.insert(member);
		return member;
	}

	// セッションにログイン情報があるかないか判定機能
	public boolean loginjudge() {
		Member memberinfo = (Member) session.getAttribute("memberinfo");
		if (memberinfo == null) {
			return false;
		}
		return true;
	}

	// idで検索を行う
	public Member findById(String id) {
		return repository.findById(id);
	}
	
	// 退会処理（id指定）
	public void deleteById(String id) {
		repository.deleteById(id);
	}

}
