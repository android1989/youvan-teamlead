package com.xwaydesigns.youvanteamlead.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.xwaydesigns.youvanteamlead.Fragments.DashBoardFragment;
import com.xwaydesigns.youvanteamlead.Fragments.PendingItemsFragment;
import com.xwaydesigns.youvanteamlead.Fragments.ProfileFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter
{
    public ViewPagerAdapter(@NonNull FragmentManager fm)
    {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {switch (position)
    {
        case 0:
            return new DashBoardFragment();
        case 1:
            return new PendingItemsFragment();
        case 2:
            return new ProfileFragment();
    }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
