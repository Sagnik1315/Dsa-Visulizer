package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class sorting extends AppCompatActivity {

    private EditText etInput;
    private Button btnReset;
    private RelativeLayout barContainer;
    private Button btnGenerate, btnRandom, btnSort, btnPause;
    private SeekBar sbSpeed;
    private TextView tvStatus;
    private int[] array;
    private View[] bars;
    private Handler handler = new Handler();
    private boolean isPaused = false,isresumed = false;
    private Button btnSelectionSort; // Add this with other buttons
    private Button btnInsertionSort; // Add this with other buttons
    private Button btnMergeSort;
    private Button btnQuickSort;



    private int speed = 500; // Default speed in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting);

        etInput = findViewById(R.id.et_input);
        barContainer = findViewById(R.id.barContainer);
        btnGenerate = findViewById(R.id.btn_generate);
        btnRandom = findViewById(R.id.btn_random);
        btnSort = findViewById(R.id.btn_sort);
        btnPause = findViewById(R.id.btn_pause);
        sbSpeed = findViewById(R.id.sb_speed);
        tvStatus = findViewById(R.id.tv_status);
        btnReset = findViewById(R.id.btn_reset);
        btnSelectionSort = findViewById(R.id.btn_selection_sort);
        btnInsertionSort = findViewById(R.id.btn_insertion_sort);
        btnMergeSort = findViewById(R.id.btn_merge_sort);
        btnQuickSort = findViewById(R.id.btn_quick_sort); // Or R.id.btnQuickSort




        btnReset.setOnClickListener(v -> {
            resetVisualization();
        });
        



        sbSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speed = 100 + (1000 - progress * 10); // Map slider progress to 100-1000ms
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnGenerate.setOnClickListener(v -> {
            String input = etInput.getText().toString().trim();
            if (validateInput(input)) {
                generateArray(input);
                initializeBars();
                btnSort.setEnabled(true);
                btnReset.setEnabled(true);
                btnSelectionSort.setEnabled(true);
                btnInsertionSort.setEnabled(true);
                btnMergeSort.setEnabled(true);
                btnQuickSort.setEnabled(true);
            } else {
                Toast.makeText(sorting.this, "Please enter valid numbers separated by spaces", Toast.LENGTH_SHORT).show();
            }
        });

        btnRandom.setOnClickListener(v -> {
            generateRandomArray();
            initializeBars();
            tvStatus.setText("");
            btnSort.setEnabled(true);
            btnReset.setEnabled(true);
            btnSelectionSort.setEnabled(true);
            btnInsertionSort.setEnabled(true);
            btnMergeSort.setEnabled(true);
            btnQuickSort.setEnabled(true);

        });


        btnSort.setOnClickListener(v -> {
            btnSort.setEnabled(false);
            btnPause.setEnabled(true);

            bubbleSortVisualization();
            btnReset.setEnabled(false);
            btnSelectionSort.setEnabled(false);
        });

        btnSelectionSort.setOnClickListener(v -> {
            btnSelectionSort.setEnabled(false);
            btnPause.setEnabled(true);
            selectionSortVisualization();
            btnReset.setEnabled(false);
            btnSort.setEnabled(false);
        });

        btnInsertionSort.setOnClickListener(v -> {
            btnInsertionSort.setEnabled(false);
            btnPause.setEnabled(true);
            insertionSortVisualization();
            btnSelectionSort.setEnabled(false);
            btnSort.setEnabled(false);
            btnReset.setEnabled(false);


        });
        btnMergeSort.setOnClickListener(v -> {
            btnMergeSort.setEnabled(false);
            btnPause.setEnabled(true);
            mergeSortVisualization(0, array.length - 1);
            btnReset.setEnabled(false);
            btnSort.setEnabled(false);
            btnSelectionSort.setEnabled(false);
            btnInsertionSort.setEnabled(false);
        });
        btnQuickSort.setOnClickListener(v -> {
            btnQuickSort.setEnabled(false);
            btnPause.setEnabled(true);

            quickSortVisualization(0, array.length - 1);

            btnReset.setEnabled(false);
            btnSort.setEnabled(false);
            btnSelectionSort.setEnabled(false);
            btnInsertionSort.setEnabled(false);
            btnMergeSort.setEnabled(false);
        });

        btnPause.setOnClickListener(v -> {
                isPaused = !isPaused;
            btnReset.setEnabled(false);

                if (isPaused) {
                    btnPause.setText("Resume"); // Change text to Resume
                    tvStatus.setText("Sorting Paused"); // Update status
                } else {
                    btnPause.setText("Pause"); // Change text to Pause
                    tvStatus.setText("Sorting Resumed"); // Update status
                }


            });



    }
    private void quickSortVisualization(int low, int high) {
        new Thread(() -> {
            try {
                if (low < high) {
                    int pivotIndex = partition(low, high);

                    // Recursive calls for left and right partitions
                    quickSortVisualization(low, pivotIndex - 1);
                    quickSortVisualization(pivotIndex + 1, high);
                }

                // Ensure all bars turn green after each partition is sorted
                handler.post(() -> {
                    for (View bar : bars) {
                        bar.setBackgroundColor(Color.GREEN);
                    }
                });

                // Final update after full sorting
                if (low == 0 && high == array.length - 1) {
                    handler.post(() -> {
                        tvStatus.setText("Sorting Complete!");
                        btnReset.setEnabled(true);
                        btnPause.setEnabled(false);
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }




    private int partition(int low, int high) throws InterruptedException {
        int pivot = array[high]; // Take the last element as pivot
        int i = low - 1; // Pointer for smaller element

        // Highlight pivot element
        int finalPivotIndex = high;
        handler.post(() -> bars[finalPivotIndex].setBackgroundColor(Color.YELLOW));

        for (int j = low; j < high; j++) {
            while (isPaused) {
                Thread.sleep(100); // Pause logic
            }

            int finalJ = j;

            // Highlight the element being compared
            handler.post(() -> bars[finalJ].setBackgroundColor(Color.RED));

            Thread.sleep(speed);

            if (array[j] < pivot) {
                i++;
                // Swap elements in the array
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;

                // Swap the bars visually
                int finalI = i;
                handler.post(() -> swapBars(finalI, finalJ));
            }

            // Reset the color of the compared bar
            handler.post(() -> bars[finalJ].setBackgroundColor(Color.BLUE));
        }

        // Place the pivot in the correct position
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;

        // Swap pivot bar visually
        int finalI1 = i + 1;
        handler.post(() -> swapBars(finalI1, finalPivotIndex));

        // Mark pivot position as sorted
        handler.post(() -> bars[finalI1].setBackgroundColor(Color.GREEN));

        return i + 1; // Return pivot index
    }

    private void mergeSortVisualization(int left, int right) {
        new Thread(() -> {
            try {
                performMergeSort(left, right);
                handler.post(() -> {
                    tvStatus.setText("Sorting Complete!");
                    for (View bar : bars) {
                        bar.setBackgroundColor(Color.GREEN);
                    }
                    btnReset.setEnabled(true);
                    btnPause.setEnabled(false);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void performMergeSort(int left, int right) throws InterruptedException {
        if (left < right) {
            int mid = left + (right - left) / 2;

            // Sort first and second halves recursively
            performMergeSort(left, mid);
            performMergeSort(mid + 1, right);

            // Merge the sorted halves
            merge(left, mid, right);
        }
    }

    private void merge(int left, int mid, int right) throws InterruptedException {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] L = new int[n1];
        int[] R = new int[n2];

        System.arraycopy(array, left, L, 0, n1);
        System.arraycopy(array, mid + 1, R, 0, n2);

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            while (isPaused) {
                Thread.sleep(100); // Pause logic
            }

            int finalK = k;
            handler.post(() -> bars[finalK].setBackgroundColor(Color.RED)); // Highlight merging bars

            Thread.sleep(speed);

            if (L[i] <= R[j]) {
                array[k] = L[i];
                int finalI = i;
                handler.post(() -> updateBar(finalK, L[finalI]));
                i++;
            } else {
                array[k] = R[j];
                int finalJ = j;
                handler.post(() -> updateBar(finalK, R[finalJ]));
                j++;
            }

            handler.post(() -> bars[finalK].setBackgroundColor(Color.BLUE)); // Reset color
            k++;
        }

        // Copy remaining elements of L[]
        while (i < n1) {
            while (isPaused) {
                Thread.sleep(100);
            }

            array[k] = L[i];
            int finalK = k;
            int finalI = i;
            handler.post(() -> updateBar(finalK, L[finalI]));
            i++;
            k++;
        }

        // Copy remaining elements of R[]
        while (j < n2) {
            while (isPaused) {
                Thread.sleep(100);
            }

            array[k] = R[j];
            int finalK = k;
            int finalJ = j;
            handler.post(() -> updateBar(finalK, R[finalJ]));
            j++;
            k++;
        }

        // Mark the merged section as sorted
        for (int x = left; x <= right; x++) {
            int finalX = x;
            handler.post(() -> bars[finalX].setBackgroundColor(Color.GREEN));
        }
    }

    private void updateBar(int index, int value) {
        bars[index].getLayoutParams().height = (int) ((value / (float) findMaxValue(array)) * barContainer.getHeight());
        bars[index].requestLayout();
    }



    private void insertionSortVisualization() {
        new Thread(() -> {
            try {
                for (int i = 1; i < array.length; i++) {
                    int key = array[i];
                    int j = i - 1;

                    // Highlight the key element
                    int finalI = i;
                    handler.post(() -> bars[finalI].setBackgroundColor(Color.YELLOW));

                    // Move elements greater than key
                    while (j >= 0 && array[j] > key) {
                        while (isPaused) {
                            Thread.sleep(100); // Wait if paused
                        }

                        int finalJ = j;

                        // Highlight comparison
                        handler.post(() -> bars[finalJ].setBackgroundColor(Color.RED));

                        Thread.sleep(speed); // Delay for visualization speed

                        // Move elements
                        array[j + 1] = array[j];
                        handler.post(() -> swapBars(finalJ, finalJ + 1));

                        // Reset bar color
                        handler.post(() -> bars[finalJ].setBackgroundColor(Color.BLUE));

                        j--;
                    }

                    // Place the key in the correct position
                    array[j + 1] = key;

                    // Highlight the bar where key is placed
                    int finalJ1 = j + 1;
                    handler.post(() -> bars[finalJ1].setBackgroundColor(Color.YELLOW));

                    // Mark sorted part
                    for (int k = 0; k <= i; k++) {
                        int finalK = k;
                        handler.post(() -> bars[finalK].setBackgroundColor(Color.GREEN));
                    }

                    Thread.sleep(speed); // Delay after each insertion
                }

                // Update status after sorting is complete
                handler.post(() -> {
                    tvStatus.setText("Sorting Complete!");
                    btnReset.setEnabled(true); // Enable reset button
                    btnPause.setEnabled(false); // Disable pause button

                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void resetVisualization() {

        // Clear the bar container
        barContainer.removeAllViews();
        // Reset all UI elements
        etInput.setText("");
        tvStatus.setText("");
        btnSort.setEnabled(false);
        btnPause.setEnabled(false);
        btnReset.setEnabled(false);
        btnSelectionSort.setEnabled(false);

        isPaused = false; // Ensure sorting isn't paused
        array = null; // Clear the array
        bars = null; // Clear the bars
        barContainer.removeAllViews();



    }

    private void selectionSortVisualization() {
        new Thread(() -> {
            try {

                for ( int i = 0; i < array.length - 1; i++) {
                    int minIndex = i;

                    // Highlight the starting element of the unsorted array
                    int finalMinIndex = minIndex;
                    handler.post(() -> bars[finalMinIndex].setBackgroundColor(Color.YELLOW));

                    for (int j = i + 1; j < array.length; j++) {
                        // Pause if sorting is paused
                        while (isPaused) {
                            Thread.sleep(100); // Wait in paused state
                        }

                        int finalJ = j;

                        // Highlight the element being compared
                        handler.post(() -> bars[finalJ].setBackgroundColor(Color.RED));

                        Thread.sleep(speed);

                        if (array[j] < array[minIndex]) {
                            // Reset the previous min color
                            int previousMinIndex = minIndex;
                            handler.post(() -> bars[previousMinIndex].setBackgroundColor(Color.BLUE));

                            minIndex = j;

                            // Highlight the new minimum element
                            int newMinIndex = minIndex;
                            handler.post(() -> bars[newMinIndex].setBackgroundColor(Color.YELLOW));
                        } else {
                            // Reset the color of the compared element
                            handler.post(() -> bars[finalJ].setBackgroundColor(Color.BLUE));
                        }
                    }

                    // Swap the minimum element with the first element of the unsorted part
                    if (minIndex != i) {
                        int temp = array[i];
                        array[i] = array[minIndex];
                        array[minIndex] = temp;

                        // Swap the bars visually
                        int finalMinIndex1 = minIndex;
                        int finalI = i;
                        handler.post(() -> swapBars(finalI, finalMinIndex1));

                    }

                    // Mark the sorted element
                    int sortedIndex = i;
                    handler.post(() -> bars[sortedIndex].setBackgroundColor(Color.GREEN));
                }

                // Mark the last element as sorted
                handler.post(() -> {
                    for (View bar : bars) {
                        bar.setBackgroundColor(Color.GREEN);
                    }
                    tvStatus.setText("Sorting Complete!");
                    btnReset.setEnabled(true); // Enable reset button here
                });

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void bubbleSortVisualization() {
        new Thread(() -> {
            try {
                for (int i = 0; i < array.length - 1; i++) {
                    for (int j = 0; j < array.length - 1 - i; j++) {
                        // Pause if sorting is paused
                        while (isPaused) {
                            Thread.sleep(100); // Wait in paused state
                        }

                        int finalJ = j;

                        // Highlight bars being compared
                        handler.post(() -> {
                            bars[finalJ].setBackgroundColor(Color.RED); // Current bar
                            bars[finalJ + 1].setBackgroundColor(Color.RED); // Next bar
                        });

                        Thread.sleep(speed); // Delay for visualization speed

                        if (array[j] > array[j + 1]) {
                            // Swap values in the array
                            int temp = array[j];
                            array[j] = array[j + 1];
                            array[j + 1] = temp;

                            // Swap the bar heights visually
                            handler.post(() -> swapBars(finalJ, finalJ + 1));
                        }

                        // Reset bar colors only if sorting is not paused
//                        if (!isPaused) {
                            handler.post(() -> {
                                bars[finalJ].setBackgroundColor(Color.BLUE);
                                bars[finalJ + 1].setBackgroundColor(Color.BLUE);
                            });
                        }
                    }
//                }

                // Set all bars to green when sorting is complete
                handler.post(() -> {
                    for (View bar : bars) {
                        bar.setBackgroundColor(Color.GREEN);
                    }
                    tvStatus.setText("Sorting Complete!");
                    if (tvStatus.getText().toString().equals("Sorting Complete!")){
                        btnReset.setEnabled(true);
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void swapBars(int index1, int index2) {
        int tempHeight = bars[index1].getLayoutParams().height;
        bars[index1].getLayoutParams().height = bars[index2].getLayoutParams().height;
        bars[index2].getLayoutParams().height = tempHeight;

        bars[index1].requestLayout();
        bars[index2].requestLayout();
    }

    private boolean validateInput(String input) {
        if (input.isEmpty()) return false;
        String[] numbers = input.split("\\s+");
        for (String num : numbers) {
            try {
                Integer.parseInt(num);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    private void generateArray(String input) {
        String[] numbers = input.split("\\s+");
        array = new int[numbers.length];
        bars = new View[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            array[i] = Integer.parseInt(numbers[i]);
        }
    }

    private void generateRandomArray() {
        Random random = new Random();
        int size = random.nextInt(10) + 5;
        array = new int[size];
        bars = new View[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(100) + 1;
        }
    }

    private void initializeBars() {
        barContainer.post(() -> {
            barContainer.removeAllViews();
            int maxValue = findMaxValue(array);
            int containerWidth = barContainer.getWidth();
            int barWidth = containerWidth / array.length;

            for (int i = 0; i < array.length; i++) {
                View bar = new View(this);
                int barHeight = (int) ((array[i] / (float) maxValue) * barContainer.getHeight());
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(barWidth - 8, barHeight);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                params.leftMargin = i * barWidth;
                bar.setLayoutParams(params);
                bar.setBackgroundColor(Color.BLUE);
                barContainer.addView(bar);
                bars[i] = bar;
            }
        });
    }

    private int findMaxValue(int[] array) {
        int max = array[0];
        for (int num : array) {
            if (num > max) max = num;
        }
        return max;
    }

   }