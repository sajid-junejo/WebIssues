/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOImpl;
// Java Program to create nested JSplitPane,
// one of them is one Touch Expandable
import javax.swing.event.*;
import java.awt.*;
import javax.swing.*;
class solve extends JFrame {

	// frame
	static JFrame f;

	// text areas
	static JTextArea t1, t2, t3, t4;

	// main class
	public static void main(String[] args)
	{
		// create a new frame
		f = new JFrame("frame");

		// create a object
		solve s = new solve();

		// create a panel
		JPanel p1 = new JPanel();
		JPanel p = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();

		// create text areas
		t1 = new JTextArea(10, 10);
		t2 = new JTextArea(10, 10);
		t3 = new JTextArea(10, 10);
		t4 = new JTextArea(10, 10);

		// set texts
		t1.setText("this is first text area");
		t2.setText("this is second text area");
		t3.setText("this is third text area");
		t4.setText("this is fourth text area");

		// add text area to panel
		p1.add(t1);
		p.add(t2);
		p2.add(t3);
		p3.add(t4);

		// create a splitpane
		JSplitPane sl = new JSplitPane(SwingConstants.VERTICAL, p1, p);
		JSplitPane s2 = new JSplitPane(SwingConstants.VERTICAL, p2, p3);

		// set Orientation for slider
		sl.setOrientation(SwingConstants.VERTICAL);
		s2.setOrientation(SwingConstants.VERTICAL);

		s2.setOneTouchExpandable(true);

		// set divider location
		sl.setDividerLocation(70);

		// set Layout for frame
		f.setLayout(new FlowLayout());

		// add panel
		f.add(sl);
		f.add(s2);

		// set the size of frame
		f.setSize(600, 300);

		f.show();
	}
}

