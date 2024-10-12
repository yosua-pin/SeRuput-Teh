package model;

public class ProductItem {
	private String productID;
    private String productName;
    private int productPrice;
    private String description;
    
	public ProductItem(String productID, String productName, int productPrice, String description) {
		this.productID = productID;
		this.productName = productName;
		this.productPrice = productPrice;
		this.description = description;
	}

	public String getProductID() {
		return productID;
	}

	public String getProductName() {
		return productName;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public String getDescription() {
		return description;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public void setProductPrice(int productPrice) {
		this.productPrice = productPrice;
	}

	public void setDescription(String description) {
		this.description = description;
	}
    
    
}
