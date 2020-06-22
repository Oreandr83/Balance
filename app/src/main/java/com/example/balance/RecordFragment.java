package com.example.balance;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import api.Api;
import api.App;
import dialog.ConfirmationDialog;
import dialog.ConfirmationDialogListener;
import models.AddItemResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordFragment extends Fragment {

    private static final String TAG = "ItemFragment";
    private static final String TYPE_KEY = "type";
    public static final int REQUEST_CODE_ADD = 123;

    String name;
    String price;

    private String type;

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private Adapter adapter;
    private SwipeRefreshLayout refreshLayout;

    private Api api;
    private App app;

    public static RecordFragment newItemFragment(String type) {
        RecordFragment fragment = new RecordFragment();

        Bundle bundle = new Bundle();
        bundle.putString(RecordFragment.TYPE_KEY, type);
        bundle.putBoolean("key",true);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new Adapter();
        adapter.setListener(new AdapterListener());

        Bundle bundle = getArguments();
        type = bundle.getString(TYPE_KEY, Record.TYPE_EXPENSES);

        if (type.equals(Record.TYPE_UNKNOWN)) {
            throw new IllegalArgumentException("Unknown type");
        }
        app =(App)getActivity().getApplication();

        api = app.getApi();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_res, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.id_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);


        refreshLayout = view.findViewById(R.id.id_swipe);
        refreshLayout.setColorSchemeColors(Color.BLUE, Color.RED);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadData();
            }
        });

        loadData();

    }

    private void loadData() {
        //   Log.d(TAG, "loadData: current thread " + Thread.currentThread().getName());

        Call<List<Record>> call = api.getRecord(type);

        call.enqueue(new Callback<List<Record>>() {
            @Override
            public void onResponse(Call<List<Record>> call, Response<List<Record>> response) {
                adapter.setData(response.body());
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Record>> call, Throwable t) {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void addRecord(Record record){
      Call<AddItemResult> call = api.addRecord(record.price,record.name,record.type);

      call.enqueue(new Callback<AddItemResult>() {
          @Override
          public void onResponse(Call<AddItemResult> call, Response<AddItemResult> response) {
               AddItemResult result = response.body();
               if(result.status.equals("success")){
                   record.id = result.id;//got id from the server
                   adapter.addRecord(record);//add id to adapter
                   //in result return new addItemResult with new id
               }
          }

          @Override
          public void onFailure(Call<AddItemResult> call, Throwable t) {

          }
      });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_ADD) {
            if (data == null) {
                return;
            }

            Record record = data.getParcelableExtra("recd");
            if (record.type.equals(type)) {
                adapter.addRecord(record);
    //            addRecord(record);//if send to server
            }

        }
    }

    // ACTION MODE

    private ActionMode modeAction;

    //Array of dedicated records
    private void removeSelectedRecord(){
        for(int i = adapter.getSelectedRecords().size()-1; i >= 0; i--){
            adapter.remove(adapter.getSelectedRecords().get(i));//remove from ItemAdapter.List<Record> recordList
        }
        modeAction.finish();
    }

    private class AdapterListener implements RecordAdapterListener {

        @Override
        public void recordClick(Record record, int position) {
            Log.i(TAG, "recordClick: name = " + record.name + " position = " + position);
            if(isInActionMode()){
                toggleSelection(position);
            }
        }

        @Override
        public void recordLongClick(Record record, int position) {
            if(isInActionMode()){
                return;
            }
            Log.i(TAG, "recordLongClick: name = " + record.name + " position = " + position);
           modeAction =  ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
           toggleSelection(position);
        }
        private boolean isInActionMode(){
           return modeAction != null;
            }

            private void toggleSelection(int position){
            adapter.toggleSelection(position);
            }
        }

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {//is checked I to action mode
            MenuInflater inflater = new MenuInflater(getContext());
            inflater.inflate(R.menu.record_menu,menu);
           return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.remove:
                    showDialog();
                    break;
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.clearSelections();
           modeAction = null;
        }
    };
    private void showDialog(){
        ConfirmationDialog dialog = new ConfirmationDialog();
        dialog.show(getFragmentManager(),"ConfirmationDialog");
        dialog.setListener(new ConfirmationDialogListener() {
            @Override
            public void onPositiveBtnClicked() {
                removeSelectedRecord();
            }

            @Override
            public void onNegativeBtnClicked() {
              modeAction.finish();
            }
        });

    }
}