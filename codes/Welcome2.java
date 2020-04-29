package coursework;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JList;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.SwingConstants;

public class Welcome2 extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	//private JTextField textField_2;
	static String position;
	public static String id;
	String pass;
	static String sql = null;  
    static DBConnect db1 = null;  
    static ResultSet ret = null; 
	static int m=0;
	static int n=0;
	public static void check()//检查是否选择两个position或者两个都没选	

{	//System.out.println("retailer被点了"+n+"次");
	//System.out.println("manager被点了"+m+"次");
	if(m%2==0&&n%2!=0)
		position="Retailer";
	
	else if(n%2==0&&m%2!=0)
		position="Manager";
	else if(n%2==0&&m%2==0)
	{
		position=null;
		JOptionPane.showMessageDialog(null, "You must choose one position!","WRONG",  JOptionPane.ERROR_MESSAGE);
	}
	else {
		position=null;
		JOptionPane.showMessageDialog(null,   "You can't choose  positions both!","WRONG",JOptionPane.ERROR_MESSAGE);
	}
	
}
private JPasswordField passwordField;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Welcome2 frame = new Welcome2();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Welcome2() {
		super("Store manager system");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 80, 916, 626);
		contentPane = new JPanel();
		contentPane.setBackground(Color.PINK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setLocationRelativeTo(null);

		
		
		JLabel lblNewLabel = new JLabel("Welcome to our store!");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(new Color(0, 153, 153));
		lblNewLabel.setFont(new Font("Century Schoolbook L", Font.BOLD, 39));
		lblNewLabel.setBounds(197, 12, 596, 83);
		contentPane.add(lblNewLabel);
		
		JLabel lblPosition = new JLabel("Position:");
		lblPosition.setForeground(new Color(0, 0, 0));
		lblPosition.setFont(new Font("Axure Handwriting", Font.BOLD, 24));
		lblPosition.setBounds(91, 122, 146, 33);
		contentPane.add(lblPosition);
		
		JRadioButton rdbtnRetailer = new JRadioButton("Retailer");
		
		rdbtnRetailer.setFont(new Font("Axure Handwriting", Font.PLAIN, 20));
		rdbtnRetailer.setBounds(273, 126, 194, 29);
		contentPane.add(rdbtnRetailer);
		
		JRadioButton rdbtnManager = new JRadioButton("Manager");
		rdbtnManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m++;
				check();
			}
		});
		rdbtnManager.setFont(new Font("Axure Handwriting", Font.PLAIN, 20));
		rdbtnManager.setBounds(540, 126, 199, 29);
		contentPane.add(rdbtnManager);
		rdbtnRetailer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//rdbtnManager.setVisible(false);
				n++;
				check();	
			}
		});
		
		passwordField = new JPasswordField();
		passwordField.setBounds(273, 295, 292, 41);
		contentPane.add(passwordField);
		
		JLabel lblId = new JLabel("ID:");
		lblId.setFont(new Font("Axure Handwriting", Font.BOLD, 24));
		lblId.setBounds(142, 217, 58, 29);
		contentPane.add(lblId);
		
		textField_1 = new JTextField();
		textField_1.setBounds(273, 215, 292, 41);
		contentPane.add(textField_1);
		textField_1.setColumns(20);
	
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Axure Handwriting", Font.BOLD, 24));
		lblPassword.setBounds(74, 299, 161, 33);
		contentPane.add(lblPassword);
		
		
		
		
		JButton btnLogIn = new JButton("Log in");
		btnLogIn.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				id=textField_1.getText();
				//System.out.println(id);
				pass=passwordField.getText();	
			
				if(position=="Manager")//SQLó???
				{
					sql="select * from Manager";
					
				}
				else if(position=="Retailer") {
					sql="select * from Retailer";
					
				}
				if(sql==null)
				{
					JOptionPane.showMessageDialog(null, "You must choose one position!", "WRONG",JOptionPane.ERROR_MESSAGE);
					
				}
				if(sql!=null) {
		        db1 = new DBConnect(sql);//????DBHelper???ó
		        try {  
		        	String pa=null;
		        	String i=null;
		        	
		            ret = db1.pst.executeQuery();//??DDó???￡?μ?μ??á1??ˉ  
		            while (ret.next()) {  
		            	i=ret.getString(1);
		                pa = ret.getString(2);  //×￠òa??±ê
		                /**byte[] md =pa.getBytes();
		                byte[] paSafe=MD5.computeMD5(md);
		                String sPass=new String(paSafe,"UTF-8");
		                */
		               // System.out.println(i+pa);
		                if(i.equals(id)&&pa.equals(pass)) {
		                		EasyDrive fancy = new EasyDrive();
		                		//System.out.println(i+pa);
		                		setVisible(false);
		                	
			                	break;
			                	
		                }
		            }
		            if(!(i.equals(id))||!(pa.equals(pass))) {
		            	JOptionPane.showMessageDialog(null,   "UserName or PassWord is Wrong","WRONG",JOptionPane.ERROR_MESSAGE);
		  
		            }
		            ret.close();  
		            db1.close();//1?±?á??ó  
		        } catch (SQLException e1) {  
		            e1.printStackTrace();  
		        } 
		        
			}}
		});
		btnLogIn.setFont(new Font("Axure Handwriting", Font.BOLD, 22));
		btnLogIn.setBounds(366, 412, 146, 47);
		contentPane.add(btnLogIn);
		
		
		

		
	}
}