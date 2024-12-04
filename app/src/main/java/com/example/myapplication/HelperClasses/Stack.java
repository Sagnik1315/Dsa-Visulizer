package com.example.myapplication.HelperClasses;

public class Stack {
    private int maxSize;
    private int[] stackArray;
    private int top;

    public Stack(int size) {
        maxSize = size;
        stackArray = new int[maxSize];
        top = -1;
    }

    public boolean isFull() {
        return top == maxSize - 1;
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public void push(int value) {
        if (!isFull()) {
            stackArray[++top] = value;
        }
    }

    public int pop() {
        if (!isEmpty()) {
            return stackArray[top--];
        }
        return -1; // Indicates the stack is empty
    }

    public int peek() {
        if (!isEmpty()) {
            return stackArray[top];
        }
        return -1; // Indicates the stack is empty
    }
    public int[] getStackArray() {
        return stackArray;
    }

    public int getTop() {
        return top;
    }
}
