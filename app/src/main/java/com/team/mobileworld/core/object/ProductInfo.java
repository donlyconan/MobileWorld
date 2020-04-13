package com.team.mobileworld.core.object;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public abstract class ProductInfo {

	@SerializedName("cpu")
	private String cpu;

	@SerializedName("ram")
	private String ram;

	@SerializedName("screen")
	private String screen;

	@SerializedName("os")
	private String os;
	

	public ProductInfo(String cpu, String ram, String screen, String os) {
		super();
		this.cpu = cpu;
		this.ram = ram;
		this.screen = screen;
		this.os = os;
	}

	public abstract void toJson(ProductInfo info);

	public abstract JsonObject getObject(JsonObject json);

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getRam() {
		return ram;
	}

	public void setRam(String ram) {
		this.ram = ram;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

}
