package coursework;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JTable;
import javax.swing.JComboBox;

public class DBOperation {
	// Query and get the list
		public static List queryGetList(String sql)//获得数据集
		{
			List ls = null;
			//List retl=null;
			DBConnect dbc = null;
			java.sql.PreparedStatement pstmt = null;
			try
			{
				dbc = new DBConnect();
				pstmt = dbc.getConnection().prepareStatement(sql);
				java.sql.ResultSet rs = pstmt.executeQuery();
				ls = resultSetToList(rs);
				rs.close();
				pstmt.close();
			} catch (SQLException e)
			{
				System.out.println(e.getMessage());
			} finally
			{
				dbc.close();
			}

			return ls;// ArrayList<HashMap> 每一个HashMap是<列名,
			// 值>组合，ArrayList的大小是返回的条目数（行数）
		}
		
		// DML 
		public static String updateToDB(String sql){
			String result = null;
			DBConnect dbc = null;
			java.sql.PreparedStatement pstmt = null;
			try
			{
				dbc = new DBConnect();
				pstmt = dbc.getConnection().prepareStatement(sql);
				result = String.valueOf(pstmt.executeUpdate());
				pstmt.close();
			} catch (Exception e)
			{
				result = e.getMessage();
			} finally
			{
				dbc.close();
			}
			return result;
		}
		
		// Query and get Jtable
		public static JTable queryGetTable(String sql)//获得数据table。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。!!!!!                      !!!!!          。。。。。。
		{ // 用法：Query返回的是ResultSet类型，如需转化成Table方便展示，请用下面的converToTable(ResultSet)方法
			JTable table = null;
			DBConnect dbc = null;
			java.sql.PreparedStatement pstmt = null;
			try
			{
				dbc = new DBConnect();
				pstmt = dbc.getConnection().prepareStatement(sql);
				java.sql.ResultSet rs = pstmt.executeQuery();//执行语句到数据库中获得 结果集
				int column = rs.getMetaData().getColumnCount();
				int row = 0;
				while (rs.next())
					row++;

				if (rs.first())
				{
					Object[][] base = new Object[row][column];
					Object[] name = new Object[column];
					int j = 0;

					for (int i = 0; i < row; i++)
					{

						for (j = 1; j <= column; j++)
						{
							name[j - 1] = rs.getMetaData().getColumnName(j);//循环将数据集的列都导入到name中
							base[i][j - 1] = rs.getObject(j);//第几行第几列 单个元素存在base中
						}
						rs.next();

					}
					table = new JTable(base, name);
				}
				rs.close();
				pstmt.close();
			} catch (SQLException e)
			{
				System.out.println(e.getMessage());
			} finally
			{
				dbc.close();
			}

			return table;

		}
		/*
		 * public static List resultSetToList(ResultSet rs) throws
		 * java.sql.SQLException { if (rs == null) return Collections.EMPTY_LIST;
		 * java.sql.ResultSetMetaData md = rs.getMetaData();
		 * //得到结果集(rs)的结构信息，比如字段数、字段名等 int columnCount = md.getColumnCount(); //返回此
		 * ResultSet 对象中的列数 List list = new ArrayList<Map>(); Map rowData = null;
		 * while (rs.next()) { rowData = new HashMap(columnCount); for (int i = 1; i
		 * <= columnCount; i++) { rowData.put(md.getColumnName(i), rs.getObject(i));
		 * } list.add(rowData); System.out.println("list:" + list.toString()); }
		 * return list; }
		 */

		public static List resultSetToList(ResultSet rs) throws java.sql.SQLException
		{
			if (rs == null)
				return Collections.EMPTY_LIST;
			ArrayList<String[]> RsArrayList = new ArrayList<String[]>();
			java.sql.ResultSetMetaData metaData = rs.getMetaData(); // 得到结果集(rs)的结构信息，比如字段数、字段名等
			int colCount = metaData.getColumnCount(); // 返回此 ResultSet 对象中的列数
			String[] fieldNameArray = new String[colCount];
			for (int i = 0; i < colCount; i++)
			{
				fieldNameArray[i] = new String(metaData.getColumnName(i + 1));
			}
			RsArrayList.add(fieldNameArray);

			while (rs.next())
			{
				String[] tempArray = new String[colCount];
				for (int i = 0; i < colCount; i++)
				{
					tempArray[i] = rs.getString(fieldNameArray[i]);
				}
				RsArrayList.add(tempArray);
			}

			return RsArrayList;
		}
		
		public static String[] getTableNames(boolean getRawTableOrNot){

			DBConnect dbc = null;
			java.sql.PreparedStatement pstmt = null;
			String[] tableNames = null;
			try
			{
				dbc = new DBConnect();
				pstmt = dbc.getConnection().prepareStatement("Show tables;");
				java.sql.ResultSet rs = pstmt.executeQuery();
				ArrayList<String> temp = new ArrayList<String>();
				
				while(rs.next()){
					String c=rs.getString(1);
					System.out.println(c);
					if(Welcome.position=="Retailer")
						System.out.println("retailer login");
					if(Welcome.position=="Retailer"&&(c.contentEquals("manager")||c.contentEquals("customer")||c.contentEquals("retailer"))) {
						System.out.println("retailer continue");
						continue;
					}
					// 依据getRawTableOrNot决定是只添加view还是只添加基本表，这在插入数据时会用到
					if(!getRawTableOrNot && rs.getString(1).toLowerCase().indexOf("_view")!=-1)
						temp.add(rs.getString(1)); // Retrieve the value in the first column
					else if (getRawTableOrNot && rs.getString(1).toLowerCase().indexOf("_view")==-1)
						temp.add(rs.getString(1));
				}
				if(temp.size()>0)
				{
					tableNames = new String[temp.size()];
					for(int i = 0; i < temp.size(); i++){
						tableNames[i] = temp.get(i);
					}
				}
				
				rs.close();
				pstmt.close();
			} catch (SQLException e)
			{
				System.out.println(e.getMessage());
			} finally
			{
				dbc.close();
			}
			return tableNames;
		}
		
		public static String retailerSQL(String t) {
			String rsql=null;
			
			if(Welcome.position=="Retailer") {
				System.out.println(t+"rrrrrrrrrrrrrrrrrrrrrrrrrrrrttttttttttttttttttttttt");
				if(t.contentEquals("comment")) {
					System.out.println("ininincomment");
					rsql="from comment ,productorder ,product where comment.comid=productorder.comid and productorder.proid=product.proid and product.retid = '"
				          +Welcome.id+"'";
				}
				else if(t.contentEquals("product")) {
					rsql="from product where retid = '"+Welcome.id+"'";
				}
				else if(t.contentEquals("productorder")) {
					rsql="from productorder ,product where productorder.proid=product.proid and product.retid= '"
							+Welcome.id+"'";
				}
			}
			else rsql="from "+t;
			System.out.println(rsql+"uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu");
			return rsql;
		}
		
		public static String judgevalue(String i,String s) {
			if(i=="age"||i=="account"||i=="redit"||i=="delete"||i=="price"||i=="num") {
				return s;
			}
			else{
				return "'"+s+"'";
			}
		}
		
		public static String getid(String table) {
			String[] id=null;
			String sql;
			if(Welcome.position=="Retailer") {
				if(table.contentEquals("comment")) {
					sql="select comid"+retailerSQL("comment");
				}
				else if(table.contentEquals("product")) {
					sql="select proid"+retailerSQL("prodcuct");
				}
				else {
					sql="select orid"+retailerSQL("productorder");
				}
			}
			else sql="";
			return sql;
		}
		
		public static JComboBox addid(String id) {
			JComboBox b=new JComboBox();
			String sql;
			List l;
			if(id.contentEquals("comID")) {
				sql="select comID from comment ;";
			}
			else if(id.contentEquals("proID")) {
				sql="select * from product;";
			}
			else if(id.contentEquals("cusID")) {
				sql="select cusID from customer;";
			}
			else if(id.contentEquals("retID")) {
				sql="select retID from retailer;";
			}
			
			else sql=null;
			
			l=queryGetList(sql);
			for(int i=1;i<l.size();i++) {
				String[] temp= (String[]) l.get(i);
				b.addItem(temp[0]);
			}
			return b;
		}
		
		public static String[] getFieldNameArray(String tableName){
			String sql = "select * from " + tableName;
			List list = DBOperation.queryGetList(sql);
			return (String[]) list.get(0);
		}

}
