package coursework;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.mysql.jdbc.ResultSetMetaData;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EasyDrive  implements ActionListener, ContainerListener{
	private JFrame frame;
	private ButtonPanel bp;
	private SelectionPanel sp;
	private JPanel rp;

	private String[] tableNames = DBOperation.getTableNames(true); // get Views 返回一组字符串数组，table的名字
	
	public EasyDrive() {
		frame = new JFrame("Store manager system!");
		
		System.out.println(Welcome.position);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		
		bp = new ButtonPanel(true);
		bp.setBackground(Color.BLUE);
		rp = new JPanel(new BorderLayout());
		rp.addContainerListener(this);
		rp.setBackground(Color.WHITE);
		sp = new SelectionPanel();
		sp.setBackground(Color.pink);
		rp.add(new JLabel(new ImageIcon("E:\\小学期\\store.jpg")));
		
		
		for(int i = 0; i < tableNames.length + 1; i++)
			bp.button[i].addActionListener(this);//button 为一个数组，显示所有的tablename
		
		frame.getContentPane().add(bp);
		frame.getContentPane().add(sp);
		frame.getContentPane().add(rp);
		
		frame.setMaximumSize(new Dimension(1062, 600));
		frame.setMinimumSize(new Dimension(1062, 600));
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		EasyDrive fancy = new EasyDrive();
				
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String tabName = ((JButton)e.getSource()).getText();
		System.out.println(tabName);
		if(tabName.equalsIgnoreCase("useful query")){
			System.out.println("Useful Query!");
			rp.removeAll();
			new UsefulQueryTab(sp, rp);
		}
		else{
			sp.setTableName(tabName);
			sp.setUpSelectionPanel(rp);
		
			//一开始点过之后即显示表
			SelectionPanel.showResult("Select * "+DBOperation.retailerSQL(tabName)+";", rp);
		}
		
		
		System.out.println("ButtonPanel: " + bp.getSize());
		System.out.println("Frame: " + frame.getSize());

	}

	@Override
	public void componentAdded(ContainerEvent e)
	{
		frame.repaint();
		//frame.pack();
	}

	@Override
	public void componentRemoved(ContainerEvent e)
	{
		frame.repaint();
		//frame.pack();
	}

}
