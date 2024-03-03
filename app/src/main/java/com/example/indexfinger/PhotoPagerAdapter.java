package com.example.indexfinger;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class PhotoPagerAdapter extends FragmentPagerAdapter {
    private final List<String> photoUris;

    public PhotoPagerAdapter(FragmentManager fm, List<String> photoUris) {
        super(fm);
        this.photoUris = photoUris;
    }

    @Override
    public Fragment getItem(int position) {
        return PictureFragment.newInstance(photoUris.get(position));
    }

    @Override
    public int getCount() {
        return photoUris.size();
    }
}
