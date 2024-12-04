
package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;

public class scearching extends AppCompatActivity {
    private int[] array;  // Array will be user-defined
    private int target;   // Search target will be user-defined
    private int low, high, mid;

    private TextView lowValue, midValue, highValue, searchingValue, foundStatus;
    private GridLayout arrayGrid;
    private Button pauseButton, stepForwardButton, stepBackwardButton, inputArrayButton;
    private boolean isPaused = false;
    private Handler handler = new Handler();
    private Runnable searchRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scearching);
        // Initialize UI components
        pauseButton = findViewById(R.id.pauseButton);
        stepForwardButton = findViewById(R.id.stepForwardButton);
        stepBackwardButton = findViewById(R.id.stepBackwardButton);
        arrayGrid = findViewById(R.id.arrayGrid);
        lowValue = findViewById(R.id.lowValue);
        midValue = findViewById(R.id.midValue);
        highValue = findViewById(R.id.highValue);
        searchingValue = findViewById(R.id.searchingValue);
        foundStatus = findViewById(R.id.foundStatus);

        // Open input dialog for the user to enter the array and search element
        findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getInputValues()) {
                    displayArray();
                    pauseButton.setVisibility(View.VISIBLE);
                    stepForwardButton.setVisibility(View.VISIBLE);
                    stepBackwardButton.setVisibility(View.VISIBLE);
                    startBinarySearch();

                }
            }
        });

        // Pause/Resume the automatic search
        pauseButton.setOnClickListener(v -> {
            if (isPaused) {
                isPaused = false;
                handler.post(searchRunnable); // Resume search
                pauseButton.setText("Pause");
                stepForwardButton.setVisibility(View.GONE);
                stepBackwardButton.setVisibility(View.GONE);
            } else {
                isPaused = true;
                pauseButton.setText("Resume");
                stepForwardButton.setVisibility(View.VISIBLE);
                stepBackwardButton.setVisibility(View.VISIBLE);
            }
        });

        // Step Forward when paused
        stepForwardButton.setOnClickListener(v -> {
            if (isPaused) {
                performBinarySearchStep();
            }
        });

        // Step Backward when paused
        stepBackwardButton.setOnClickListener(v -> {
            if (isPaused) {
                performStepBackward();
            }
        });
    }


    private boolean getInputValues() {
        String arrayString = ((EditText) findViewById(R.id.arrayInput)).getText().toString();
        String targetString = ((EditText) findViewById(R.id.searchElementInput)).getText().toString();

        if (TextUtils.isEmpty(arrayString) || TextUtils.isEmpty(targetString)) {
            Toast.makeText(this, "Please enter both array and target", Toast.LENGTH_SHORT).show();
            return false;
        }
        try {
            String[] arrayItems = arrayString.split(",");
            array = new int[arrayItems.length];
            for (int i = 0; i < arrayItems.length; i++) {
                array[i] = Integer.parseInt(arrayItems[i].trim());
            }
            target = Integer.parseInt(targetString);

            // Sort the array for binary search (assuming unsorted initially)
            Arrays.sort(array);

            return true;
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input format", Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    private void displayArray() {
        arrayGrid.removeAllViews();
        for (int i = 0; i < array.length; i++) {
            TextView cell = new TextView(this);
            cell.setText(String.valueOf(array[i]));
            cell.setPadding(16, 16, 16, 16);
            cell.setGravity(Gravity.CENTER);
            cell.setBackgroundColor(Color.WHITE); // Default background
            cell.setTextColor(Color.BLACK);
            arrayGrid.addView(cell);
        }
    }

    private void startBinarySearch() {
        low = 0;
        high = array.length - 1;
        mid = (low + high) / 2;

        searchingValue.setText("Searching for: " + target);
        foundStatus.setText("");  // Clear found status

        // Set up the automatic search with a delay between steps
        searchRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isPaused) {
                    performBinarySearchStep();
                    handler.postDelayed(this, 1000); // Delay of 1 second
                }
            }
        };
        handler.post(searchRunnable); // Start automatic search
    }

    private void highlightPositions() {
        // Reset all positions to white
        for (int i = 0; i < arrayGrid.getChildCount(); i++) {
            TextView cell = (TextView) arrayGrid.getChildAt(i);
            cell.setBackgroundColor(Color.WHITE);
        }

        // Highlight low, mid, and high positions
        if (low < array.length) {
            TextView lowCell = (TextView) arrayGrid.getChildAt(low);
            lowCell.setBackgroundColor(Color.rgb(51,181,229));
        }

        if (mid < array.length) {
            TextView midCell = (TextView) arrayGrid.getChildAt(mid);
            midCell.setBackgroundColor(Color.rgb(153,204,0));
        }

        if (high < array.length) {
            TextView highCell = (TextView) arrayGrid.getChildAt(high);
            highCell.setBackgroundColor(Color.rgb(255, 165, 0)); // Orange
        }
    }

    private void performBinarySearchStep() {
        // Check if search is complete
        if (low <= high) {
            mid = (low + high) / 2;
            updatePointers();

            // Highlight the positions
            highlightPositions();

            // Perform binary search logic
            if (array[mid] == target) {
                foundStatus.setText("Found target at index: " + mid);
                handler.removeCallbacks(searchRunnable); // Stop further automatic steps
            } else if (array[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        } else {
            foundStatus.setText("Target not found");
            handler.removeCallbacks(searchRunnable); // Stop further automatic steps
        }
    }

    private void updatePointers() {
        lowValue.setText("Low: " + low);
        midValue.setText("Mid: " + mid);
        highValue.setText("High: " + high);
    }

    private void performStepBackward() {
        // Logic to step back in the binary search process (if needed)
        if (mid < array.length && mid > 0) {
            if (array[mid] > target) {
                high = mid + 1;
            } else {
                low = mid - 1;
            }
        }

        updatePointers();
        highlightPositions();
    }

}