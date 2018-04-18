package io.github.kotelliada.flickrlient.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

import io.github.kotelliada.flickrlient.R;
import io.github.kotelliada.flickrlient.utils.ConnectionUtils;
import io.github.kotelliada.flickrlient.utils.InjectorUtils;
import io.github.kotelliada.flickrlient.utils.QueryPreferences;
import io.github.kotelliada.flickrlient.viewmodel.SharedViewModel;

public class PhotoListFragment extends Fragment {
    private Context context;
    private SharedViewModel viewModel;

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
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.list_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(context.getResources().getColor(R.color.colorAccent));

        if (ConnectionUtils.isNetworkAvailableAndConnected(context))
            swipeRefreshLayout.setRefreshing(true);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        PhotoAdapter recyclerViewAdapter = new PhotoAdapter(new ArrayList<>());
        recyclerView.setAdapter(recyclerViewAdapter);

        viewModel = ViewModelProviders.of(getActivity(), InjectorUtils.provideSharedViewModelFactory()).get(SharedViewModel.class);
        viewModel.getPhotoList().observe(this, photoList -> {
            recyclerViewAdapter.setPhotoList(photoList);
            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);
        });

        viewModel.getErrorMessage().observe(this, errorMessageId -> {
            Snackbar.make(recyclerView, context.getResources().getString(errorMessageId), Snackbar.LENGTH_LONG).show();
            if (swipeRefreshLayout.isRefreshing())
                swipeRefreshLayout.setRefreshing(false);
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (ConnectionUtils.isNetworkAvailableAndConnected(context))
                getPhotosNetwork();
            else {
                Snackbar.make(recyclerView, context.getResources().getString(R.string.no_connection), Snackbar.LENGTH_LONG).show();
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
            }
        });
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
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void getPhotosNetwork() {
        String query = QueryPreferences.getStoredQuery(context);
        if (query == null)
            viewModel.fetchRandomPhotosFromNetwork();
        else
            viewModel.fetchPhotosByQuery(query);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
    }
}