package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Stack;

public class convertion extends AppCompatActivity {
    private EditText infixInput;
    private TextView Output;
    private Button prefix,postfix,infix;
    private TableLayout conversionTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_convertion);
        infixInput = findViewById(R.id.infixInput);
        Output = findViewById(R.id.Output);
        prefix=findViewById(R.id.PrefixButton);
        postfix=findViewById(R.id.PostfixButton);
        conversionTable = findViewById(R.id.conversionTable);
        infix=findViewById(R.id.infixButton);

        postfix.setOnClickListener(v -> {
            String infix = infixInput.getText().toString().trim();

                        if (TextUtils.isEmpty(infix)) {
                            Toast.makeText(this, "Please enter an valid expression", Toast.LENGTH_SHORT).show();
                            return;
                        }
            findViewById(R.id.message).setVisibility(View.GONE);

                        // Clear any previous conversion steps
                        clearTableRows();
            if (is_valid(infix)) {
                        String expressionType = identifyExpressionType(infix);

                        // Debugging toast to check what the function is returning
//            Toast.makeText(this, "Expression Type: " + expressionType, Toast.LENGTH_SHORT).show();

                        // Clear any previous conversion steps
//            clearTableRows();

                        if (expressionType.equals("prefix")) {
                            String postfix = convertPrefixToPostfix(infix);
                            Output.setText(postfix);
                        } else if (expressionType.equals("infix")) {
                            String postfix = convertInfixToPostfix(infix);
                            Output.setText(postfix);
                        } else if (expressionType.equals("postfix")) {
                            Toast.makeText(this, "It is a postfix expression", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Invalid expression entered!", Toast.LENGTH_SHORT).show();
                        }
                    }

        });
        prefix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String infix = infixInput.getText().toString().trim();
                if (TextUtils.isEmpty(infix)) {
                    Toast.makeText(convertion.this, "Please enter an valid expression", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Clear any previous conversion steps
                clearTableRows();
                if (is_valid(infix)) {
                    String expressionType = identifyExpressionType(infix);
                    // Convert to prefix and display the steps
                    if (expressionType.equals("postfix")) {
                        String postfix = convertPostfixToPrefix(infix);

                        Output.setText(postfix);
                    } else if (expressionType.equals("infix")) {
                        String postfix = convertInfixToPrefix(infix);
                        findViewById(R.id.message).setVisibility(View.VISIBLE);
                        Output.setText(postfix);
                    } else if (expressionType.equals("prefix")) {
                        Toast.makeText(convertion.this, "It is a prefix expression", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(convertion.this, "Invalid expression entered!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(convertion.this, "Invalid expression entered!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        infix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String infix = infixInput.getText().toString().trim();
                if (TextUtils.isEmpty(infix)) {
                    Toast.makeText(convertion.this, "Please enter an valid expression", Toast.LENGTH_SHORT).show();
                    return;
                }
                findViewById(R.id.message).setVisibility(View.GONE);
                // Clear any previous conversion steps
                clearTableRows();
                if (is_valid(infix)) {
                    String expressionType = identifyExpressionType(infix);
                    // Convert to prefix and display the steps
                    if (expressionType.equals("postfix")) {
                        String postfix = convertPostfixToInfix(infix);
                        Output.setText(postfix);
                    } else if (expressionType.equals("prefix")) {
                        String postfix = convertPrefixToInfix(infix);
                        Output.setText(postfix);
                    } else if (expressionType.equals("infix")) {
                        Toast.makeText(convertion.this, "It is a infix expression", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(convertion.this, "Invalid expression entered!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(convertion.this, "Invalid expression entered!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Helper method to clear previous table rows
    private void clearTableRows() {
        int childCount = conversionTable.getChildCount();
        if (childCount > 1) {
            conversionTable.removeViews(1, childCount - 1); // Keep the header
        }
    }

    // Method to convert infix expression to postfix and show steps
    private String convertInfixToPostfix(String infix) {
        Stack<Character> stack = new Stack<>();
        StringBuilder postfix = new StringBuilder();
        int stepCount = 0;

        for (char ch : infix.toCharArray()) {
            if (Character.isLetterOrDigit(ch)) {
                // If the character is an operand, add it to the output
                postfix.append(ch);
                addRowToTable(stepCount++, String.valueOf(ch), stack.toString(), postfix.toString());
            } else if (ch == '(') {
                stack.push(ch);
                addRowToTable(stepCount++, String.valueOf(ch), stack.toString(), postfix.toString());
            } else if (ch == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop());
                    addRowToTable(stepCount++, ")", stack.toString(), postfix.toString());
                }
                if (!stack.isEmpty() && stack.peek() != '(')
                    return "Invalid Expression"; // Invalid case
                else
                    stack.pop();
            } else { // an operator is encountered
                while (!stack.isEmpty() && precedence(ch) <= precedence(stack.peek())) {
                    postfix.append(stack.pop());
                    addRowToTable(stepCount++, String.valueOf(ch), stack.toString(), postfix.toString());
                }
                stack.push(ch);
                addRowToTable(stepCount++, String.valueOf(ch), stack.toString(), postfix.toString());
            }
        }

        // Pop all the operators from the stack
        while (!stack.isEmpty()) {
            postfix.append(stack.pop());
            addRowToTable(stepCount++, "", stack.toString(), postfix.toString());
        }

        return postfix.toString();
    }

    private String convertInfixToPrefix(String infix) {
        // Step 1: Reverse the infix expression
        StringBuilder reversedInfix = new StringBuilder(infix).reverse();
        for (int i = 0; i < reversedInfix.length(); i++) {
            if (reversedInfix.charAt(i) == '(') {
                reversedInfix.setCharAt(i, ')');
            } else if (reversedInfix.charAt(i) == ')') {
                reversedInfix.setCharAt(i, '(');
            }
        }

        // Step 2: Convert the reversed infix to postfix
        String reversedPostfix = convertInfixToPostfix(reversedInfix.toString());

        // Step 3: Reverse the postfix expression to get prefix
        return new StringBuilder(reversedPostfix).reverse().toString();
    }

    private String convertPostfixToInfix(String postfix) {
        Stack<String> stack = new Stack<>();
        int stepCount = 0;

        for (char ch : postfix.toCharArray()) {
            if (Character.isLetterOrDigit(ch)) {
                stack.push(String.valueOf(ch));
                addRowToTable(stepCount++, String.valueOf(ch), stack.toString(), "");
            } else {
                String operand2 = stack.pop();
                String operand1 = stack.pop();
                String infix = "(" + operand1 + ch + operand2 + ")";
                stack.push(infix);
                addRowToTable(stepCount++, String.valueOf(ch), stack.toString(), infix);
            }
        }

        return stack.peek();
    }

    // Method to convert postfix to prefix and show steps
    private String convertPostfixToPrefix(String postfix) {
        Stack<String> stack = new Stack<>();
        int stepCount = 0;

        for (char ch : postfix.toCharArray()) {
            if (Character.isLetterOrDigit(ch)) {
                stack.push(String.valueOf(ch));
                addRowToTable(stepCount++, String.valueOf(ch), stack.toString(), "");
            } else {
                String operand2 = stack.pop();
                String operand1 = stack.pop();
                String prefix = ch + operand1 + operand2;
                stack.push(prefix);
                addRowToTable(stepCount++, String.valueOf(ch), stack.toString(), prefix);
            }
        }

        return stack.peek();
    }
    // Method to convert infix expression to postfix (using the reversed prefix)
    private String convertPrefixToPostfix(String prefix) {
        Stack<Character> stack = new Stack<>();
        StringBuilder postfix = new StringBuilder();
        int stepCount = 0;

        for (char ch : prefix.toCharArray()) {
            if (Character.isLetterOrDigit(ch)) {
                // If the character is an operand, push it to the stack
                stack.push(ch);
                addRowToTable(stepCount++, String.valueOf(ch), stack.toString(), "");
            } else { // an operator is encountered
                while (!stack.isEmpty()) {
                    postfix.append(stack.pop());
                    addRowToTable(stepCount++, String.valueOf(ch), stack.toString(), postfix.toString());
                }
                stack.push(ch);
                addRowToTable(stepCount++, String.valueOf(ch), stack.toString(), postfix.toString());
            }
        }

        // Pop all the operands from the stack (already reversed order)
        while (!stack.isEmpty()) {
            postfix.append(stack.pop());
            addRowToTable(stepCount++, "", stack.toString(), postfix.toString());
        }

        return postfix.toString();
    }

    // Method to convert prefix expression to infix
    // Method to convert prefix expression to infix and display steps
    private String convertPrefixToInfix(String prefix) {
        Stack<String> stack = new Stack<>();
        int stepCount = 0; // To track steps

        // Process the prefix expression from right to left
        for (int i = prefix.length() - 1; i >= 0; i--) {
            char ch = prefix.charAt(i);

            if (Character.isLetterOrDigit(ch)) {
                // If the character is an operand, push it to the stack
                stack.push(String.valueOf(ch));
                // Add the current state to the table after pushing the operand
                addRowToTable(stepCount++, String.valueOf(ch), stack.toString(), "");
            } else { // Operator encountered
                String operand1 = stack.pop();
                String operand2 = stack.pop();

                // Form the infix expression
                String infixExpression = "(" + operand1 + ch + operand2 + ")";
                stack.push(infixExpression);

                // Add the current state to the table after processing the operator
                addRowToTable(stepCount++, String.valueOf(ch), stack.toString(), infixExpression);
            }
        }

        // The final expression on the stack is the infix expression
        return stack.peek();
    }
    // Helper method to add rows dynamically to the table
    private void addRowToTable(int step, String expression, String stackState, String postfix) {
        TableRow row = new TableRow(this);

        // Create TextViews for each column
        TextView stepView = new TextView(this);
        stepView.setText(String.valueOf(step));
        stepView.setPadding(16, 8, 16, 8);  // Padding for better appearance
        stepView.setGravity(Gravity.CENTER); // Center alignment
        stepView.setBackgroundResource(R.drawable.table_row_border);
        row.addView(stepView);

        TextView expressionView = new TextView(this);
        expressionView.setText(expression);
        expressionView.setPadding(8, 8, 8, 8);
        expressionView.setGravity(Gravity.CENTER);
        expressionView.setBackgroundResource(R.drawable.table_row_border);
        row.addView(expressionView);

        TextView stackView = new TextView(this);
        stackView.setText(stackState);
        stackView.setPadding(16, 8, 16, 8);
        stackView.setGravity(Gravity.CENTER);
        stackView.setBackgroundResource(R.drawable.table_row_border);
        row.addView(stackView);

        TextView postfixView = new TextView(this);
        postfixView.setText(postfix);
        postfixView.setPadding(16, 8, 16, 8);
        postfixView.setGravity(Gravity.CENTER);
        postfixView.setBackgroundResource(R.drawable.table_row_border);
        row.addView(postfixView);

        // Add the row to the table
        conversionTable.addView(row);
    }

    public static String identifyExpressionType(String expression) {
        // Check for prefix expression
        if (isPrefix(expression)) {
            return "prefix";
        }
        // Check for postfix expression
        if (isPostfix(expression)) {
            return "postfix";
        }
        // Default to infix
        return "infix";
    }

    public static boolean isPrefix(String expression) {
        // A prefix expression starts with an operator
        return isOperator(expression.charAt(0));
    }

    public static boolean isPostfix(String expression) {
        // A postfix expression ends with an operator
        return isOperator(expression.charAt(expression.length() - 1));
    }
    public static boolean isOperator(char ch) {
        return ch == '+'|| ch == '-'  ||ch == '*' || ch == '/'  || ch == '^' ;
    }
    public static boolean is_valid (String expression) {
        int operators = 0;
        int operands = 0;
        for (char ch : expression.toCharArray()) {
            if (Character.isLetterOrDigit(ch)) {
                operands++;
            } else if (isOperator(ch)) {
                operators++;
            }
        }
        if (operators + 1 == operands) {
            return true;
        } else {
            return false;
        }
    }


    // Define operator
    private int precedence(char ch) {
        switch (ch) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
        }
        return -1;
    }

    }
