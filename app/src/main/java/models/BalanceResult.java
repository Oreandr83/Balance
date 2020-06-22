package models;

import com.google.gson.annotations.SerializedName;

public class BalanceResult {

    public String status;
    @SerializedName("total_expenses")
    public int expense;
    @SerializedName("total_income")
    public int income;
}

//{"status":"success","total_expenses":"123.23","total_income":"400"}
