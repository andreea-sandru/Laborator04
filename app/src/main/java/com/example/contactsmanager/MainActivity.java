package com.example.contactsmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import static com.example.contactsmanager.Constants.*;

public class MainActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText addressEditText;
    private EditText jobTitleEditText;
    private EditText companyEditText;
    private EditText websiteEditText;
    private EditText imEditText;

    private LinearLayout additionalLinear;

    private Button showAdditional, save, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showAdditional = findViewById(R.id.showAdditional);
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);
        additionalLinear = findViewById(R.id.additionalLinear);
        nameEditText = (EditText)findViewById(buttonIds[0]);
        phoneEditText = (EditText)findViewById(buttonIds[1]);
        emailEditText = (EditText)findViewById(buttonIds[2]);
        addressEditText = (EditText)findViewById(buttonIds[3]);
        jobTitleEditText = (EditText)findViewById(buttonIds[4]);
        companyEditText = (EditText)findViewById(buttonIds[5]);
        websiteEditText = (EditText)findViewById(buttonIds[6]);
        imEditText = (EditText)findViewById(buttonIds[7]);

        save.setOnClickListener(buttonClickListener);
        cancel.setOnClickListener(buttonClickListener);
        showAdditional.setOnClickListener(buttonClickListener);

        Intent intent = getIntent();
        if (intent != null) {
            String phone = intent.getStringExtra("com.example.contactsmanager.PHONE_NUMBER_KEY");
            if (phone != null) {
                phoneEditText.setText(phone);
            } else {
                Toast.makeText(this, getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }

    }

    private ButtonClickListener buttonClickListener = new ButtonClickListener();

    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.showAdditional:
                    String state = showAdditional.getText().toString();

                    if(state == HIDE_ADDITIONAL) {
                        showAdditional.setText(SHOW_ADDITIONAL);
                        additionalLinear.setVisibility(View.INVISIBLE);
                    }
                    else {
                        showAdditional.setText(HIDE_ADDITIONAL);
                        additionalLinear.setVisibility(View.VISIBLE);
                    }
                    break;

                case R.id.save:
                    saveData();
                    break;

                case R.id.cancel:
                    setResult(Activity.RESULT_CANCELED, new Intent());
                    finish();
                    break;

            }
        }
    }

    public  void saveData() {

        String name = nameEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String jobTitle = jobTitleEditText.getText().toString();
        String company = companyEditText.getText().toString();
        String website = websiteEditText.getText().toString();
        String im = imEditText.getText().toString();

        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        if (name != null) {
            intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
        }
        if (phone != null) {
            intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
        }
        if (email != null) {
            intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
        }
        if (address != null) {
            intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
        }
        if (jobTitle != null) {
            intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle);
        }
        if (company != null) {
            intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
        }

        ArrayList<ContentValues> contactData = new ArrayList<ContentValues>();
        if (website != null) {
            ContentValues websiteRow = new ContentValues();
            websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
            websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website);
            contactData.add(websiteRow);
        }
        if (im != null) {
            ContentValues imRow = new ContentValues();
            imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
            imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im);
            contactData.add(imRow);
        }
        intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
        //startActivity(intent);

        startActivityForResult(intent, Constants.CONTACTS_MANAGER_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case CONTACTS_MANAGER_REQUEST_CODE:
                setResult(resultCode, new Intent());
                finish();
                break;
        }
    }

}