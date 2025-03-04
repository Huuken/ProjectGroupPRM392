package com.example.freshfoldlaundrycare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SelectPaymentMehodActivity extends AppCompatActivity {

    String totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment_mehod);

        totalPrice = getIntent().getStringExtra("totalPrice");

    }

    public void goToFinalOrderPage(View view) {
        Intent intent = new Intent(getApplicationContext(), ConfirmOrderActivity.class);
        intent.putExtra("totalPrice", totalPrice);
        startActivity(intent);
    }

    public void goToCardPayment(View view) {
        Intent intent = new Intent(getApplicationContext(), CardPaymentActivity.class);
        intent.putExtra("totalPrice", totalPrice);
        startActivity(intent);
    }
}