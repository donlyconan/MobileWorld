package com.team.mobileworld.core.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LocationService {
	public static final int VIETNAM = 1240 * 1000;

	public static final String BASE_URL_GOOGLE = "https://maps.googleapis.com/maps/";

	public static final String BASE_URL_DEVELOPER_MAPQUEST = "http://www.mapquestapi.com/";
	
	

	//https://maps.googleapis.com/maps/api/place/autocomplete/json?radius=1240000&types=address&key=AIzaSyBN9m6S4grySLSwI1nsDeLomFyRHz0MZo8&input=32%20kim%20ma
	@GET("api/place/autocomplete/json?radius="+VIETNAM+"&types=address&key=AIzaSyBN9m6S4grySLSwI1nsDeLomFyRHz0MZo8")
	Call<ResponseBody> searchPlaceLocation(@Query("input") String address);
	
	//http://www.mapquestapi.com/geocoding/v1/address?key=YWGumzZ1PWsG1aYqQYwTYAo86gbEAzJW&location=32%20Kim%20M%C3%A3%2C%20Kim%20Ma%2C%20Ba%20%C4%90%C3%ACnh%2C%20Hanoi%2C%20Vietnam
	@GET("geocoding/v1/address?key=YWGumzZ1PWsG1aYqQYwTYAo86gbEAzJW")
	Call<ResponseBody> searchGeocodeLocation(@Query("location") String location);

	
	
}
