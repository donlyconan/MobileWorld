package com.team.mobileworld.core.object;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class LaptopInfo extends ProductInfo {

	@SerializedName("cardscreen")
	private String cardscreen;
	
	@SerializedName("connector")
	private String connector;
	
	@SerializedName("harddrive")
	private String harddrive;
	
	@SerializedName("design")
	private String design;
	
	@SerializedName("size")
	private String size;
	
	@SerializedName("relese")
	private String relese;
	
	

	public LaptopInfo(String cpu, String ram, String screen, String os, String cardscreen, String connector,
			String harddrive, String design, String size, String relese) {
		super(cpu, ram, screen, os);
		this.cardscreen = cardscreen;
		this.connector = connector;
		this.harddrive = harddrive;
		this.design = design;
		this.size = size;
		this.relese = relese;
	}

	@Override
	public void toJson(ProductInfo info) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JsonObject getObject(JsonObject json) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	@Override
	public String toString() {
		return "LaptopInfo [cardscreen=" + cardscreen + ", connector=" + connector + ", harddrive=" + harddrive
				+ ", design=" + design + ", size=" + size + ", relese=" + relese + "]";
	}

	public String getCardscreen() {
		return cardscreen;
	}

	public void setCardscreen(String cardscreen) {
		this.cardscreen = cardscreen;
	}

	public String getConnector() {
		return connector;
	}

	public void setConnector(String connector) {
		this.connector = connector;
	}

	public String getHarddrive() {
		return harddrive;
	}

	public void setHarddrive(String harddrive) {
		this.harddrive = harddrive;
	}

	public String getDesign() {
		return design;
	}

	public void setDesign(String design) {
		this.design = design;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getRelese() {
		return relese;
	}

	public void setRelese(String relese) {
		this.relese = relese;
	}


	
}
