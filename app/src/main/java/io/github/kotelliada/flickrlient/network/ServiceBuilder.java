package io.github.kotelliada.flickrlient.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.github.kotelliada.flickrlient.model.Photo;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceBuilder {
    public static final String API_KEY = "0468d31f28d288781dc9fcade5146b5f";
    private static final String URL = "https://api.flickr.com/";

    private static JsonDeserializer<List<Photo>> customDeserializer = (json, typeOfT, context) -> {
        List<Photo> photoList = new ArrayList<>();
        JsonObject rootObject = json.getAsJsonObject();
        JsonObject photosObject = rootObject.getAsJsonObject("photos");
        JsonArray photoArray = photosObject.getAsJsonArray("photo");

        for (JsonElement jsonElement : photoArray) {
            JsonObject photoObject = jsonElement.getAsJsonObject();
            Photo photo = new Photo();
            photo.setId(photoObject.get("id").getAsString());
            photo.setTitle(photoObject.get("title").getAsString());
            photo.setUrl(photoObject.get("url_s").getAsString());
            photo.setLatitude(photoObject.get("latitude").getAsString());
            photo.setLongitude(photoObject.get("longitude").getAsString());
            photoList.add(photo);
        }

        return photoList;
    };

    private static GsonBuilder gsonBuilder = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<List<Photo>>() {
            }.getType(), customDeserializer);

    private static Gson customGson = gsonBuilder.create();

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create(customGson));

    private static Retrofit retrofit = retrofitBuilder.build();

    public static <T> T buildService(Class<T> service) {
        return retrofit.create(service);
    }
}