package io.github.kotelliada.flickrlient.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import io.github.kotelliada.flickrlient.repository.PhotoRepository;

public class SharedViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final PhotoRepository repository;

    public SharedViewModelFactory(PhotoRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SharedViewModel(repository);
    }
}