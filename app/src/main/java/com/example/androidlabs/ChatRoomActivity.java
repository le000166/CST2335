package com.example.androidlabs;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    ArrayList<Messages> chatList = new ArrayList<>();


    MyOwnAdapter myAdapter;

    public static final String ACTIVITY_NAME = "activity_chatroom";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chatroom);


        //Get the fields from the screen:
        EditText message = (EditText) findViewById(R.id.editText5);
        Button sendButton = (Button) findViewById(R.id.sendbutton);
        Button receiveButton = (Button) findViewById(R.id.receivebutton);
        ListView theList = (ListView) findViewById(R.id.the_list);


        //get a database:
        MyDatabaseOpenHelper dbOpener = new MyDatabaseOpenHelper(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();

        //query all the results from the database:
        String [] columns = {MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_MESSAGE, MyDatabaseOpenHelper.COL_BOOLEAN};
        Cursor results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);



        //find the column indices:
        int messageColumnIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_MESSAGE);
        int idColumnIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);
        int checkColumnIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_BOOLEAN);



        while(results.moveToNext())
        {
            String messageData = results.getString(messageColumnIndex);
            int checkData = results.getInt(checkColumnIndex);
            boolean isSend = true;

            if (checkData == 0) {
                isSend = false;
            }

            long idData = results.getLong(idColumnIndex);

            //add the new Message to the array list:
            chatList.add(new Messages(messageData, idData, isSend));
        }



        //create an adapter object and send it to the listVIew
        myAdapter = new MyOwnAdapter();
        theList.setAdapter(myAdapter);
        theList.setSelection(myAdapter.getCount()-1);

        //SENDING BUTTON ON CLICK
        sendButton.setOnClickListener(clk -> {
            String sendMessage = message.getText().toString();

            ContentValues newRowValues = new ContentValues();
            //put string name in the NAME column:
            newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE, sendMessage);
            //put string email in the EMAIL column:
            newRowValues.put(MyDatabaseOpenHelper.COL_BOOLEAN, 1);
            //insert in the database:
            long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);
            //now you have the newId, you can create the Message object
            Messages messages = new Messages(sendMessage, newId, true);

            //add the new message to the list:
            chatList.add(messages);
            //update the listView:
            myAdapter.notifyDataSetChanged();

            //show up at the most recent added content eveything added
            theList.setSelection(myAdapter.getCount()-1);
            message.setText("");

            //show a notification: first parameter is any view on screen. second parameter is the text. Third parameter is the length (SHORT/LONG)
            //Snackbar.make(sendButton, "Message is sent from SEND", Snackbar.LENGTH_SHORT).show();
            //
        });

        //RECEIVER BUTTON ON CLICK
        receiveButton.setOnClickListener(clk -> {

            String receiveMessage = message.getText().toString();
            ContentValues newRowValues = new ContentValues();
            //put string name in the NAME column:
            newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGE, receiveMessage);
            //put string email in the EMAIL column:
            newRowValues.put(MyDatabaseOpenHelper.COL_BOOLEAN, 0);
            //insert in the database:
            long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);
            //now you have the newId, you can create the Message object
            Messages messages = new Messages(receiveMessage, newId, false);

            //add the new message to the list:
            chatList.add(messages);
            //update the listView:
            myAdapter.notifyDataSetChanged();

            //show up at the most recent added content eveything added
            theList.setSelection(myAdapter.getCount()-1);
            message.setText("");

            //show a notification: first parameter is any view on screen. second parameter is the text. Third parameter is the length (SHORT/LONG)
            //Snackbar.make(receiveButton, "Message is sent from Receive", Snackbar.LENGTH_SHORT).show();
            //
        });


        results.moveToFirst();
//        int hello = MyDatabaseOpenHelper.VERSION_NUM;
//        Log.e(String.valueOf(hello), " Version num");

        printCursor(results);
        Log.e(ACTIVITY_NAME, "In function: OnCreate()");
    }

    protected void printCursor(Cursor c) {

        Log.e(String.valueOf(MyDatabaseOpenHelper.VERSION_NUM), "Database version number");

        Log.e(String.valueOf(c.getColumnCount()), "Number of columns");

        String[] columns = c.getColumnNames();

        for (String name : columns
             ) {
            Log.d(name, "Column of the cursor");
        }

        Log.e(String.valueOf(c.getCount()), "number of results in the cursor");

        c.moveToFirst();
        while (c.moveToNext()) {
            Log.e(c.getString(c.getColumnIndex(MyDatabaseOpenHelper.COL_MESSAGE)), "printCursor: Message");
            Log.e(String.valueOf(c.getInt(c.getColumnIndex(MyDatabaseOpenHelper.COL_ID))), "printCursor: ColumnID");
            Log.e(String.valueOf(c.getInt(c.getColumnIndex(MyDatabaseOpenHelper.COL_BOOLEAN))), "printCursor: IsSend");
        }
    }

    //This class needs 4 functions to work properly:
    protected class MyOwnAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return chatList.size();
        }

        public Messages getItem(int position) {
            return chatList.get(position);
        }


        public View getView(int position, View old, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View rowView;
            TextView rowMessage;

            Messages thisRow = getItem(position);

            if (getItem(position).isSend()) {
                rowView = inflater.inflate(R.layout.left_row, parent, false);
                rowMessage = (TextView) rowView.findViewById(R.id.left_message);
            } else {
                rowView = inflater.inflate(R.layout.right_row, parent, false);
                rowMessage = (TextView) rowView.findViewById(R.id.right_message);
            }


            rowMessage.setText(thisRow.getMessages());

            return rowView;
        }

        public long getItemId(int position) {
            return position;
        }
    }


}


