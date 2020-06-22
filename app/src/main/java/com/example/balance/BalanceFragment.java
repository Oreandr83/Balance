package com.example.balance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Random;

import api.Api;
import api.App;
import models.BalanceResult;

public class BalanceFragment extends Fragment {

    private static final String TAG = "BalanceFragment";

    private TextView total;
    private TextView expense;
    private TextView income;
    private DiagramView dgView;

    private Api api;
    private App app;

    //request to the surf 1
 /*   @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (App)getActivity().getApplication();
        api = app.getApi();
    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.balance_fragment, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        total = view.findViewById(R.id.total);
        expense = view.findViewById(R.id.expense);
        income = view.findViewById(R.id.incomes);
        dgView = view.findViewById(R.id.diagram);

        //request to the internet
      updateData();
    }



    private void updateData() {
//request to the serf 2
 /*       Call<BalanceResult> call = api.balance();

        call.enqueue(new Callback<BalanceResult>() {
            @Override
            public void onResponse(Call<BalanceResult> call, Response<BalanceResult> response) {
              BalanceResult result = response.body();

              total.setText(getString(R.string.price,result.income - result.expense));
              expense.setText(getString(R.string.price,result.expense));
              income.setText(getString(R.string.price,result.income));
              dgView.update(result.income,result.expense);

            }

            @Override
            public void onFailure(Call<BalanceResult> call, Throwable t) {

            }
        });*/


      Random random = new Random();
      BalanceResult result = new BalanceResult();
      result.expense = random.nextInt(40000);
       result.income = random.nextInt(70000);

       total.setText(getString(R.string.price,result.income - result.expense));
        expense.setText(getString(R.string.price,result.expense));
        income.setText(getString(R.string.price,result.income));
        dgView.update(result.income,result.expense);
    }
}
