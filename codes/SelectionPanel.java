package coursework;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class SelectionPanel extends JPanel{

	private static JComboBox[] selectionBox;//����ѡ������ѡ���������������������������������������������������������������������������������������������������������
	private List list;
	private static JCheckBox[] columnNames;//�����������ơ�����������������������������������������������������������������������������
	private JButton queryButton;
	private static String tableName;
	private static String[] fieldNameArray;
	private ArrayList<String> resultSQLParams;
	private ArrayList<String> toShownColumns;
	private JPanel rp;
	private JPanel addbp; // new JPanel(new GridLayout(0, 6, 2, 2));
	private JPanel addrp = new JPanel(new GridLayout(0, 1, 2, 2));
	private static JTable rTable;
	private static JButton upButton=new JButton("Update");
	private static TableModel model;
	private static String upsql;
	private static int[] except=null;
	
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public SelectionPanel() {
		this.setLayout(new FlowLayout());
		JLabel msg = new JLabel("Welcome to Store manager System!");
		msg.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(msg);
		
		if(Welcome.position=="Retailer") {
			addbp = new JPanel(new GridLayout(0, 1, 2, 2));
		}
		else addbp = new JPanel(new GridLayout(0, 6, 2, 2));
		
	}

	public void setUpSelectionPanel(JPanel rp)
	{
		this.setLayout(new GridLayout(0, 8, 2, 2));
		this.rp = rp;

		String sql = "select * " + DBOperation.retailerSQL(tableName)+";";//sql�������
		

		list = DBOperation.queryGetList(sql);
		fieldNameArray = (String[]) list.get(0);//������
	//	if(Welcome.position=="Retailer") {
		//	except=null;
		//	int j=0;
		//	for(int i=0;i<fieldNameArray.length;i++) {
		//		if(fieldNameArray[i].contentEquals("manager")||fieldNameArray[i].contentEquals("customer")||fieldNameArray[i].contentEquals("retailer")) {
		//			except[j++]=i;
		//		}
					
				
		//	}
	//	}
		

		columnNames = new JCheckBox[fieldNameArray.length];
		selectionBox = new JComboBox[fieldNameArray.length];

		this.removeAll();
		// ��Ӹ�����label��������
		for (int i = 0; i < fieldNameArray.length; i++)
		{
		//	if(DBOperation.exceptIn(except, i)) 
		//		continue;
			
			columnNames[i] = new JCheckBox(fieldNameArray[i]);
			selectionBox[i] = new JComboBox<String>();
			selectionBox[i].addItem("");
			this.add(columnNames[i]);
			this.add(selectionBox[i]);
		}

		// ��Ӳ�ѯ��ť
		queryButton = new JButton("Query");
		queryButton.addActionListener(new checkButtonListener());
		this.add(queryButton);

		JButton addButton = new JButton("Add Data");
		addButton.addActionListener(new addButtonListener());
		this.add(addButton);
		
		JButton upButton = new JButton("Update Data");//�¼ӵĸ��ļ�
	    upButton.addActionListener(new upButtonListener());
		this.add(upButton);

		// ���ص���Ŀ������0����һ��������
		if (list.size() > 1)
		{
			// �������������
			for (int i = 1; i < list.size(); i++)
			{
				String[] tempArray = (String[]) list.get(i);//��������
				for (int j = 0; j < tempArray.length; j++)
				{
					//if
					if (tempArray[j] != null)
					{
						try
						{
							int value = Integer.parseInt(tempArray[j]);
							selectionBox[j].setEditable(true);
						} catch (Exception e)
						{
							selectionBox[j].setEditable(false);
						} finally
						{
							String temp = convertBoxToString(selectionBox[j]);//
							//System.out.println(temp);
							if (temp.indexOf(tempArray[j]) == -1)
								selectionBox[j].addItem(tempArray[j]);
						}
					}
				}
			}
		}

		this.updateUI();
		this.repaint();

	}

	public String convertBoxToString(JComboBox b)//���ѡ����Ϣ���ݡ���������������������������������������������������������������������������������
	{
		StringBuilder s = new StringBuilder("");
		for (int i = 0; i < b.getItemCount(); i++)
		{
			s.append(b.getItemAt(i));
			s.append(", ");
		}
		return s.toString();
	}

	/* An inner class that serve as the listener class */
	class checkButtonListener implements ActionListener//check���ݿ������� .....................................................................................
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{

			toShownColumns = new ArrayList<String>();
			resultSQLParams = new ArrayList<String>();

			for (int i = 0; i < fieldNameArray.length; i++)
			{
				// ����ѡ�е�����
				if (columnNames[i].isSelected())
					toShownColumns.add(tableName + "." + "`" + columnNames[i].getText() + "`");

				// ����ѡ�е�����ֵ
				String temp = (String) selectionBox[i].getSelectedItem();
				if (temp != "")
				{
					if (selectionBox[i].isEditable() && temp.indexOf("-") != -1)
					{
						String[] splitStr = temp.split("-");
						try
						{
							int min = Integer.parseInt(splitStr[0]);
							int max = Integer.parseInt(splitStr[1]);
							resultSQLParams.add(tableName + "." + "`" + fieldNameArray[i] + "`" + " between " + min
									+ " and " + max);
						} catch (Exception e1)
						{
							System.out.println("Invalid parameters! Try again");
							e1.printStackTrace();
						}

					} else
					{
						resultSQLParams.add(tableName + "." + "`" + fieldNameArray[i] + "`" + " = '" + temp + "'");
					}
				}
			}

			/*
			 * Validation only for (int i = 0; i < resultSQLParams.size(); i++)
			 * System.out.println(resultSQLParams.get(i));
			 * 
			 * for (int i = 0; i < toShownColumns.size(); i++)
			 * System.out.println(toShownColumns.get(i));
			 */

			// �����ѯ���
			StringBuilder sql = new StringBuilder("SELECT DISTINCT ");//sql����ѯ����������������������������������������������������������������������������������������������
			if (toShownColumns.size() > 0)//����ѡ�е�����
			{
				for (int i = 0; i < toShownColumns.size() - 1; i++)
				{
					sql.append(toShownColumns.get(i) + ", ");
				}
				sql.append(toShownColumns.get(toShownColumns.size() - 1) + DBOperation.retailerSQL(tableName));
			} else
			{
				sql.append(" * " + DBOperation.retailerSQL(tableName));
			}

			if (resultSQLParams.size() > 0)//��ѡ�����������
			{
				if(Welcome.position=="Retailer") {
					sql.append(" and ");
				}
				else sql.append(" where ");
				
				for (int i = 0; i < resultSQLParams.size() - 1; i++)
				{
					sql.append(resultSQLParams.get(i) + " and ");
				}
				sql.append(resultSQLParams.get(resultSQLParams.size() - 1) + ";");
			} else
			{
				sql.append(";");
			}

			System.out.println(sql.toString());

			showResult(sql.toString(), rp);
		}
	}

	public static void showResult(String sql, JPanel rp)//show result........................!!!!!!!!!!...............................................................
	{
		rp.removeAll();
		try{
			rTable = DBOperation.queryGetTable(sql.toString());//��ʾ�ı�sql!!!!................................!!!!1!1!!!
			// ���JTable��Panel��
			
	        //rTable = new JTable(model);
	        rTable.setRowSelectionAllowed(true);
	        rTable.setColumnSelectionAllowed(true);
	       // rTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			JScrollPane temp = new JScrollPane(rTable);
			// rp.setViewportView(rTable);
			rp.add(temp, BorderLayout.CENTER);
			JPanel prompt = new JPanel(new BorderLayout());
			JPanel prompt_1 = new JPanel();//��ʾsql���
			prompt_1.setBackground(Color.yellow);
			JPanel prompt_2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
			prompt_2.setBackground(Color.orange);
			prompt_1.add(new JLabel(sql));
			
			if(rTable!=null)
				prompt_2.add(new JLabel("Total: " + (rTable.getRowCount() > 0 ? rTable.getRowCount() : 0) + " rows returned."));//��ʾ������
			else
				prompt_2.add(new JLabel("Total: 0 rows returned."));
			prompt.add(prompt_1, BorderLayout.NORTH);
			prompt.add(prompt_2, BorderLayout.SOUTH);
			//prompt.setBackground(Color.orange);
			rp.add(prompt,BorderLayout.SOUTH);
			rp.updateUI();
			rp.repaint();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
	}

	class addButtonListener implements ActionListener//add data............................................................................
	{
		private String tabName;
		//JDialog dialog;
		String[] addTableNames;//�������
		JButton[] button;
		JLabel[] inputLabels;
		JTextField[] inputTextField;//���ĵ�����ֵ
		String[] addFieldNames;//���ĵ�������
		JButton OKbutton;
		Timer timer;
		int len;
		int p=0;
		int po=0;
		JComboBox[] specialid;

		@Override
		public void actionPerformed(ActionEvent e)
		{
			
			JFrame dialog = new JFrame("ADD DATA PAGE");
			if(Welcome.position=="Retailer") {
				addTableNames=new String[1];
				addTableNames[0]="product";
			}
			else addTableNames = DBOperation.getTableNames(true);
			button = new JButton[addTableNames.length];
			
			dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
			dialog.setBackground(Color.magenta);
			OKbutton = new JButton("OK");
			addrp.removeAll();
			addbp.removeAll();
			
			 
			timer = new Timer(1000, new ActionListener() {//Timer��  
	            int count = 1;  
	  
	            public void actionPerformed(ActionEvent e) {  
	                count--;  
	                if (count < 0) {//����count��ֵ�����жϣ����Խ��кܶ��Լ��Ĵ���  
	                    timer.stop();  
	                    
	                    dialog.dispose();//ʱ�䵹����ϣ�����Dialog  
	                }
	            } 
	        });  
			
			for (int i = 0; i < addTableNames.length; i++)
			{
				button[i] = new JButton(addTableNames[i]);
				addbp.add(button[i]);
			}
			addbp.setBackground(Color.BLUE);
			addbp.setMaximumSize(new Dimension(1046, 26));
			addbp.setMinimumSize(new Dimension(1046, 26));
			addrp.setBackground(Color.PINK);
			addrp.setAlignmentX(CENTER_ALIGNMENT);
			addrp.setAlignmentY(LEFT_ALIGNMENT);
			dialog.getContentPane().add(addbp);
			JScrollPane temp = new JScrollPane(addrp);
			dialog.getContentPane().add(temp);
			dialog.getContentPane().add(OKbutton);


			for (int i = 0; i < addTableNames.length; i++)
				button[i].addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e)
					{
						p=0;po=0;
						addrp.removeAll();
						
						tabName = ((JButton) e.getSource()).getText();
						if(tabName.contentEquals("product")) {
							p=1;
						}
						else if(tabName.contentEquals("productorder")) {
							po=1;
						}
						
						System.out.println(tabName);
						addFieldNames = DBOperation.getFieldNameArray(tabName);
						len=addFieldNames.length;
						System.out.println("beforeeeeeeeeeeeeeeeeeeeeeeeeee"+addFieldNames.length);

						if(Welcome.position=="Retailer") {//retailer����
							for(int i=0;i<addFieldNames.length;i++) {
								if(addFieldNames[i].contentEquals("retID")) {
									for(int j=i;j<addFieldNames.length-1;j++) {
										addFieldNames[j]=addFieldNames[j+1].toString();
									}
									len--;
									//System.out.println("afterrrrrrrrrrrrrrrrrrrrrrrrrrrrr"+addFieldNames.length);
									break;
								}
							}
						}
						
						inputLabels = new JLabel[len];
						if(p==1) {
							inputTextField = new JTextField[len];
							specialid=new JComboBox[1];
						}
						else if(po==1) {
							inputTextField = new JTextField[len];
							specialid=new JComboBox[3];
						}
						else inputTextField = new JTextField[len];
						
						
						int j=0;
						for (int i = 0; i < inputLabels.length; i++)
						{
							inputLabels[i] = new JLabel(addFieldNames[i],JLabel.CENTER);
							
							addrp.add(inputLabels[i]);
							
							System.out.println(p+"and"+po);
							if(p==1&&addFieldNames[i].contentEquals("retID")) {
								System.out.println(p+"and ppppppppppppppppppppppp");
								specialid[0]=DBOperation.addid(addFieldNames[i]);
								addrp.add(specialid[0]);
							}
							
							else if(po==1&&addFieldNames[i].contentEquals("comID")) {
								System.out.println(po+"andpooooooooooooooooooooooooooooooo");
								specialid[0]=DBOperation.addid(addFieldNames[i]);
								addrp.add(specialid[0]);
							}
							else if(po==1&&addFieldNames[i].contentEquals("proID")) {
									specialid[1]=DBOperation.addid(addFieldNames[i]);
									addrp.add(specialid[1]);
							}
							else if(po==1&&addFieldNames[i].contentEquals("cusID")) {
									specialid[2]=DBOperation.addid(addFieldNames[i]);
									addrp.add(specialid[2]);
							}
							
							else {
								inputTextField[i] = new JTextField(30);
								addrp.add(inputTextField[i]);
							}
							
						}
						addrp.updateUI();
						addrp.repaint();
						dialog.repaint();
						
					}

				});

			

			OKbutton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e)
				{
					try
					{
					
						System.out.println(tabName);
					
						String idsql="select * from "+tabName;
						List lid=DBOperation.queryGetList(idsql);
						for(int i=1;i<lid.size();i++) {
							String[] temp= (String[]) lid.get(i);
							if(inputTextField[0].getText().contentEquals(temp[0])) {
								inputTextField[0].setText(temp[0]+1);
								break;
							}
								
						}
						
						StringBuffer inputresult = new StringBuffer();
						StringBuffer columnresult = new StringBuffer();

						int i=0;
						for (int j = 0; j < len; j++)
						{
							if (j == 0)
							{
								inputresult.append("'" + inputTextField[j].getText() + "'");
								columnresult.append("`" + inputLabels[j].getText() + "`");
							} else
							{
								if(inputTextField[j]==null) {
									inputresult.append(", " + "'" + (String)specialid[i++].getSelectedItem() + "'");
								}
								else inputresult.append(", " + "'" + inputTextField[j].getText() + "'");
								columnresult.append(", " + "`" + inputLabels[j].getText() + "`");
							}

						}
						
						if(Welcome.position=="Retailer") {
							inputresult.append(",'" + Welcome.id + "'");
							columnresult.append(",`retid`");
						}
						
						String addinputresult = inputresult.toString();
						String addcolumnresult = columnresult.toString();

						System.out.println("Label: " + addcolumnresult.toString());
						System.out.println("Input: " + addinputresult.toString());

						String addsql;
					//	if(Welcome.position=="Retailer") addsql="insert into " + tabName + "(" + addcolumnresult + ",retid) " + " values( " + addinputresult + ","+Welcome.id+");";
						//else 
							addsql = "insert into " + tabName + "(" + addcolumnresult + ") " + " values( " + addinputresult + ");";
						System.out.print(addsql);
						
						String result = DBOperation.updateToDB(addsql);
						//System.out.println(result);
						
						if(result.equalsIgnoreCase("1")){
							addrp.removeAll();
							//addrp.setLayout(new FlowLayout());
							addrp.add(new JLabel("Success!"));
							addrp.updateUI();
							dialog.repaint();
							timer.start();
						}

					} catch (Exception e1)
					{
						e1.printStackTrace();
					}

				}

			});
			
			dialog.setVisible(true);
			dialog.setMaximumSize(new Dimension(1062, 522));
			dialog.setMinimumSize(new Dimension(1062, 522));
			dialog.pack();
		}


	}
	
	class upButtonListener implements ActionListener//add data............................................................................
	{
		private JPanel tabp;
		private JPanel attp;
		private JPanel chep;
		private JTextField textField;
		String[] tablename;
		String[] attribute;
		String[] value;
		int v;
		private List list;
		String upsql;
		Timer timer;

		@Override
		public void actionPerformed(ActionEvent e)
		{
			
			JFrame dialog = new JFrame("UPDATE DATE PAGE");
			dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			dialog.setLayout(new BoxLayout(dialog.getContentPane(), BoxLayout.Y_AXIS));
			dialog.setBackground(Color.magenta);
			
			JLabel lblTableName = new JLabel("Table name:");
			JLabel lblId = new JLabel("ID:");
			JLabel lblAttribute = new JLabel("Attribute");
			JLabel lblCorrect = new JLabel("Correct to:");
			
			JComboBox selecttable = new JComboBox();
			selecttable.addItem("");
			JComboBox selectid = new JComboBox();
			JComboBox selectAtt = new JComboBox();
			
			if(Welcome.position=="Retailer") {
				tablename=new String[1];
				tablename[0]="product";
			}
			else tablename=DBOperation.getTableNames(true);
			JButton btnCheck = new JButton("check");
			JButton btnOk = new JButton("OK");
			textField = new JTextField();
			textField.setColumns(100);
			
			tabp=new JPanel();
			attp=new JPanel();
			chep=new JPanel();
			
			tabp.removeAll();
			attp.removeAll();
			chep.removeAll();
			
			tabp.add(lblTableName);
			tabp.add(selecttable);
			tabp.add(btnCheck);
			attp.add(lblId);
			attp.add(selectid);
			attp.add(lblAttribute);
			attp.add(selectAtt);
			chep.add(lblCorrect);
			chep.add(textField);
			
			timer = new Timer(1000, new ActionListener() {//Timer��  
	            int count = 1;  
	  
	            public void actionPerformed(ActionEvent e) {  
	                count--;  
	                if (count < 0) {//����count��ֵ�����жϣ����Խ��кܶ��Լ��Ĵ���  
	                    timer.stop();  
	                    
	                    dialog.dispose();//ʱ�䵹����ϣ�����Dialog  
	                }
	            } 
	        });  
			
			tabp.setBackground(Color.pink);
			tabp.setMaximumSize(new Dimension(1046, 300));
			tabp.setMinimumSize(new Dimension(1046, 66));
			attp.setBackground(Color.white);
			attp.setMaximumSize(new Dimension(1046, 300));
			attp.setMinimumSize(new Dimension(1046, 66));
			chep.setBackground(Color.pink);
			chep.setAlignmentX(CENTER_ALIGNMENT);
			chep.setAlignmentY(LEFT_ALIGNMENT);
			//chep.setMaximumSize(new Dimension(1046, 60));
			//chep.setMinimumSize(new Dimension(1046, 26));
			dialog.getContentPane().add(tabp);
			dialog.getContentPane().add(attp);
			dialog.getContentPane().add(chep);
			dialog.getContentPane().add(btnOk);


			if(tablename.length>0) {
				for(int i=0;i<tablename.length;i++)
				{
					if(tablename[i]!=null) {
						try {
							selecttable.setEditable(true);
							selecttable.addItem(""+tablename[i]);
						}
						catch (Exception e1)
						{
							selecttable.setEditable(false);}
					}
				}
			}

			btnCheck.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					selectid.removeAllItems();
					
					String table=(String) selecttable.getSelectedItem();//ȷ��ѡ�еı�
					if(table!="") {
						selectAtt.removeAllItems(); 
						upsql=" Update "+table+" set "+table+".";
						System.out.println(table+"tttttttttttttttttttttttttttttttttttttttttttt");
						
						String sql="select * from " + table+";";
						String sql2="select * "+DBOperation.retailerSQL(table)+";";
						
						System.out.println(sql+"..."+sql2);
						list = DBOperation.queryGetList(sql);
						List list2= DBOperation.queryGetList(sql2);
						System.out.println("sqlinnnnnnnnnnnn22222222222222222222222");
						attribute = (String[]) list.get(0);//������
						//value=list.get(1).getClass().getName();//��øñ��һ����ֵ
						
						System.out.println(value);
						
						if(attribute.length>0) {
							for(int i=1;i<attribute.length;i++)
							{
								if(attribute[i]!=null) {
									try {
										v=i;
										selectAtt.setEditable(true);
										selectAtt.addItem(""+attribute[i]);
										for(int j=1;j<list2.size();j++) {
											String[] temp= (String[]) list2.get(j);
											selectid.addItem(temp[0]);
										}
									}
									catch (Exception e1)
									{
										selectAtt.setEditable(false);}
								}
							}
						}
					}
				}
			});
			
			
			btnOk.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e)
				{
					try
					{
						String att=(String) selectAtt.getSelectedItem();
						String textin =DBOperation.judgevalue(att,textField.getText());
						System.out.println(textin);
						
						upsql=upsql+att +"= " + textin+" where "
				              +attribute[0]+" = '"+(String) selectid.getSelectedItem()+"' ;";
				      
						System.out.println(upsql);
						
						String result = DBOperation.updateToDB(upsql);
						
						if(result.equalsIgnoreCase("1")){
							addrp.removeAll();
							//addrp.setLayout(new FlowLayout());
							addrp.add(new JLabel("Success!"));
							addrp.updateUI();
							dialog.repaint();
							timer.start();
						}

					} catch (Exception e1)
					{
						e1.printStackTrace();
					}

				}

			});
			
			dialog.setVisible(true);
			dialog.setMaximumSize(new Dimension(1062, 522));
			dialog.setMinimumSize(new Dimension(1062, 522));
			dialog.pack();
		}


	}


}
