package com.example.bth2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

public class ExpenseDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_expense_detail);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detail_root), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            findViewById(R.id.appBar).setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            v.setPadding(0, 0, 0, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        Expense expense = (Expense) intent.getSerializableExtra(MainActivity.EXTRA_EXPENSE);
        int position = intent.getIntExtra(MainActivity.EXTRA_POSITION, -1);
        if (expense == null) {
            finish();
            return;
        }

        ((TextView) findViewById(R.id.tvDetailName)).setText(expense.getName());
        ((TextView) findViewById(R.id.tvDetailAmount)).setText(Expense.formatMoney(expense.getAmount()));
        ((TextView) findViewById(R.id.tvDetailCategory)).setText(expense.getCategory());
        ((TextView) findViewById(R.id.tvDetailDate)).setText(expense.getDate());
        String note = expense.getNote();
        ((TextView) findViewById(R.id.tvDetailNote)).setText(
                note == null || note.isEmpty() ? getString(R.string.detail_no_note) : note);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        Button btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(v -> {
            Intent result = new Intent();
            result.putExtra(MainActivity.EXTRA_POSITION, position);
            setResult(RESULT_OK, result);
            finish();
        });
    }
}
