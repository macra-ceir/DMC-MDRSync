package com.gl.MDRProcess.model.view;

public class BrandView {
	
	private String brandName;
	
	private Integer brandId;
	
	public BrandView() {}
	
	public BrandView(String brandName, Integer brandId) {
		this.brandName = brandName;
		this.brandId   = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	@Override
	public String toString() {
		return "BrandView [brandName=" + brandName + ", brandId=" + brandId + "]";
	}
	
}
