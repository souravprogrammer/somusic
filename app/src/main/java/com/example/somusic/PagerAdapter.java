package com.example.somusic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter {
    private final int TAB_COUNT ;
    { TAB_COUNT = 2 ; }
    ArrayList<String> TabTitle = new ArrayList<String>() ;

    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        // Title of TABS
        TabTitle.add(new String("Songs")) ;
        TabTitle.add(new String("Album")) ;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TabTitle.get(position);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0 :
                return new SongsFrag() ;
            case 1 :
                return new AlbumFrag();
            default:
                return null ;
        }

    }
    @Override
    public int getCount() {
        return TAB_COUNT;
    }
}
