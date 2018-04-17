package io.github.kotelliada.flickrlient.network;

import java.util.List;

import io.github.kotelliada.flickrlient.model.Photo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PhotoService {
    @GET("services/rest/?method=flickr.photos.search&format=json&nojsoncallback=1&extras=url_s,geo&has_geo=1&per_page=40")
    Call<List<Photo>> getRandomPhotos(@Query("api_key") String apiKey);
}