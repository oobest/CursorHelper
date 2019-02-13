package com.albert.simple;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.TextInputEditText;
import android.widget.TextView;

import com.albert.simple.dao.CriteriaCustomer;
import com.albert.simple.dao.CustomerDAO;
import com.albert.simple.dao.impl.CustomerDAODBImpl;
import com.albert.simple.domain.Customer;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextInputEditText nameTextView;

    TextInputEditText phoneTextView;

    TextInputEditText addressTextView;

    TextView mTextView;

    CustomerDAO mCustomerDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCustomerDAO = new CustomerDAODBImpl(this);

        nameTextView = findViewById(R.id.nameTextView);
        addressTextView = findViewById(R.id.addressTextView);
        phoneTextView = findViewById(R.id.phoneTextView);

        mTextView = findViewById(R.id.textView);

        updateUI();
    }

    public void insertButtonClick(View view) {
        Customer customer = new Customer(nameTextView.getText().toString(),
                addressTextView.getText().toString(),
                phoneTextView.getText().toString());
        mCustomerDAO.save(customer);
        updateUI();
    }

    private void updateUI() {
        List<Customer> list = mCustomerDAO.getAll();
        StringBuilder sb = new StringBuilder();
        for (Customer customer : list) {
            sb.append(customer.toString()).append("\n");
        }
        mTextView.setText(sb.toString());
    }

    public void queryButtonClick(View view) {
        CriteriaCustomer criteriaCustomer = new CriteriaCustomer(nameTextView.getText().toString(),
                addressTextView.getText().toString(),
                phoneTextView.getText().toString());

        List<Customer> list = mCustomerDAO.getForListWithCriteriaCustomer(criteriaCustomer);
        StringBuilder sb = new StringBuilder();
        for (Customer customer : list) {
            sb.append(customer.toString()).append("\n");
        }
        mTextView.setText(sb.toString());
    }
}
