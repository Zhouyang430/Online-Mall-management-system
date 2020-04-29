package coursework;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.swing.JTable;

import com.mysql.jdbc.ResultSetMetaData;

public class DBConnect {
	private final String DBDRIVER = "com.mysql.jdbc.Driver";//com.mysql.jdbc.Driver//org.gjt.mm.mysql.Driver
	private final String DBURL = "jdbc:mysql://localhost:3306/storefinal2?useUnicode=true&characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=GMT";
	private final String DBUSER = "root";
	private final String DBPASSWORD = "Wangyf369";//qwertQWERT@123
	private Connection conn = null;
	public PreparedStatement pst = null;  

	public DBConnect() {
		try {
			Class.forName(DBDRIVER);
			this.conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	public DBConnect(String sql) {  
        try {  
            Class.forName(DBDRIVER);//ָ����������  
            conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);//��ȡ����  
            pst = conn.prepareStatement(sql);//׼��ִ�����  
        } catch (Exception e) {  
            e.printStackTrace();
            System.out.println("fail connect.");
        }  
    }  

	// ȡ�����ݿ�����
	public Connection getConnection() {
		return this.conn;
	}

	// �ر����ݿ�����
	public void close() {
		try {
			this.conn.close();
		} catch (Exception e) {
		}
	}

}
