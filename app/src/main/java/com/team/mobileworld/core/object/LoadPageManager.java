package com.team.mobileworld.core.object;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class LoadPageManager {
	@SerializedName("list")
	private List<ItemList> list;
	
	@SerializedName("page")
	private int page = 0;

	public LoadPageManager(List<ItemList> list, int page) {
		super();
		this.list = list;
		this.page = page;
	}

	public List<ItemList> getList() {
		return list;
	}

	public void setList(List<ItemList> list) {
		this.list = list;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
	
	
	
}
