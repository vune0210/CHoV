package com.example.uipj.adapter.viewpager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.uipj.ui.fragments.library.FoldersFragment;
import com.example.uipj.ui.fragments.library.MyClassesFragment;
import com.example.uipj.ui.fragments.library.StudySetsFragment;

public class MyViewPagerAdapter extends FragmentStatePagerAdapter {
    public MyViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) return new FoldersFragment();
        else if (position == 2) return new MyClassesFragment();
        return new StudySetsFragment();
        /* return switch (position) {
            case 1 -> new FoldersFragment();
            case 2 -> new MyClassesFragment();
            default -> new StudySetsFragment();
        }; */
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) return "Study sets";
        else if (position == 1) return "Folders";
        else if (position == 2) return "Classes";
        return "";
        /*return switch (position) {
            case 0 -> "Study sets";
            case 1 -> "Folders";
            case 2 -> "Classes";
            default -> "";
        };*/
    }
}