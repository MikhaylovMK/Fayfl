package com.example.fayfl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fayfl.model.Message;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.github.library.bubbleview.BubbleTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import android.text.format.DateFormat;
import android.widget.Toast;

public class ChatActivity extends AppCompatActivity {
    public static int SIGN_IN_CODE = 1;
    private FirebaseListAdapter<Message> adapter;
    private ImageButton btn_send;
    private String send_message;
    private EditText textField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        textField = findViewById(R.id.message_field);

        btn_send = findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send_message = textField.getText().toString();

                if (TextUtils.isEmpty(send_message))
                    Toast.makeText(ChatActivity.this, "Введите сообщение. ", Toast.LENGTH_SHORT).show();
                else {
                    if (textField.getText().toString() == "")
                        return;
                    FirebaseDatabase.getInstance().getReference("User").child("Chat").push().setValue(
                            new Message(
                                    FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(),
                                    textField.getText().toString()));
                    FirebaseDatabase.getInstance().getReference("User").child("Archives_Chat").push().setValue(
                            new Message(
                                    FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(),
                                    textField.getText().toString()));

                    textField.setText("");
                }
            }
        });

        displayAllMessages();
    }

    private void displayAllMessages() {

        ListView Messages = findViewById(R.id.list_of_messages);
        FirebaseListOptions.Builder<Message> builder = new FirebaseListOptions.Builder<>();
        builder.setLayout(R.layout.list_item)
                .setQuery(FirebaseDatabase.getInstance().getReference("User").child("Chat"),Message.class).setLifecycleOwner(this);

        adapter = new FirebaseListAdapter<Message>(builder.build()) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Message model, int position) {

                TextView mess_user, mess_time;
                BubbleTextView mess_text;
                mess_user = v.findViewById(R.id.message_user);
                mess_time = v.findViewById(R.id.message_time);
                mess_text = v.findViewById(R.id.message_text);

                mess_user.setText(model.getPhoneNumber());
                mess_text.setText(model.getTextMessage());
                mess_time.setText(DateFormat.format("dd.MM.yy hh:mm", model.getMessageTime()));

            }
        };
        Messages.setAdapter(adapter);

        Messages.setClickable(true);
        Messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                AlertDialog.Builder alert = new AlertDialog.Builder(ChatActivity.this);
                alert.setTitle("Удалить сообщение?");
                alert.setCancelable(false);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        adapter.getRef(position).removeValue();
                    }
                });

                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}