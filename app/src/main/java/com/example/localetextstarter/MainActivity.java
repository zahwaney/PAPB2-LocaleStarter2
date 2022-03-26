package com.example.localetextstarter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    // Default quantity is 1.
    private int mInputQuantity = 1;

    private NumberFormat mCurrencyFormat = NumberFormat.getCurrencyInstance();

    // TODO: Get the number format for this locale.

    // Fixed price in U.S. dollars and cents: ten cents.
    private double mPrice = 0.10;

    // Exchange rates for Indonesia(ID) and Arabic-Egypt(EG).
    double mIdExchangeRate = 14000; //
    double mEgExchangeRate = 18.5; //

    // TODO: Get locale's currency.

    /**
     * Creates the view with a toolbar for the options menu
     * and a floating action button, and initialize the
     * app data.
     *
     * @param savedInstanceState Bundle with activity's previously saved state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHelp();
            }
        });

        // Get the current date.
        final Date myDate = new Date();
        // Add 5 days in milliseconds to create the expiration date.
        final long expirationDate = myDate.getTime() + TimeUnit.DAYS.toMillis(5);
        // Set the expiration date as the date to display.
        myDate.setTime(expirationDate);

        // TODO: Format the date for the locale.
        TextView expiredDateTextView = findViewById(R.id.date);
        String formatedExpiredDate = DateFormat.getDateInstance().format(myDate);
        expiredDateTextView.setText(formatedExpiredDate);


        // TODO: Apply the exchange rate and calculate the price.

        String deviceLocale = Locale.getDefault().getCountry();

        if (deviceLocale == "ID") {
            mPrice = mPrice * mIdExchangeRate;
        }
        else if (deviceLocale == "EG") {
            mPrice = mPrice * mEgExchangeRate;
        }
        else {
            mCurrencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        }

        TextView priceTextView = findViewById(R.id.price);
        String formattedPrice = mCurrencyFormat.format(mPrice);
        priceTextView.setText(formattedPrice);

        // TODO: Show the price string.

        // Get the EditText view for the entered quantity.
        final EditText enteredQuantity = (EditText) findViewById(R.id.quantity);
        // Add an OnEditorActionListener to the EditText view.
        enteredQuantity.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // String myFormattedTotal;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Close the keyboard.
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService
                            (Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    // Check if view v is empty.
                    if (v.toString().equals("")) {
                        // Don't format, leave alone.
                    } else {
                        NumberFormat mNumberFormat = NumberFormat.getNumberInstance();

                        // TODO: Parse string in view v to a number.
                        try {
                            mInputQuantity = mNumberFormat.parse(v.getText().toString()).intValue();
                        } catch (ParseException e) {
                            v.setError(getText(R.string.enter_number));
                            e.printStackTrace();
                        }

                        String formatedQuantity = mNumberFormat.format(mInputQuantity);
                        v.setText(formatedQuantity);

                        // TODO: Convert to string using locale's number format.

                        // TODO: Homework: Calculate the total amount from price and quantity.

                        // TODO: Homework: Use currency format for France (FR) or Israel (IL).

                        // TODO: Homework: Show the total amount string.

                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**
     * Shows the Help screen.
     */
    private void showHelp() {
        // Create the intent.
        Intent helpIntent = new Intent(this, HelpActivity.class);
        // Start the HelpActivity.
        startActivity(helpIntent);
    }

    /**
     * Clears the quantity when resuming the app after language is changed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        ((EditText) findViewById(R.id.quantity)).getText().clear();
    }

    /**
     * Creates the options menu and returns true.
     *
     * @param menu       Options menu
     * @return boolean   True after creating options menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Handles options menu item clicks.
     *
     * @param item      Menu item
     * @return boolean  True if menu item is selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle options menu item clicks here.
        switch (item.getItemId()) {
            case R.id.action_help:
                Intent helpIntent = new Intent(this, HelpActivity.class);
                startActivity(helpIntent);
                return true;
            case R.id.action_language:
                Intent languageIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(languageIntent);
                return true;
            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);
    }
}