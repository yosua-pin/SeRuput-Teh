package model;

public class Transaction {
	private String transactionID;
    private String userID;
    private String productID;
    private int quantity;
    private String username;
    private String address;
    private String phoneNumber;
    private int price;
    private String productName;
 
	public Transaction(String transactionID, String userID, String productID, int quantity, String username,
			String address, String phoneNumber, int price, String productName) {
		this.transactionID = transactionID;
		this.userID = userID;
		this.productID = productID;
		this.quantity = quantity;
		this.username = username;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.price = price;
		this.productName = productName;
	}

	public String getTransactionID() {
		return transactionID;
	}

	public String getUserID() {
		return userID;
	}

	public String getProductID() {
		return productID;
	}

	public int getQuantity() {
		return quantity;
	}

	public String getUsername() {
		return username;
	}

	public String getAddress() {
		return address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public int getPrice() {
		return price;
	}
	
	public String getProductName() {
		return productName;
	}

	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public void setPrice(int price) {
		this.price = price;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
}
