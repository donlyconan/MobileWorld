package com.team.mobileworld.core.object;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class Geocode {
	@SerializedName("linkId")
	private String id;
	@SerializedName("street")
	private String street;
	@SerializedName("latLng")
	private LatLng latlng;

	public Geocode() {
		// TODO Auto-generated constructor stub
	}

	public Geocode(String id, String street, LatLng latlng) {
		super();
		this.id = id;
		this.street = street;
		this.latlng = latlng;
	}
	
	@RequiresApi(api = Build.VERSION_CODES.N)
	public static List<Geocode> getListGeocode(ResponseBody response) throws JsonSyntaxException, IOException{
		List<Geocode> geocodes = new ArrayList<Geocode>(20);
		JsonObject json = (JsonObject) new JsonParser().parse(response.string());
		JsonArray array = json.getAsJsonArray("results");
		json = array.get(0).getAsJsonObject();
		array = json.get("locations").getAsJsonArray();
		array.forEach(e->geocodes.add(geocodeFromJson(e.getAsJsonObject())));
		return geocodes;
	}
	
	public static Geocode geocodeFromJson(JsonObject json) {
		JsonObject lat = json.get("latLng").getAsJsonObject();
		return new Geocode(
				json.get("linkId").getAsString()
				,json.get("street").getAsString()
				, LatLng.make(lat.get("lat").getAsDouble(), lat.get("lng").getAsDouble())
				);
	}

	@Override
	public String toString() {
		return "Geocode [id=" + id + ", street=" + street + ", latlng=" + latlng + "]";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public LatLng getLatlng() {
		return latlng;
	}

	public void setLatlng(LatLng latlng) {
		this.latlng = latlng;
	}

	public static class LatLng {
		@SerializedName("lat")
		private double lat;
		@SerializedName("lng")
		private double lng;

		public LatLng() {
			// TODO Auto-generated constructor stub
		}

		public LatLng(double lat, double lng) {
			super();
			this.lat = lat;
			this.lng = lng;
		}
		
		public static LatLng make(double x, double y)
		{
			return new LatLng(x,y);
		}

		@Override
		public String toString() {
			return "LatLng [lat=" + lat + ", lng=" + lng + "]";
		}

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public double getLng() {
			return lng;
		}

		public void setLng(double lng) {
			this.lng = lng;
		}

	}
}
