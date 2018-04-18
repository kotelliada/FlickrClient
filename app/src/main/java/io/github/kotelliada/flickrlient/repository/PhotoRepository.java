package io.github.kotelliada.flickrlient.repository;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import io.github.kotelliada.flickrlient.R;
import io.github.kotelliada.flickrlient.model.Photo;
import io.github.kotelliada.flickrlient.network.PhotoService;
import io.github.kotelliada.flickrlient.network.ServiceBuilder;
import io.github.kotelliada.flickrlient.utils.SingleEventLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoRepository {
    private static final String TAG = PhotoRepository.class.getSimpleName();
    private static PhotoRepository instance;
    private MutableLiveData<List<Photo>> photoList = new MutableLiveData<>();
    private SingleEventLiveData<Integer> errorMessage = new SingleEventLiveData<>();
    private PhotoService photoService;

    private PhotoRepository(PhotoService photoService) {
        this.photoService = photoService;
        getRandomPhotosFromService();
    }

    public static PhotoRepository getInstance(PhotoService photoService) {
        if (instance == null)
            instance = new PhotoRepository(photoService);
        return instance;
    }

    public MutableLiveData<List<Photo>> getPhotoList() {
        return photoList;
    }

    public MutableLiveData<Integer> getErrorMessage() {
        return errorMessage;
    }

    public void getRandomPhotosFromService() {
        photoService.getRandomPhotos(ServiceBuilder.API_KEY)
                .enqueue(new Callback<List<Photo>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Photo>> call, @NonNull Response<List<Photo>> response) {
                        if (response.isSuccessful())
                            photoList.setValue(response.body());
                        else
                            Log.e(TAG, "PhotoRepository.getRandomPhotosFromService(): " + response.message());
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Photo>> call, @NonNull Throwable t) {
                        errorMessage.setValue(R.string.no_connection);
                    }
                });
    }
}