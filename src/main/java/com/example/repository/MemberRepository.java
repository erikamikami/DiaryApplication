package com.example.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.example.domain.Member;

@Repository
@Component
public class MemberRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	private static final RowMapper<Member> MEMBER_ROW_MAPPER = (rs, i) -> {
		Member member = new Member();
		member.setSerial(rs.getInt("serial"));
		member.setId(rs.getString("id"));
		member.setName(rs.getString("name"));
		member.setPassword(rs.getString("password"));
		return member;
	};

	// idとパスワードで検索を行う
	public Member loadIdPassword(String id, String password) {
		String sql = "SELECT * FROM member WHERE id=:id AND password=:password";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id).addValue("password", password);
		return template.queryForObject(sql, param, MEMBER_ROW_MAPPER);
	}

	// idで検索を行う
	public Member findById(String id) {
		String sql = "SELECT * FROM member WHERE id=:id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		return template.queryForObject(sql, param, MEMBER_ROW_MAPPER);
	}

	// 新規会員登録
	public void insert(Member member) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(member);
		String sql = "INSERT INTO member (id, name, password) VALUES (:id, :name, :password)";
		template.update(sql, param);
	}

	// 退会処理（id指定）
	public void deleteById(String id) {
		String sql ="DELETE FROM member WHERE id=:id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		template.update(sql, param);
	}
}
