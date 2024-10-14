package com.example.data_models;

public class LoginRequest{
	private String name;
	private String loginUsername;
	private String loginPassword;
	
	public LoginRequest(String name, String username, String password){
		this.name = name;
		this.loginUsername = username;
		this.loginPassword = password;
	}
	
}
