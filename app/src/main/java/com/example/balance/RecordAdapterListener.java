package com.example.balance;

public interface RecordAdapterListener {

    void recordClick(Record record, int position);
    void recordLongClick(Record record, int position);//remove record
}
