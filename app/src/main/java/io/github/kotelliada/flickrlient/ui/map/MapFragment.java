package io.github.kotelliada.flickrlient.ui.map;

import android.Manifest;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.util.List;

import io.github.kotelliada.flickrlient.R;
import io.github.kotelliada.flickrlient.model.Photo;
import io.github.kotelliada.flickrlient.utils.InjectorUtils;
import io.github.kotelliada.flickrlient.utils.QueryPreferences;
import io.github.kotelliada.flickrlient.viewmodel.SharedViewModel;

public class MapFragment extends Fragment {
    private static final String TAG = "MapFragment";
    private static final int ERROR_DIALOG_REQUEST = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private Context context;
    private boolean locationPermissionGranted;
    private GoogleMap googleMap;
    private SharedViewModel viewModel;

    public static MapFragment newInstance() {
        Bundle args = new Bundle();
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (isGooglePlayServicesAvailable()) {
            getLocationPermission();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        SearchView searchView = (SearchView) menu.getItem(0).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                QueryPreferences.setStoredQuery(context, query);
                getPhotosNetwork();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchClickListener(v -> {
            String query = QueryPreferences.getStoredQuery(context);
            if (query != null)
                searchView.setQuery(query, false);
        });
        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(v -> {
            EditText et = searchView.findViewById(R.id.search_src_text);
            et.setText("");
            QueryPreferences.clearPreferences(context);
            getPhotosNetwork();
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }

    private boolean isGooglePlayServicesAvailable() {
        int availability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context);

        if (availability == ConnectionResult.SUCCESS)
            return true;
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(availability)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), availability, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else
            Toast.makeText(context, "You can not use Google Maps", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(googleMap -> {
            this.googleMap = googleMap;
            if (locationPermissionGranted) {
                getDeviceLocation();
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                this.googleMap.setMyLocationEnabled(true);
                this.googleMap.getUiSettings().setZoomGesturesEnabled(true);
                viewModel = ViewModelProviders.of(getActivity(), InjectorUtils.provideSharedViewModelFactory()).get(SharedViewModel.class);
                viewModel.getPhotoList().observe(this, photoList -> {
                    this.googleMap.clear();
                    addMarkers(photoList);
                });
            }
        });
    }

    private void addMarkers(List<Photo> photoList) {
        if (googleMap != null) {
            for (Photo photo : photoList) {
                if (!TextUtils.isEmpty(photo.getLatitude())
                        && !TextUtils.isEmpty(photo.getLongitude())) {
                    double latitude = Double.parseDouble(photo.getLatitude());
                    double longitude = Double.parseDouble(photo.getLongitude());
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude, longitude))
                            .title(photo.getTitle()));
                }
            }
        }
    }

    private void getDeviceLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(task -> {
                    if (!task.isSuccessful() || task.getResult() == null)
                        Toast.makeText(context, "Unable to get current location", Toast.LENGTH_SHORT).show();
                });
            } else
                getLocationPermission();
        } catch (SecurityException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            initMap();
        } else
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    private void getPhotosNetwork() {
        String query = QueryPreferences.getStoredQuery(context);
        if (query == null)
            viewModel.fetchRandomPhotosFromNetwork();
        else
            viewModel.fetchPhotosByQuery(query);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                    initMap();
                }
            }
        }
    }
}