package com;

import java.util.Arrays;

/**
 * Created by Ricardo on 21/06/2016.
 */
public class Stack<E> extends java.util.Stack<E> implements IStack<E>{

    private java.util.Stack<Command> undoCommandList;
    private java.util.Stack<Command> redoCommandList;

    public Stack() {
        undoCommandList = new java.util.Stack<>();
        redoCommandList = new java.util.Stack<>();
    }

    public void undo() {
        if(!undoCommandList.isEmpty()) {
            undoCommandList.pop().undo();
        }
    }

    public void redo() {
        if(!redoCommandList.isEmpty()) {
            redoCommandList.pop().redo();
        }
    }

    public void printState(){
        System.out.println("S=" + Arrays.toString(this.toArray())
                + " U=" + Arrays.toString(undoCommandList.toArray())
                + " R=" + Arrays.toString(redoCommandList.toArray()));
    }

    @Override
    public E push(E item) {
        redoCommandList.clear();
        undoCommandList.push(new PushCommand(item));
        return super.push(item);
    }

    @Override
    public synchronized E pop() {
        E value = super.pop();
        undoCommandList.push(new PopCommand(value));
        return value;
    }

    @Override
    public E top() {
        return this.lastElement();
    }

    private class PopCommand implements Command{
        private E value;

        public PopCommand(E value) {
            this.value = value;
        }

        @Override
        public void undo() {
            Stack.super.push(value);
            redoCommandList.push(this);
        }

        @Override
        public void redo() {
            Stack.super.pop();
            undoCommandList.push(this);
        }

        @Override
        public String toString() {
            return "Pop("+value.toString()+")";
        }
    }

    private class PushCommand implements Command{

        private E value;

        public PushCommand(E value) {
            this.value = value;
        }

        @Override
        public void undo() {
            Stack.super.pop();
            redoCommandList.push(this);
        }

        @Override
        public void redo() {
            Stack.super.push(value);
            undoCommandList.push(this);
        }

        @Override
        public String toString() {
            return "Push("+value.toString()+")";
        }
    }
}
