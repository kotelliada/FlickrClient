package io.github.kotelliada.flickrlient.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import io.github.kotelliada.flickrlient.model.Photo;
import io.github.kotelliada.flickrlient.repository.PhotoRepository;
import io.github.kotelliada.flickrlient.utils.SingleEventLiveData;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<List<Photo>> photoList = new MutableLiveData<>();
    private SingleEventLiveData<Integer> errorMessage = new SingleEventLiveData<>();
    private PhotoRepository repository;

    SharedViewModel(PhotoRepository repository) {
        this.repository = repository;
        this.repository.getPhotoList().observeForever(photoList1 -> this.photoList.setValue(photoList1));
        this.repository.getErrorMessage().observeForever(s -> this.errorMessage.setValue(s));
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
}