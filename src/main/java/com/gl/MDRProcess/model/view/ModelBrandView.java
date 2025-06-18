package com.gl.MDRProcess.model.view;

public class ModelBrandView {
	
	private String brandName;
	
	private String modelName;
	
	public ModelBrandView() {}
	
	public ModelBrandView(String modelName, String brandName) {
		this.brandName = brandName;
		this.modelName   = modelName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
}
