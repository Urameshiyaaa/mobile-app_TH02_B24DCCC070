package com.example.bth2;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Lớp đại diện cho một khoản chi tiêu.
 * Implement Serializable để truyền qua Intent giữa các Activity.
 */
public class Expense implements Serializable {

    private String name;      // Tên khoản chi
    private long amount;      // Số tiền (đồng)
    private String category;  // Danh mục
    private String date;      // Ngày chi tiêu, định dạng dd/MM/yyyy
    private String note;      // Ghi chú (có thể rỗng)

    public Expense(String name, long amount, String category, String date, String note) {
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.note = note;
    }

    /** Định dạng số tiền có dấu chấm phân cách hàng nghìn, ví dụ 120000 -> "120.000 đ". */
    public static String formatMoney(long amount) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        return new DecimalFormat("#,###", symbols).format(amount) + " đ";
    }

    public String getName() {
        return name;
    }

    public long getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }
}
