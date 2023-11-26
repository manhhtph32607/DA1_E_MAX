package com.example.da1_e_max.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.da1_e_max.fragment.admin.AdminAccountFragment;
import com.example.da1_e_max.fragment.admin.AdminFeedbackFragment;
import com.example.da1_e_max.fragment.admin.AdminHomeFragment;
import com.example.da1_e_max.fragment.admin.AdminOrderFragment;


public class AdminViewPagerAdapter extends FragmentStateAdapter {

    public AdminViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AdminHomeFragment();

            case 1:
                return new AdminFeedbackFragment();

            case 2:
                return new AdminOrderFragment();

            case 3:
                return new AdminAccountFragment();

            default:
                return new AdminHomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
