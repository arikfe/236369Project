package com.technion.ex5.Data;

public class User {

	public enum UserType {
		Regular, Admin;
	}

	private String name;
	private String password;
	private String username;
	private String address;
	private UserType type;

	public UserType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public String getAddress() {
		return address;
	}

	public User() {
		super();

	}

}
