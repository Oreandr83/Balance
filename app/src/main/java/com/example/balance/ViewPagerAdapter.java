package com.example.balance;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public static final int PAGE_INCOMES = 0;
    public static final int PAGE_EXPENSES = 1;
    public static final int PAGE_BALANCE = 2;

    String[] array;


    public ViewPagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        array = context.getResources().getStringArray(R.array.tab_title);

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

       switch (position) {
            case PAGE_INCOMES:
              return  RecordFragment.newItemFragment(Record.TYPE_INCOMES);
            case PAGE_EXPENSES:
                return RecordFragment.newItemFragment(Record.TYPE_EXPENSES);
           case PAGE_BALANCE:
                return new BalanceFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return array[position];
    }
}
