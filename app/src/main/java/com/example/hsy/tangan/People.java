package com.example.hsy.tangan;

import java.io.Serializable;


public class People implements Serializable {

	private static final long serialVersionUID = 1L;
	

	private String phoneNumber;

	private String password;
	private String friends;


	public People() {

	}



	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phone_number) {
		this.phoneNumber = phone_number;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getFriends() {
		return friends;
	}

	public void setFriends(String friends) {
		this.friends = friends;
	}
}
