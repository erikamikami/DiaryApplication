package com.example.domain;

import java.sql.Date;

public class Diary {
	private Integer id;
	private String memberId;
    private Date date;
    private String contents;
	
	@Override
	public String toString() {
		return "Diary [id=" + id + ", memberId=" + memberId + ", date=" + date + ", contents=" + contents + "]";
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
}
