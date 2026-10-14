package com.example.bth2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_EXPENSE = "extra_expense";
    public static final String EXTRA_POSITION = "extra_position";

    private final ArrayList<Expense> expenses = new ArrayList<>();
    private ExpenseAdapter adapter;
    private TextView tvTotal;
    private TextView tvEmpty;
    private RecyclerView rvExpenses;

    private final ActivityResultLauncher<Intent> addLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Expense newExpense =
                            (Expense) result.getData().getSerializableExtra(EXTRA_EXPENSE);
                    if (newExpense != null) {
                        expenses.add(0, newExpense);
                        adapter.notifyItemInserted(0);
                        rvExpenses.scrollToPosition(0);
                        refreshUI();
                    }
                }
            });

    private final ActivityResultLauncher<Intent> detailLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    int position = result.getData().getIntExtra(EXTRA_POSITION, -1);
                    if (position >= 0 && position < expenses.size()) {
                        expenses.remove(position);
                        adapter.notifyDataSetChanged();
                        refreshUI();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left + dp(16), systemBars.top + dp(16),
                    systemBars.right + dp(16), systemBars.bottom + dp(16));
            return insets;
        });

        tvTotal = findViewById(R.id.tvTotal);
        tvEmpty = findViewById(R.id.tvEmpty);
        rvExpenses = findViewById(R.id.rvExpenses);
        Button btnAdd = findViewById(R.id.btnAdd);

        loadSampleData();

        adapter = new ExpenseAdapter(expenses, (expense, position) -> {
            Intent intent = new Intent(MainActivity.this, ExpenseDetailActivity.class);
            intent.putExtra(EXTRA_EXPENSE, expense);
            intent.putExtra(EXTRA_POSITION, position);
            detailLauncher.launch(intent);
        });
        rvExpenses.setLayoutManager(new LinearLayoutManager(this));
        rvExpenses.setAdapter(adapter);

        btnAdd.setOnClickListener(v ->
                addLauncher.launch(new Intent(MainActivity.this, AddExpenseActivity.class)));
        refreshUI();
    }

    private void loadSampleData() {
        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        expenses.add(new Expense("Ăn sáng", 30000, "Ăn uống", today, ""));
        expenses.add(new Expense("Đổ xăng", 50000, "Đi lại", today, ""));
        expenses.add(new Expense("Mua sách", 120000, "Học tập", today, ""));
    }

    private void refreshUI() {
        long total = 0;
        for (Expense e : expenses) {
            total += e.getAmount();
        }
        tvTotal.setText(Expense.formatMoney(total));

        boolean isEmpty = expenses.isEmpty();
        tvEmpty.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        rvExpenses.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}