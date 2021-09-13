package com.example.simpletodo;

import android.os.FileUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

// Responsible for isplaying data from the model into a row in the ecycle view
public class ItemsAdaptor extends RecyclerView.Adapter<ItemsAdaptor.ViewHolder> {

    public interface OnClickListener {
        void onItemClicked(int position);

    }

    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }



    List<String> items ;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;
    public ItemsAdaptor(List<String> items , OnLongClickListener longClickListener, OnClickListener clickListener) {
        this.items = items ;
        this.longClickListener = longClickListener ;
        this.clickListener = clickListener ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //use layout inflator to inflate a view

        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent,  false);
        //wrap it inside a View Holder adn return it
        return new ViewHolder(todoView);
    }

    //Responsible for binding data to a particular view Holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Grab the item at the position
        String item = items.get(position);

        //Bind the item into the specified view Holder
        holder.bind(item);

    }
    // Tells the recycle view how many items are in the list
    @Override
    public int getItemCount() {
        return items.size();
    }

    //container to provide easy access to view that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem ;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem  = itemView.findViewById(android.R.id.text1);
        }

        //Update the view inside of the view holder with this data
        public void bind(String item) {
            tvItem.setText(item);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //Notify the listener which position was long pressed.
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }

}
