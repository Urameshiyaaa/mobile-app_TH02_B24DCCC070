package com.example.bth2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {
    private TextInputLayout tilName;
    private TextInputLayout tilAmount;
    private EditText edtName;
    private EditText edtAmount;
    private EditText edtNote;
    private Spinner spnCategory;
    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_expense);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            findViewById(R.id.appBar).setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            v.setPadding(0, 0, 0, systemBars.bottom);
            return insets;
        });

        tilName = findViewById(R.id.tilName);
        tilAmount = findViewById(R.id.tilAmount);
        edtName = findViewById(R.id.edtName);
        edtAmount = findViewById(R.id.edtAmount);
        edtNote = findViewById(R.id.edtNote);
        spnCategory = findViewById(R.id.spnCategory);
        datePicker = findViewById(R.id.datePicker);
        Button btnSave = findViewById(R.id.btnSave);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(
                this, R.array.categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(categoryAdapter);

        btnSave.setOnClickListener(v -> saveExpense());
    }

    private void saveExpense() {
        tilName.setError(null);
        tilAmount.setError(null);

        String name = edtName.getText().toString().trim();
        String amountText = edtAmount.getText().toString().trim();
        boolean valid = true;

        if (name.isEmpty()) {
            tilName.setError(getString(R.string.add_error_name));
            valid = false;
        }

        long amount = 0;
        try {
            amount = Long.parseLong(amountText);
        } catch (NumberFormatException e) {
            amount = 0;
        }
        if (amount <= 0) {
            tilAmount.setError(getString(R.string.add_error_amount));
            valid = false;
        }

        if (!valid) {
            return;
        }

        String category = spnCategory.getSelectedItem().toString();
        String date = String.format(Locale.getDefault(), "%02d/%02d/%d",
                datePicker.getDayOfMonth(), datePicker.getMonth() + 1, datePicker.getYear());
        String note = edtNote.getText().toString().trim();

        Expense expense = new Expense(name, amount, category, date, note);
        Intent result = new Intent();
        result.putExtra(MainActivity.EXTRA_EXPENSE, expense);
        setResult(RESULT_OK, result);
        finish();
    }
}
