package com.example.balance;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

class Adapter extends RecyclerView.Adapter<Adapter.ItemHolder>{

    List<Record> recordList = new ArrayList<>();

    private RecordAdapterListener listener = null;

    public void setListener(RecordAdapterListener list){
        listener = list;
    }


  public void setData(List<Record> list){
      recordList = list;
      notifyDataSetChanged();
  }

  public void addRecord(Record record){
      recordList.add(record);
      notifyItemInserted(recordList.size());
//      notifyItemInserted(0);//if i do request to server
  }
  void clearSelections(){
        selections.clear();
        notifyDataSetChanged();
  }
  int getSelectedRecordCount(){
        return selections.size();
  }
  List<Integer> getSelectedRecords(){
        List<Integer> records = new ArrayList<>(selections.size());
        for(int i = 0; i<selections.size();i++){
            records.add(selections.keyAt(i));
        }
        return records;
  }

  Record remove(int position){
        final Record record = recordList.remove(position);
        notifyItemRemoved(position);
        return record;
  }


    @NonNull
    @Override
    public Adapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Record rec = recordList.get(position);
        holder.bind(rec,position,listener,selections.get(position,false));
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    //highlighting the record in brown
    private SparseBooleanArray selections = new SparseBooleanArray();
// if the record highlighted then remove the position
    public void toggleSelection(int position) {
        if(selections.get(position,false)){
            selections.delete(position);
            //if the record not highlighted then highlighted it
        }else {
            selections.put(position,true);
        }
        //Let the adapter know about the changes
        notifyItemChanged(position);
    }

    static class ItemHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView price;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.id_name);
            price = itemView.findViewById(R.id.id_price);
        }

        public void bind(final Record record,final int position, final RecordAdapterListener listAdapter, boolean selected){
            name.setText(record.name);
            price.setText(record.price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  if(null != listAdapter){
                      listAdapter.recordClick(record,position);
                  }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(listAdapter != null){
                        listAdapter.recordLongClick(record,position);
                    }
                    return true;
                }
            });
            //Highlight the record if selected
            itemView.setActivated(selected);
            //Next create a background that, depending on the choice,
            //will change color

        }


    }

}
