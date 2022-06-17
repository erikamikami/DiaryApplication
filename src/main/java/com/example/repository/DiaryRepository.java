package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.example.domain.Diary;

@Repository
@Component
public class DiaryRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<Diary> DIARY_ROW_MAPPER = (rs, i) -> {
		Diary diary = new Diary();
		diary.setId(rs.getInt("id"));
		diary.setDate(rs.getDate("date"));
		diary.setContents(rs.getString("contents"));
		diary.setMemberId(rs.getString("member_id"));
		return diary;
	};
	
	// 日記の一覧表示（MemberId指定）
	public List<Diary> findByMemberId(String memberId){
		String sql = "SELECT * FROM diary WHERE member_id=:memberId ORDER BY date";
		SqlParameterSource param = new MapSqlParameterSource().addValue("memberId", memberId);
		List<Diary> diaryList = new ArrayList<>();
		diaryList = template.query(sql, param, DIARY_ROW_MAPPER);
		return diaryList;
	}
	
	// 日記を1個だけ表示（Id指定）
	public Diary findById(Integer id) {
		String sql="SELECT * FROM diary WHERE id=:id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		return template.queryForObject(sql, param, DIARY_ROW_MAPPER);
	}

	// 日記を登録する（INSERT）
	public Diary insert(Diary diary) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(diary);
		String sql = "INSERT INTO diary (date, contents, member_id) VALUES (:date, :contents, :memberId)";
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String[] keyColumnNames = {"id"};
		
		template.update(sql, param, keyHolder, keyColumnNames);
		diary.setId(keyHolder.getKey().intValue());
		return diary;
	}
	
	// 日記を編集する（UPDATE）（id指定）
	public void update(Diary diary) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(diary);
		String sql = "UPDATE diary SET date=:date, contents=:contents WHERE id=:id";
		template.update(sql, param);
	}
	
	
	// 日記を削除する（DELETE）（id指定）
	public void delete(Integer id) {
		String sql="DELETE FROM diary WHERE id=:id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		template.update(sql, param);
	}
	
	// ある特定の人の日記をすべて削除する（member_id指定）
		public void deleteByMemberId(String memberId) {
			String sql ="DELETE FROM diary WHERE member_id=:memberId";
			SqlParameterSource param = new MapSqlParameterSource().addValue("memberId", memberId);
			template.update(sql, param);
		}
	
}
