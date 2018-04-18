package io.github.kotelliada.flickrlient.ui.map;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import io.github.kotelliada.flickrlient.R;

public class MapFragment extends Fragment {
    private static final String TAG = "MapFragment";
    private static final int ERROR_DIALOG_REQUEST = 1;
    private Context context;

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (isGooglePlayServicesAvailable()) {
            
        }
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
}