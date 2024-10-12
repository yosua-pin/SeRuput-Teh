package model;

public class User {

	private String userID;
	private String userName;
	private String password;
	private String role;
	private String address;
	private String phone_num;
	private String gender;
	
	public User(String userID, String userName, String password, String role, String address, String phone_num,
			String gender) {
		super();
		this.userID = userID;
		this.userName = userName;
		this.password = password;
		this.role = role;
		this.address = address;
		this.phone_num = phone_num;
		this.gender = gender;
	}
	public User() {
		// TODO Auto-generated constructor stub
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone_num() {
		return phone_num;
	}
	public void setPhone_num(String phone_num) {
		this.phone_num = phone_num;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	
}
