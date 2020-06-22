package com.example.balance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private ViewPager pager;
    private TabLayout tabLayout;
    private Toolbar bar;
    private FloatingActionButton fab;
    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         pager = findViewById(R.id.id_pager);
        tabLayout = findViewById(R.id.id_tablayout);

         ViewPagerAdapter mainAdapter = new ViewPagerAdapter(getSupportFragmentManager(),this);
         pager.setAdapter(mainAdapter);
         pager.addOnPageChangeListener(this);

         tabLayout.setupWithViewPager(pager);

         bar = findViewById(R.id.id_toolbar);
       // setSupportActionBar(bar);
      // getSupportActionBar().setDisplayShowTitleEnabled(true);

        fab = findViewById(R.id.id_float_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int currentPager = pager.getCurrentItem();
                String type = null;
                if(currentPager == ViewPagerAdapter.PAGE_INCOMES){
                    type = Record.TYPE_INCOMES;
                }else if(currentPager == ViewPagerAdapter.PAGE_EXPENSES){
                    type = Record.TYPE_EXPENSES;
                }

                Intent intent = new Intent(MainActivity.this,AddItemActivity.class);
                intent.putExtra(AddItemActivity.TYPE_KEY,type);
                startActivityForResult(intent, RecordFragment.REQUEST_CODE_ADD);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //if token save I turn to the main screen
     /*   if (((App) getApplication()).isAuthorise()) {
            initUI();
        } else {
               Intent intent  = new Intent(this,AuthActivity.class);
        startActivity(intent);
        }*/
    }
 //implementation of the home page only when registered
  /*  private void initUI(){
        MainPagerAdapter mainAdapter = new MainPagerAdapter(getSupportFragmentManager(),this);
        pager.setAdapter(mainAdapter);
    }*/

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case ViewPagerAdapter.PAGE_INCOMES:
            case ViewPagerAdapter.PAGE_EXPENSES:
                fab.show();
                break;
            case ViewPagerAdapter.PAGE_BALANCE:
                fab.hide();
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
          switch (state){
              case ViewPager.SCROLL_STATE_IDLE:
                  fab.setEnabled(true);
                  break;
              case ViewPager.SCROLL_STATE_DRAGGING:
              case ViewPager.SCROLL_STATE_SETTLING:
                  fab.setEnabled(false);
                  break;
          }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode,resultCode,data);

        }
    }
    //Hiding actionMode
    @Override
    public void onSupportActionModeStarted(@NonNull ActionMode mode) {
        super.onSupportActionModeStarted(mode);
        fab.hide();
        actionMode = mode;
    }

    @Override
    public void onSupportActionModeFinished(@NonNull ActionMode mode) {
        super.onSupportActionModeFinished(mode);
        fab.show();
        actionMode = null;
    }
}
