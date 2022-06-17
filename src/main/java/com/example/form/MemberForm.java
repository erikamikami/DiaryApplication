package com.example.form;

import javax.validation.constraints.NotBlank;

public class MemberForm {
	@NotBlank(message="※idを入力ください")
	private String id;
	@NotBlank(message="※nameを入力ください")
	private String name;
	@NotBlank(message="※passwordを入力ください")
	private String password;
	private Integer serial;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getSerial() {
		return serial;
	}
	public void setSerial(Integer serial) {
		this.serial = serial;
	}
	@Override
	public String toString() {
		return "MemberForm [id=" + id + ", name=" + name + ", password=" + password + ", serial=" + serial + "]";
	}
	
	
}
