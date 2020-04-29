package coursework;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class UsefulQueryTab {
	private JButton queryButton;
	private JComboBox queryList;
	private String[] sampleSQL;
	private String[] sampleQueryDescription=new String[10];
	private static JTextField from;
	private static JTextField to;
	private static JTextField tp;
	private JRadioButton builtin;
	private JLabel datefrom;
	private JLabel dateto;
	private JLabel type;
	static String f;
	static String t;
	static String t1;
	
	public UsefulQueryTab(SelectionPanel sp, JPanel rp){
		initSampleSQL();
		queryList = new JComboBox(sampleQueryDescription);

		from = new JTextField(20);
		to = new JTextField(20);
		tp = new JTextField(10);
		
		builtin = new JRadioButton("Built-in Query: ");
		datefrom = new JLabel("data from: ");
		dateto=new JLabel("to: ");
		type=new JLabel("type: ");
		ButtonGroup bg = new ButtonGroup();
		bg.add(builtin);
		//bg.add(customize);
		
		sp.removeAll();
		sp.setLayout(new GridLayout(0,1,2,2));
		JPanel temp1 = new JPanel();
		temp1.add(builtin);
		temp1.add(queryList);
		JPanel temp2 = new JPanel();
		temp2.add(datefrom);
		temp2.add(from);
		temp2.add(dateto);
		temp2.add(to);
		temp2.add(type);
		temp2.add(tp);
		sp.add(temp1);
		sp.add(temp2);
		queryButton = new JButton("Query !");
		queryButton.addActionListener(new ActionListener(){

			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				initSQL();
				String sql = null;
				if(builtin.isSelected()){
					int index = queryList.getSelectedIndex();
					if(index > 0){
						sql = sampleSQL[index-1];
						System.out.println(sql+"ssssssssssssssssssssssssssssssssssssssssssssssssssssssssssss");
					}
				}
				//else if(customize.isSelected()){
				//	sql = customizeQuery.getText();
			//	}
				SelectionPanel.showResult(sql, rp);
			}
			
		});
		queryList.setEditable(true);
		JPanel temp3 = new JPanel();
		temp3.add(queryButton);
		sp.add(temp3);
		sp.updateUI();
		sp.repaint();
	}
	
	public void initSampleSQL(){
		//sampleSQL = new String[10];
		//sampleQueryDescription = new String[10];
		if(Welcome.position=="Retailer") {
			sampleQueryDescription = new String[5];
		}
		else {
			sampleQueryDescription = new String[11];
			sampleQueryDescription[5]="5)按照商家统计某段时间的订单数和总金额";
			sampleQueryDescription[6]="6)某个时间段内，销量排名前十名的商家";
			sampleQueryDescription[7]="7)某个时间段内，成功下单金额前十名的用户";
			sampleQueryDescription[8]="8)每个月的订单数量和金额情况统计";
			sampleQueryDescription[9]="9)24小时的订单数量分布情况统计";
			sampleQueryDescription[10]="10)按照地区的销量进行统计";
		}
		
		sampleQueryDescription[0]="";
		sampleQueryDescription[1]="1)某个时间段所有订单的单数和总金额";
		sampleQueryDescription[2]="2)查询某个类别商品的订单数和总金额";
		sampleQueryDescription[3]="3)某个时间段内，前十名热销的商品";
		sampleQueryDescription[4]="4)评价数量（热销程度）和评分（商品好评率）前十名的商品";
		
		
	}
	
	public void initSQL() {
		if(Welcome.position=="Retailer") {
			sampleSQL = new String[4];
			sampleQueryDescription = new String[4];
			sampleSQL[0]="select count(oriD) as Countnumber, Sum(o.price) as sumprice from retailer r, productorder o,product p where p.retID = r.retID and p.proid=o.proid and o.date >=\""+from.getText()+"\" and o.date<= \""+to.getText()+"\" and r.retid=\""+Welcome.id+"\";";
			sampleSQL[1]="select count(orid)as ordernumber,sum(o.price) as price from productorder o,product p,retailer r where o.proID=p.proID and p.retID = r.retID and (p.ltype like \"%"+tp.getText()+"%\" or p.mtype like \"%"+tp.getText()+"%\" or p.stype like \"%"+tp.getText()+"%\") and r.retid=\""+Welcome.id+"\";";
			sampleSQL[2]="select p.proid,p.name,sum(o.num)as number from productorder o,product p,retailer r where p.proid=o.proid and p.retID = r.retID and r.retid=\""+Welcome.id+"\" and o.date>=\""+from.getText()+"\" and o.date<=\""+to.getText()+"\" group by proid order by number desc limit 10;";
			sampleSQL[3]="select p.proID, p.name, count(c.comid) as Countnumber , avg(score) as score from comment c, productorder o,product p,retailer r where c.comID = o.comID and o.proID = p.proID and p.retID = r.retID and r.retid=\""+Welcome.id+"\" group by proID order by Countnumber desc, score desc limit 10;";
		}
		else {
			sampleSQL = new String[10];
			sampleQueryDescription = new String[11];
			
			sampleSQL[0]="select count(orid) as ordernumber ,sum(price) as price from productorder p where p.date >=\""+from.getText()+"\" and p.date<=\""+to.getText()+"\";";
		    sampleSQL[1]="select count(orid)as ordernumber,sum(o.price) as price from productorder o,product p where o.proID=p.proID and (p.ltype like \"%"+tp.getText()+"%\" or p.mtype like \"%"+tp.getText()+"%\"  or p.stype like \"%"+tp.getText()+"%\");";
		    sampleSQL[2]="select p.proid,p.name,sum(o.num)as number from productorder o,product p where p.proid=o.proid and o.date>=\""+from.getText()+"\" and o.date<=\""+to.getText()+"\" group by proid order by number desc limit 10;";
		    sampleSQL[3]="select p.proID, p.name, count(c.comid) as Countnumber , avg(score) as score from comment c, productorder o,product p  where c.comID = o.comID and o.proID = p.proID  group by proID order by Countnumber desc, score desc limit 10;";
			// given instructor can be edited at the where clause
			sampleSQL[4]="select r.retid ,count(orid) as Countid, Sum(o.price) as Sumprice from productorder o, product p, retailer r where p.proID = o.proID and p.retID = r.retID and o.date >=\""+from.getText()+"\" and o.date<= \""+to.getText()+"\" group by retid order by retid;";
			sampleSQL[5]="select r.retid, r.storename,sum(o.num) as sumsale from productorder o, product p,retailer r where o.proID = p.proID and r.retID=p.retID and o.date >=\""+from.getText()+"\" and o.date<= \""+to.getText()+"\" group by r.retID order by sumsale desc limit 10;";
			sampleSQL[6]="select c.cusID,c.name, sum(o.price) as Sumprice from customer c, productorder o, product p where c.cusID = o.cusID and o.proID = p.proID and o.date >=\""+from.getText()+"\" and o.date<= \""+to.getText()+"\" group by c.cusID order by Sumprice desc limit 10;";
			sampleSQL[7]="select year(o.date)as y,month(o.date) as m, count(oriD) as countid, sum(o.price)as totalprice from productorder o, product p ,customer c where o.cusID = c.cusID and p.proid=o.proid group by m order by y,m;";
			sampleSQL[8]="select sum(o.price)as total,count(oriD)as totalorder from productorder o where o.date >=\""+from.getText()+"\" and o.date<= \""+to.getText()+"\";";
			sampleSQL[9]="select c.location,sum(o.price)as price from productorder o,customer c where c.cusID=o.cusID group by location order by price desc;";
		}
		
	}

}
