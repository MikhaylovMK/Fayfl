package com.example.fayfl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fayfl.model.Order;

import com.firebase.ui.auth.data.model.User;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.text.format.DateFormat;
import android.widget.Toast;


public class TaxiActivity extends AppCompatActivity {

    public static int SIGN_IN_CODE = 1;
    private FirebaseListAdapter<Order> adapter;
    private Button btn_order;
    private Button btn_order_true;
    private ViewGroup safety_text;

    private EditText textFieldOrderA;
    private EditText textFieldOrderB;
    private EditText textFieldOrderPrice;
    private ListView order_menu;
    private String order_a, order_b, order_p;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taxi);

        btn_order_true = findViewById(R.id.btn_order_true);
        btn_order = findViewById(R.id.btn_order);
        textFieldOrderA = findViewById(R.id.textFieldOrderA);
        textFieldOrderB = findViewById(R.id.textFieldOrderB);
        textFieldOrderPrice = findViewById(R.id.textFieldOrderPrice);
        order_menu = findViewById(R.id.order_menu);
        safety_text = findViewById(R.id.safety_text);

        order_menu.setVisibility(View.VISIBLE);
        btn_order_true.setVisibility(View.VISIBLE);
        btn_order.setVisibility(View.GONE);
        textFieldOrderA.setVisibility(View.GONE);
        textFieldOrderB.setVisibility(View.GONE);
        textFieldOrderPrice.setVisibility(View.GONE);
        safety_text.setVisibility(View.GONE);


        btn_order_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_order_true.setVisibility(View.GONE);
                order_menu.setVisibility(View.GONE);
                btn_order.setVisibility(View.VISIBLE);
                textFieldOrderA.setVisibility(View.VISIBLE);
                textFieldOrderB.setVisibility(View.VISIBLE);
                textFieldOrderPrice.setVisibility(View.VISIBLE);
                safety_text.setVisibility(View.VISIBLE);
            }
        });
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                order_menu.setVisibility(View.VISIBLE);
                btn_order_true.setVisibility(View.VISIBLE);
                btn_order.setVisibility(View.GONE);
                textFieldOrderA.setVisibility(View.GONE);
                textFieldOrderB.setVisibility(View.GONE);
                textFieldOrderPrice.setVisibility(View.GONE);
                safety_text.setVisibility(View.GONE);

                order_a = textFieldOrderA.getText().toString();
                order_b = textFieldOrderB.getText().toString();
                order_p = textFieldOrderPrice.getText().toString();

                if (TextUtils.isEmpty(order_a)) {
                    Toast.makeText(TaxiActivity.this, "Добавьте адрес отправления.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(order_b)) {
                    Toast.makeText(TaxiActivity.this, "Добавьте адрес назначения.", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(order_p)) {
                    Toast.makeText(TaxiActivity.this, "Укажите цену.", Toast.LENGTH_SHORT).show();
                } else {
                    if (textFieldOrderA.getText().toString() == "") return;
                    if (textFieldOrderB.getText().toString() == "") return;
                    if (textFieldOrderPrice.getText().toString() == "") return;

                    FirebaseDatabase.getInstance().getReference("User").child("Archives_Taxi")
                            .push().setValue(new Order(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(), textFieldOrderA.getText()
                                    .toString(), textFieldOrderB.getText().toString(), textFieldOrderPrice.getText().toString()));

                    FirebaseDatabase.getInstance().getReference("User").child("Taxi")
                            .push().setValue(new Order(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(), textFieldOrderA.getText()
                                    .toString(), textFieldOrderB.getText().toString(), textFieldOrderPrice.getText().toString()));
                    textFieldOrderA.setText("");
                    textFieldOrderB.setText("");
                    textFieldOrderPrice.setText("");

                    Toast.makeText(TaxiActivity.this, "Ваша заявка успешно оформлена, ожидайте звонка от водителя.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        displayAllOrder();
    }

    private void displayAllOrder() {
        ListView Orders = findViewById(R.id.order_menu);
        FirebaseListOptions.Builder<Order> builder = new FirebaseListOptions.Builder<>();
        builder.setLayout(R.layout.order_item).setQuery(FirebaseDatabase.getInstance().getReference("User")
                .child("Taxi"), Order.class).setLifecycleOwner(this);

        adapter = new FirebaseListAdapter<Order>(builder.build()) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Order model, int position) {

                TextView mess_user, mess_time, mess_the_cost_of_travel, mess_order_where, mess_order_to_where;
                mess_user = v.findViewById(R.id.order_user);
                mess_time = v.findViewById(R.id.order_time);
                mess_order_where = v.findViewById(R.id.order_where);
                mess_order_to_where = v.findViewById(R.id.order_to_where);
                mess_the_cost_of_travel = v.findViewById(R.id.the_cost_of_travel);

                mess_user.setText(model.getPhoneNumber());
                mess_the_cost_of_travel.setText(model.getThe_cost_of_travel());
                mess_order_where.setText(model.getOrder_where());
                mess_order_to_where.setText(model.getOrder_to_where());
                mess_time.setText(DateFormat.format("dd.MM.yy hh:mm", model.getOrderTime()));

            }
        };

        Orders.setAdapter(adapter);

        Orders.setClickable(true);
        Orders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                TextView numb_user = view.findViewById(R.id.order_user);
                String number = numb_user.getText().toString();

                int permissionCheck = ContextCompat.checkSelfPermission(TaxiActivity.this, Manifest.permission.CALL_PHONE);


                Intent intent_call = new Intent(Intent.ACTION_CALL);
                intent_call.setData(Uri.parse("tel: " + number));

                AlertDialog.Builder alert = new AlertDialog.Builder(TaxiActivity.this);
                alert.setTitle("Принять заказ?");
                alert.setMessage("Для принятия заказа необходимо связаться с клиентом.");
                alert.setCancelable(false);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (permissionCheck!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(
                                    TaxiActivity.this,new String[]{Manifest.permission.CALL_PHONE},123
                            );
                        }
                        else {
                            startActivity(intent_call);
                            adapter.getRef(position).removeValue();
                        }
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
}