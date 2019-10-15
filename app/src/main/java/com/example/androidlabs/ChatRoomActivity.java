package com.example.androidlabs;

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


        //SENDING BUTTON ON CLICK
        sendButton.setOnClickListener(clk -> {
            String sendMessage = message.getText().toString();

            Messages newMessage = new Messages(sendMessage, true);

            //add the new contact to the list:
            chatList.add(newMessage);
            //update the listView:
            myAdapter = new MyOwnAdapter();
            theList.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
//            theList.smoothScrollToPosition(myAdapter.getCount());
            theList.setSelection(myAdapter.getCount()-1);
            message.setText("");

            //show a notification: first parameter is any view on screen. second parameter is the text. Third parameter is the length (SHORT/LONG)
            //Snackbar.make(sendButton, "Message is sent from SEND", Snackbar.LENGTH_SHORT).show();
            //
        });
//
        receiveButton.setOnClickListener(clk -> {


            String receiveMessage = message.getText().toString();
            Messages newMessage = new Messages(receiveMessage, false);

            //add the new contact to the list:
            chatList.add(newMessage);
            //update the listView:
            myAdapter = new MyOwnAdapter();
            theList.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
//            theList.smoothScrollToPosition(myAdapter.getCount());
            //visible at the current added position
            theList.setSelection(myAdapter.getCount()-1);

            message.setText("");

            //show a notification: first parameter is any view on screen. second parameter is the text. Third parameter is the length (SHORT/LONG)
            //Snackbar.make(receiveButton, "Message is sent from Receive", Snackbar.LENGTH_SHORT).show();
            //
        });



        Log.e(ACTIVITY_NAME, "In function: OnCreate()");
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
                ImageView i1 = (ImageView) rowView.findViewById(R.id.icon_left);
                i1.setImageResource(R.drawable.row_send);
                rowMessage.setText(thisRow.getMessages());
            } else {
                rowView = inflater.inflate(R.layout.right_row, parent, false);
                rowMessage = (TextView) rowView.findViewById(R.id.right_message);
            }


            rowMessage.setText(thisRow.getMessages());


            /*Class object
            String message
            boolean

            if(messages.get(p).isSend())
                thisRow(sendLayout)
            Else
            thisRow(receiveLayout)*/

            //return the row:
            return rowView;
        }

        public long getItemId(int position) {
            return position;
        }
    }
}


