package com.example.form;

import javax.validation.constraints.NotBlank;

public class DiaryForm {
	private Integer id;
	private String memberId;
	@NotBlank(message="※日付を選択してください")
    private String date;
	@NotBlank(message="※日記を記入してください")
    private String contents;
    
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "DiaryForm [id=" + id + ", memberId=" + memberId + ", date=" + date + ", contents=" + contents + "]";
	}
	
}
