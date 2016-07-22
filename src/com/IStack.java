package com;

/**
 * Created by Ricardo on 22/06/2016.
 */
interface IStack<E> {
    E push(E value);
    E pop();
    E top();
    boolean empty();
    void undo();
    void redo();
}
