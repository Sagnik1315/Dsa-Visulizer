package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.HelperClasses.Stack;

public class Stack_array extends AppCompatActivity {
    private Stack stack;
    private LinearLayout stackView;
    private EditText inputValue;
    private TextView resultView, topView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stack_array);

        stack = new Stack(15); // Stack with 15 elements
        stackView = findViewById(R.id.stackView);
        inputValue = findViewById(R.id.inputValue);
        resultView = findViewById(R.id.resultView);
        topView = findViewById(R.id.topView);

        Button pushButton = findViewById(R.id.pushButton);
        Button popButton = findViewById(R.id.popButton);
        Button peekButton = findViewById(R.id.peekButton);

        pushButton.setOnClickListener(v -> {
            String input = inputValue.getText().toString();
            if (!input.isEmpty()) {
                int value = Integer.parseInt(input);
                stack.push(value);
                updateStackView();
                inputValue.setText("");
            }
        });

        popButton.setOnClickListener(v -> {
            int poppedValue = stack.pop();
            resultView.setText("Popped: " + (poppedValue == -1 ? "Stack is empty" : poppedValue));
            updateStackView();
        });

        peekButton.setOnClickListener(v -> {
            int topValue = stack.peek();
            resultView.setText("Peek: " + (topValue == -1 ? "Stack is empty" : topValue));
        });

        updateStackView();
    }

    private void updateStackView() {
        stackView.removeAllViews();
        int[] stackArray = stack.getStackArray();
        int top = stack.getTop();
        for (int i = top; i >=0; i--) {
            TextView stackItem = new TextView(this);
            stackItem.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            stackItem.setText(" " + stackArray[i] + " ");
            stackItem.setTextSize(20);
            stackItem.setBackgroundResource(android.R.drawable.editbox_background);
            stackItem.setGravity(View.TEXT_ALIGNMENT_CENTER);

            stackView.addView(stackItem);
        }

        topView.setText("Top: " + top);
    }
}