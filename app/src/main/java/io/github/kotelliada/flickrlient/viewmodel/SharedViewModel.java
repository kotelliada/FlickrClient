package io.github.kotelliada.flickrlient.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import io.github.kotelliada.flickrlient.model.Photo;
import io.github.kotelliada.flickrlient.repository.PhotoRepository;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<List<Photo>> photoList = new MutableLiveData<>();
    private PhotoRepository repository;

    SharedViewModel(PhotoRepository repository) {
        this.repository = repository;
        this.repository.getPhotoList().observeForever(photoList1 -> this.photoList.setValue(photoList1));
    }

    public LiveData<List<Photo>> getPhotoList() {
        return photoList;
    }

    public void fetchRandomPhotosFromNetwork() {
        this.repository.getRandomPhotosFromService();
    }
}