# Command exercise - Stack with undo and redo operations

Write a class Stack with 2 extra operations: 'undo' and 'redo'.

The first should invert the last operation(s) push/pop, the second - invert the inversion (cancel the 'undo' operation).

Example:
```
Stack S = new Stack(); S={}
S.push(2); S={2}
S.push(4);S={2, 4}
S.push(6); S={2, 4, 6}
S.undo(); S={2, 4} //undoing push(6) operation
S.undo(); S={2} //undoing push(4) operation
S.redo(); S={2, 4} //redoing the last undone operation (push(4))
S.push(10); S={2, 4, 10}
* S.redo() => impossible, since no previous 'undo' operation
S.pop(); S={2, 4}
S.undo(); S={2, 4, 10} //undoing pop() operation
S.redo(); S={2, 4} //redoing the last undone operation (pop())
```


To do it, you need a Command interface:
```
interface Command {
void undo();
void redo();
}
```
and two concrete classes implementing this interface: PopCommand, and PushCommand.


In each 'pop' and 'push' operations of the Stack class, except updating the stack state (content), a new object of the corresponding class (PopCommand, PushCommand) is created and stored in a separate list (history) inside the stack object (for instance called undoCommandList). This object should know:
1 What number was poped/pushed;
2 From which stack.
The second functionality can be realized as an inner Java class (preferably) - PopCommand and PushCommand can be inner classes of the Stack class.

Finally, the 'undo' operation of the class Stack should take the last object from the undoCommandList, perform its own 'undo' operation and move it to the redoCommandList. Similarly the 'redo' operation. The above example, with contents of undoCommandList (U) and redoCommandList(R) - PopCommand and PushCommand objects - are presented below:

```
Stack S = new Stack(); S={}, U={}, R={},
S.push(2); S={2}, U={PUSH(2)}, R={},
S.push(4);S={2, 4}, U={PUSH(2), PUSH(4)}, R={},
S.push(6); S={2, 4, 6}, U={PUSH(2), PUSH(4), PUSH(6)}, R={},
S.undo(); S={2, 4},U={PUSH(2), PUSH(4)}, R={PUSH(6)},
S.undo(); S={2}, U={PUSH(2)}, R={PUSH(6), PUSH(4)}
S.redo(); S={2, 4}, U={PUSH(2), PUSH(4)}, R={PUSH(6)}
S.push(10); S={2, 4, 10}, U={PUSH(2), PUSH(4), PUSH(10)}, R={}
* S.redo() => impossible, since no previous 'undo' operation
S.pop(); S={2, 4}, U={PUSH(2), PUSH(4), PUSH(10), POP(10)}, R={}
S.undo(); S={2, 4, 10}, U={PUSH(2), PUSH(4), PUSH(10)}, R={POP(10)}
S.redo(); S={2, 4},U={PUSH(2), PUSH(4), PUSH(10), POP(10)}, R={}
```

Original code:
```
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.event.*;

public class StackUndoRedo extends JFrame{
private IStack stack=new Stack();
JTextField message = new JTextField("<empty>");
public static void main(String[] args) {
new StackUndoRedo().setVisible(true);
}
StackUndoRedo() {
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
getContentPane().setLayout(new GridLayout(2,3));
JButton b=new JButton("Push"); add(b);
b.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent e) {
String s=JOptionPane.showInputDialog(null, "Give a number: ");
if (s==null) return;
int i=0;
try {
i=Integer.parseInt(s);
stack.push(i);
refresh();
}
catch (Exception ex) { JOptionPane.showMessageDialog(null, "Bad number"); }
}
});
b=new JButton("Undo"); add(b);
b.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent e) {
stack.undo();
refresh();
}
});
add(new JLabel("Top of the stack: "));
b=new JButton("Pop"); add(b);
b.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent e) {
if (stack.empty())
JOptionPane.showMessageDialog(null, "Stack empty !");
else { stack.pop(); refresh(); }
}
});
b=new JButton("Redo"); add(b);
b.addActionListener(new ActionListener() {
public void actionPerformed(ActionEvent e) {
stack.redo();
refresh();
}
});
add(message);

pack();
}
private void refresh(){
if (stack.empty()) message.setText("<empty>");
else message.setText(stack.top()+"");
}
}

interface IStack {
void push(int i);
void pop();
int top();
boolean empty();
void undo();
void redo();
}

interface Command {
void undo();
void redo();
}

class Stack implements IStack {
...
}
```