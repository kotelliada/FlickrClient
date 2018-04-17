package io.github.kotelliada.flickrlient.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.github.kotelliada.flickrlient.R;
import io.github.kotelliada.flickrlient.utils.InjectorUtils;
import io.github.kotelliada.flickrlient.viewmodel.SharedViewModel;

public class PhotoListFragment extends Fragment {
    private Context context;

    public static PhotoListFragment newInstance() {
        Bundle args = new Bundle();
        PhotoListFragment fragment = new PhotoListFragment();
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
        return inflater.inflate(R.layout.fragment_photo_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.list_swipe_refresh_layout);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        PhotoAdapter recyclerViewAdapter = new PhotoAdapter(new ArrayList<>());
        recyclerView.setAdapter(recyclerViewAdapter);

        SharedViewModel viewModel = ViewModelProviders.of(getActivity(), InjectorUtils.provideSharedViewModelFactory()).get(SharedViewModel.class);
        viewModel.getPhotoList().observe(this, photoList -> {
            recyclerViewAdapter.setPhotoList(photoList);
            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);
        });

        swipeRefreshLayout.setOnRefreshListener(viewModel::fetchRandomPhotosFromNetwork);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }
}