package com.team.mobileworld.core.handle;

import java.util.regex.Pattern;

public class Validate {
	public static String REGEX_EMAIL = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)"
			+ "*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
	public static String REGEX_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
	
	public static String REGEX_USER = "^[\\Sa-zA-Z0-9].{3,40}$";

	/**
	 * Kiểm tra sự hợp lệ của ký tự (User-Password)
	 * ^ # bắt đầu của chuỗi 
	 * (? =. * [0-9]) một chữ số phải xảy ra ít nhất một lần
	 * (? =. * [a-z]) một chữ cái viết thường phải xảy ra ít nhất một lần
	 * (? =. * [A-Z]) một chữ in hoa phải xảy ra ít nhất một lần
	 * (? =. * [@ # $% ^ & + =]) chứa ký tự đăc biệt
	 * (? = \ S + $) loại bỏ khoảng trăng
	 * .{8,}  ít nhất 8 ký tự
	 */
	
	public static boolean validatePassword(String password)
	{
		Pattern pattern = Pattern.compile(REGEX_PASSWORD);
		return pattern.matcher(password).matches();
	}
	
	/**
	 * Kiem tra tai khoan co hop le khong 
	 * 
	 * \w	:Ký tự chữ, viết ngắn gọn cho [a-zA-Z_0-9]
	 */
	public static boolean validateUser(String username) {
		Pattern pattern = Pattern.compile(REGEX_USER);
		return pattern.matcher(username).matches();
	}

}
