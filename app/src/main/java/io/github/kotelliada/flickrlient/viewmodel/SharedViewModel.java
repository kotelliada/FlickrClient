package io.github.kotelliada.flickrlient.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import io.github.kotelliada.flickrlient.model.Photo;
import io.github.kotelliada.flickrlient.repository.PhotoRepository;
import io.github.kotelliada.flickrlient.utils.SingleEventLiveData;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<Photo>> photoList = new MutableLiveData<>();
    private final SingleEventLiveData<Integer> errorMessage = new SingleEventLiveData<>();
    private final PhotoRepository repository;

    SharedViewModel(PhotoRepository repository) {
        this.repository = repository;
        this.repository.getPhotoList().observeForever(this.photoList::setValue);
        this.repository.getErrorMessage().observeForever(this.errorMessage::setValue);
    }

    public LiveData<List<Photo>> getPhotoList() {
        return photoList;
    }

    public MutableLiveData<Integer> getErrorMessage() {
        return errorMessage;
    }

    public void fetchRandomPhotosFromNetwork() {
        this.repository.getRandomPhotosFromService();
    }

    public void fetchPhotosByQuery(String query) {
        this.repository.getPhotosByQuery(query);
    }
}