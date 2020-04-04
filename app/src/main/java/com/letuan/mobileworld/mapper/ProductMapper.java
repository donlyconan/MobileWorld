package com.letuan.mobileworld.mapper;

import com.letuan.mobileworld.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ProductMapper {
    public static void mapper(JSONArray response, List<Product> products) {
        int id = 0;
        String name = "";
        Integer price = 0;
        String image = "";
        String description = "";
        int categoryid = 0;
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject jsonObject = response.getJSONObject(i);
                id = jsonObject.getInt("id");
                name = jsonObject.getString("name");
                price = jsonObject.getInt("price");
                image = jsonObject.getString("image");
                description = jsonObject.getString("description");
                categoryid = jsonObject.getInt("categoryid");
                products.add(new Product(id, name, price, image, description, categoryid));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
