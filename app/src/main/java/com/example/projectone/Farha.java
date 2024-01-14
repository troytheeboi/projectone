package com.example.projectone;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Farha extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Spinner genderSpinner;
    private Spinner countrySpinner;
    private Spinner citySpinner;
    private EditText phoneNumberEditText;
    private Button registerButton;

    // You can add more countries and cities as needed
    private static final String[] countries = {"palestine", "UK","USA","Qater"};
    private static final String[][] cities = {{"Ramallah", "nubles", "bethlahem"}, {"london", "liverpool", "Birmingham"},{"florida","dalles","houston"},{"doha","al khor","mesaieed","al wakrah"}};

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farha);

        // Initialize UI components
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        genderSpinner = findViewById(R.id.genderSpinner);
        countrySpinner = findViewById(R.id.countrySpinner);
        citySpinner = findViewById(R.id.citySpinner);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        registerButton = findViewById(R.id.registerButton);

        // Set up spinners
        setUpGenderSpinner();
        setUpCountrySpinner();

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Set a click listener for the "Register" button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the registration process
                handleRegistration();
            }
        });
    }

    private void setUpGenderSpinner() {
        // Set up the gender spinner without using resources
        String[] genders = {"Male", "Female", "Other"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
    }

    private void setUpCountrySpinner() {
        // Set up the country spinner without using resources
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);

        // Set up a listener for the country spinner to update the city spinner based on the selected country
        countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateCitySpinner(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
    }

    private void updateCitySpinner(int selectedCountryIndex) {
        // Update the city spinner based on the selected country
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cities[selectedCountryIndex]);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);
    }

    private void handleRegistration() {
        // Retrieve user inputs
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String gender = genderSpinner.getSelectedItem().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String country = countrySpinner.getSelectedItem().toString();
        String city = citySpinner.getSelectedItem().toString();
        String phoneNumber = phoneNumberEditText.getText().toString().trim();

        // Validate inputs
        if (firstName.length() < 3 || lastName.length() < 3) {
            showToast("First name and last name must be at least 3 characters");
            return;
        }

        if (!isPasswordValid(password)) {
            showToast("Password must be at least 5 characters and include a character, a number, and a special character");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showToast("Passwords do not match");
            return;
        }

        // Perform registration and insert data into the database
        insertUserData(firstName, lastName, gender, password, country, city, phoneNumber);

        // Show a success message
        showToast("Registration successful! User: " + firstName + " " + lastName + ", Country: " + country + ", City: " + city + ", Phone: " + phoneNumber);
    }

    private boolean isPasswordValid(String password) {
        // Password must be at least 5 characters and include a character, a number, and a special character
        return password.length() >= 5 && password.matches(".*[a-zA-Z]+.*") && password.matches(".*\\d+.*") && password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?]+.*");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void insertUserData(String firstName, String lastName, String gender, String password, String country, String city, String phoneNumber) {
        // Open a writable database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a ContentValues object to store key-value pairs
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FIRST_NAME, firstName);
        values.put(DatabaseHelper.COLUMN_LAST_NAME, lastName);
        values.put(DatabaseHelper.COLUMN_GENDER, gender);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);
        values.put(DatabaseHelper.COLUMN_COUNTRY, country);
        values.put(DatabaseHelper.COLUMN_CITY, city);
        values.put(DatabaseHelper.COLUMN_PHONE_NUMBER, phoneNumber);

        // Insert the data into the "user" table
        db.insert(DatabaseHelper.TABLE_NAME, null, values);

        // Close the database
        db.close();
    }
}
