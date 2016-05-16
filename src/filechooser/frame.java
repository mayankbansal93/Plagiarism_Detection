package filechooser;
	
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import org.json.JSONArray;

public class frame extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JSplitPane sp;
	Container ct;
	JPanel p1, p2;
	JLabel l1;
	JButton b1;
	JProgressBar jp;
	javax.swing.Timer tm1,tm2;
	int l=0;
	HashMap<String, String> hm;
	JCheckBox checkbox;
	JTextField textfield;
	MyThread t1;
	ArrayList<String> result_list;
	
	frame(String title) {
		
		super(title);
		
		hm=new HashMap<String, String>();
		ct = getContentPane();
		
		p2 = new panel2(hm);
		p1 = new panel1(p2,hm);
		
		GridBagLayout layout=new GridBagLayout();
		setLayout(layout);
		GridBagConstraints gbc=new GridBagConstraints();	
		
		l1 = new JLabel("PLAGIARISM DETECTION");
		l1.setFont(new Font("Times New Roman", Font.BOLD, 60));
		l1.setForeground(Color.RED);
		l1.setHorizontalAlignment(0);
		
		sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, p1, p2);
		sp.setOneTouchExpandable(true);
		sp.setDividerLocation(620);
		
		tm1=new javax.swing.Timer(100,new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(l<=95){
					jp.setString("Comparing...");
					jp.setValue(jp.getValue()+1);
				}else{
					tm1.stop();
				}
				l++;
			}
		});
		
		
		final frame f = this;
		
		
		b1=new JButton("PROCEED");
		b1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(hm.size()>=2 || (checkbox.isSelected() && hm.size()==1)){
					
					tm1.start();
					
					if(checkbox.isSelected()){						
						String text = "http://localhost:3000/?query="+textfield.getText().toString();
						text = text.replaceAll(" ","%20");
						String json = callURL(text);
						result_list = new ArrayList<>();
						try{
							JSONArray array = new JSONArray(json);
							for(int i=0;i<array.length();i++){
								result_list.add(array.getString(i));
							}
							
						} catch(Exception e){
							System.out.println("error " + e);
						}
						
						
//						System.out.println(json);
						t1 = new MyThread(hm, f,true,result_list);
					}else{
						t1 = new MyThread(hm, f,false,result_list);
					}
					t1.start();
				
				}else{
					
					JOptionPane.showMessageDialog(f, "Select atleast two files to proceed further");
				}
			}
		});
		
		jp=new JProgressBar(0,1,100);
		jp.setStringPainted(true);
		jp.setBackground(Color.WHITE);
		jp.setForeground(Color.BLUE);
		
		FlowLayout fl=new FlowLayout(0, 20, 0);
		
		JPanel p3=new JPanel();
		p3.add(b1,FlowLayout.LEFT);
		p3.add(jp,FlowLayout.CENTER);
		p3.setLayout(fl);
		
		textfield = new JTextField(20);
		textfield.disable();
		
		checkbox = new JCheckBox("WIKI");
		checkbox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if(!checkbox.isSelected()){
					textfield.setText("");
					textfield.disable();
				}else{
					textfield.enable();
				}
			}
		});
			
		JPanel p4 = new JPanel();
		p4.add(checkbox,FlowLayout.LEFT);
		p4.add(textfield,FlowLayout.CENTER);
		p4.setLayout(fl);
		
		gbc.fill=GridBagConstraints.HORIZONTAL;
		gbc.gridy=0;
		ct.add(l1,gbc);
		
		gbc.fill=GridBagConstraints.HORIZONTAL;
		gbc.gridy=1;
		gbc.insets=new Insets(20, 20, 20, 20);
		gbc.weightx=2000;
		ct.add(sp,gbc);
		
		gbc.fill = GridBagConstraints.CENTER;
		gbc.gridy = 2;
		gbc.gridx = 0;
		ct.add(p4,gbc);
		
		gbc.fill=GridBagConstraints.CENTER;
		gbc.gridy=3;
		gbc.gridx=0;
		ct.add(p3,gbc);
	}
	
	public static String callURL(String myURL) {
		System.out.println("Requeted URL:" + myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
		in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ myURL, e);
		} 
 
		return sb.toString();
	}
}			
