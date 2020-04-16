package com.team.mobileworld.core.object;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class SmartphoneInfo extends ProductInfo {
	
//	@SerializedName("cpu")
//	private String cpu;
//
//	@SerializedName("ram")
//	private String ram;
//
//	@SerializedName("screen")
//	private String screen;
//
//	@SerializedName("os")
//	private String os;
	
	@SerializedName("backcamera")
	private String backcamera;
	
	@SerializedName("frontcamera")
	private String frontcamera;
	
	@SerializedName("internalmemmory")
	private String internalmemmory;
	
	@SerializedName("memorystick")
	private String memorystick;
	
	@SerializedName("sim")
	private String sim;
	
	@SerializedName("batery")
	private String batery;
	
	

	public SmartphoneInfo(String cpu, String ram, String screen, String os, String backcamera, String frontcamera,
			String internalmemmory, String memorystick, String sim, String batery) {
//		this.cpu = cpu;
//		this.ram = ram;
//		this.screen = screen;
//		this.os = os;
		super(cpu,ram,screen,os);
		this.backcamera = backcamera;
		this.frontcamera = frontcamera;
		this.internalmemmory = internalmemmory;
		this.memorystick = memorystick;
		this.sim = sim;
		this.batery = batery;
	}


	@Override
	public String toString() {
		return "SmartphoneInfo [backcamera=" + backcamera + ", frontcamera=" + frontcamera + ", internalmemmory="
				+ internalmemmory + ", memorystick=" + memorystick + ", sim=" + sim + ", batery=" + batery + "]";
	}

	public String getBackcamera() {
		return backcamera;
	}

	public void setBackcamera(String backcamera) {
		this.backcamera = backcamera;
	}

	public String getFrontcamera() {
		return frontcamera;
	}

	public void setFrontcamera(String frontcamera) {
		this.frontcamera = frontcamera;
	}

	public String getInternalmemmory() {
		return internalmemmory;
	}

	public void setInternalmemmory(String internalmemmory) {
		this.internalmemmory = internalmemmory;
	}

	public String getMemorystick() {
		return memorystick;
	}

	public void setMemorystick(String memorystick) {
		this.memorystick = memorystick;
	}

	public String getSim() {
		return sim;
	}

	public void setSim(String sim) {
		this.sim = sim;
	}

	public String getBatery() {
		return batery;
	}

	public void setBatery(String batery) {
		this.batery = batery;
	}


	@Override
	public void toJson(ProductInfo info) {

	}

	@Override
	public JsonObject getObject(JsonObject json) {
		return null;
	}
}
