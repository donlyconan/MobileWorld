package com.team.mobileworld.core.object;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class Place {
	@SerializedName("place_id")
	private String id;
	@SerializedName("description")
	private String description;
	@SerializedName("types")
	private List<String> types;

	public Place() {
		// TODO Auto-generated constructor stub
	}

	public Place(String id, String description, List<String> types) {
		super();
		this.id = id;
		this.description = description;
		this.types = types;
	}


	@RequiresApi(api = Build.VERSION_CODES.N)
	public static List<Place> getListPlace(ResponseBody response) throws JsonSyntaxException, IOException{
		List<Place> places = new ArrayList<Place>(20);
		JsonObject json = (JsonObject) new JsonParser().parse(response.string());
		JsonArray array = json.get("predictions").getAsJsonArray();
		array.forEach(e->places.add(new Gson().fromJson(e.getAsJsonObject(), Place.class)));
		return places;
	}
	

	@Override
	public String toString() {
		return "Place [id=" + id + ", description=" + description + ", types=" + types + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}


}
