package io.github.kotelliada.flickrlient.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceBuilder {
    public static final String API_KEY = "0468d31f28d288781dc9fcade5146b5f";
    private static final String URL = "https://api.flickr.com/";

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    public static <T> T buildService(Class<T> service) {
        return retrofit.create(service);
    }
}