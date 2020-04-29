package coursework;
import javax.swing.*;
import javax.swing.border.Border;

import com.mysql.jdbc.ResultSetMetaData;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;

public class ButtonPanel extends JPanel{
	String[] tableNames;
	JButton[] button;

	public ButtonPanel(boolean basicTableOrNot) {
		tableNames = DBOperation.getTableNames(basicTableOrNot);
		if(tableNames==null){
			System.out.println("Empty Tab Names! Please Create View FIRST. ");
			System.exit(ABORT);
		}
			
		
		button = new JButton[tableNames.length + 1];
		
		this.setLayout(new GridLayout(0, tableNames.length + 1, 2, 2));
		this.setMaximumSize(new Dimension(1046, 26));
		this.setMinimumSize(new Dimension(1046, 26));
		
		for(int i = 0; i < tableNames.length; i++){
			button[i] = new JButton(tableNames[i]);
			this.add(button[i]);
		}
		button[tableNames.length] = new JButton("Useful Query");
		this.add(button[tableNames.length]);
		
	}

}
