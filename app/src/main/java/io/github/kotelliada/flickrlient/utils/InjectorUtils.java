package io.github.kotelliada.flickrlient.utils;

import io.github.kotelliada.flickrlient.network.PhotoService;
import io.github.kotelliada.flickrlient.network.ServiceBuilder;
import io.github.kotelliada.flickrlient.repository.PhotoRepository;
import io.github.kotelliada.flickrlient.viewmodel.SharedViewModelFactory;

public class InjectorUtils {
    public static PhotoRepository provideRepository() {
        PhotoService photoService = ServiceBuilder.buildService(PhotoService.class);
        return PhotoRepository.getInstance(photoService);
    }

    public static SharedViewModelFactory provideSharedViewModelFactory() {
        return new SharedViewModelFactory(provideRepository());
    }
}