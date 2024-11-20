package com.example.uipj.adapter.group;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.uipj.ui.activities.classes.ViewMembersFragment;
import com.example.uipj.ui.activities.classes.ViewSetsFragment;
import com.example.uipj.ui.fragments.library.FoldersFragment;
import com.example.uipj.ui.fragments.library.MyClassesFragment;
import com.example.uipj.ui.fragments.library.StudySetsFragment;

public class MyViewClassAdapter extends FragmentStatePagerAdapter {

    public MyViewClassAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) return new ViewMembersFragment();
        return new ViewSetsFragment();
        /*return switch (position) {
            case 1: new ViewMembersFragment();
            default: new ViewSetsFragment();
        };*/
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) return "SETS";
        else if (position == 1) return "MEMBERS";
        return "";
        /*return switch (position) {
            case 0 -> "SETS";
            case 1 -> "MEMBERS";
            default -> "";
        };*/
    }
}
