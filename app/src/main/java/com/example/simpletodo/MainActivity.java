package com.example.simpletodo;

import static android.os.FileUtils.*;

import static org.apache.commons.io.FileUtils.readLines;
import static org.apache.commons.io.FileUtils.writeLines;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item text" ;
    public static final String KEY_ITEM_POSITION = "item position" ;
    public static final int EDIT_TEXT_CODE = 20 ;
    List<String> items ;

    Button btnAdd;
    EditText etItem ;
    RecyclerView rvItems ;
    ItemsAdaptor itemsAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);



        loadItems();

        ItemsAdaptor.OnLongClickListener OnLongClickListener = new ItemsAdaptor.OnLongClickListener(){
            @Override
            public void onItemLongClicked(int position) {
                //Delete the item from the model
                items.remove(position);
                //Notify the adaptor
                itemsAdaptor.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "item was Removed" , Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        ItemsAdaptor.OnClickListener onClickListener = new ItemsAdaptor.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity" , "Single click at position" + position) ;
                //Create the new activity
                Intent i = new Intent(MainActivity.this, EditActivity.class) ;
                //pass the data being edited
                i.putExtra(KEY_ITEM_TEXT , items.get(position)) ;
                i.putExtra(KEY_ITEM_POSITION, position);
                //display the acitivity
                startActivityForResult(i, EDIT_TEXT_CODE);
            }
        };
        itemsAdaptor = new ItemsAdaptor(items, OnLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdaptor);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String todoItem = etItem.getText().toString();
                //Add item to the model
                items.add(todoItem);
                //Notify user an item is inserted
                itemsAdaptor.notifyItemInserted(items.size() - 1 );
                etItem.setText("");
                Toast.makeText(getApplicationContext(), "item was added" , Toast.LENGTH_SHORT).show();
                saveItems();

            }
        });
    }

    //handle the result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE) {
            //retrieve the updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT) ;

            //extract the original position of the edited item from the position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION) ;

            //update the model at the right position with new item text
            items.set(position, itemText);
            //notify the adaptor
            itemsAdaptor.notifyItemChanged(position);

            //persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfuly", Toast.LENGTH_SHORT).show();

        } else {
            Log.w("MainActivity", "unknown call to onActivityResult") ;
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    //This function will load item bt reading every line of the data file
    private void loadItems(){
        try {
            items = new ArrayList<>(readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainAvtivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    //This function saves items by writing them into the data file
    private void saveItems(){
        try {
            writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainAvtivity", "Error writing items", e);
        }
    }
}