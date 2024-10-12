package model;

public class CartItem {
	 private String productID;
     private String productName;
     private String productPrice;
     private String description;
     private String quantity;
     
	public CartItem(String productID, String productName, String productPrice, String description, String quantity) {
		super();
		this.productID = productID;
		this.productName = productName;
		this.productPrice = productPrice;
		this.description = description;
		this.quantity = quantity;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getProductID() {
		return productID;
	}
	public void setProductID(String productID) {
		this.productID = productID;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
