package io.github.kotelliada.flickrlient.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.github.kotelliada.flickrlient.R;
import io.github.kotelliada.flickrlient.ui.list.PhotoListFragment;
import io.github.kotelliada.flickrlient.ui.map.MapFragment;

class ViewPagerAdapter extends FragmentPagerAdapter {
    private final Context context;

    ViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return PhotoListFragment.newInstance();
            case 1:
                return MapFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.photos);
            case 1:
                return context.getResources().getString(R.string.map);
            default:
                return null;
        }
    }
}