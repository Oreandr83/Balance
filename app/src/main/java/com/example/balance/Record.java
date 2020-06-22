package com.example.balance;

import android.os.Parcel;
import android.os.Parcelable;

public class Record implements Parcelable {
    //т.к. сервер воспринимает строковые значения
   public static final String TYPE_UNKNOWN = "unknown";
    public static final String TYPE_INCOMES = "incomes";
    public static final String TYPE_EXPENSES = "expenses";

    public int id;
    public String name;
    public String price;
    public String type;



   public Record(String name, String price, String type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    protected Record(Parcel in) {
        id = in.readInt();
        name = in.readString();
        price = in.readString();
        type = in.readString();
    }

    public static final Creator<Record> CREATOR = new Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
         dest.writeInt(id);
         dest.writeString(name);
         dest.writeString(price);
         dest.writeString(type);
    }
}
