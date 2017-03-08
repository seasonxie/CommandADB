package Views;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.JSONException;

import Utils.FileUtil;
/*
 * 自定义按钮弹出框
 */
public class DialogShow extends JDialog implements ActionListener {
 
    // 文本框
    JTextField name;
    // 文本框
    JTextField name1;
    JComboBox comboBox;
    // 文本框
    JTextArea command;
    // 对话框的父窗体。
    Main parent;
    // “确定”按钮
    JButton setButton;
 
    /**
     * 构造函数，参数为父窗体和对话框的标题
     */
    DialogShow(JFrame prentFrame, String title) {
        // 调用父类的构造函数，
        // 第三个参数用false表示允许激活其他窗体。为true表示不能够激活其他窗体
        super(prentFrame, title, false);
        parent = (Main) prentFrame;
        
       
 
        // 添加Label和输入文本框
        JPanel p1 = new JPanel();
        p1.setLayout( new BoxLayout(
				p1, BoxLayout.X_AXIS));
        JLabel label = new JLabel("按钮名字:");
        p1.add(label);
        name = new JTextField(14);
        name.addActionListener(this);
        p1.add(name);
       
       
       
        // 添加Label和输入文本框
        comboBox=new JComboBox();
        comboBox.addItem("android命令");
        comboBox.addItem("window命令");
        comboBox.addItem("linux命令");
       
       
        JLabel label1 = new JLabel("执行平台:");
        JLabel label11 = new JLabel("linuxIP:");
        name1 = new JTextField(8);
        name1.addActionListener(this);
        p1.add(label1);
        p1.add(comboBox);
       
        p1.add(label11);
        p1.add(name1);
        getContentPane().add("North", p1);
        
        // 添加Label和输入文本框
        JPanel p2 = new JPanel();
        p2.setLayout( new BoxLayout(
				p2, BoxLayout.X_AXIS));
        JLabel label2 = new JLabel("执行命令:");
        p2.add(label2);
        command = new JTextArea(5,12);
        JScrollPane sp = new JScrollPane(command);
        p2.add(sp);
 
        getContentPane().add("Center", p2);
        
       
 
        // 添加确定和取消按钮
        JPanel p3 = new JPanel();
        p3.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("取 消");
        cancelButton.addActionListener(this);
        setButton = new JButton("确 定");
        setButton.addActionListener(this);
        p3.add(setButton);
        p3.add(cancelButton);
        getContentPane().add("South", p3);
 
        // 调整对话框布局大小
        pack();
    }
 
    /**
     * 事件处理
     */
    public void actionPerformed(ActionEvent event) {
 
        Object source = event.getSource();
        String linuxIP="";
        String type="";
        if ((source == setButton)) {    
        	System.out.println(comboBox.getSelectedItem().toString());
        	if(comboBox.getSelectedIndex()==Constants.LINEXCODE){
        		if(name1.getText().isEmpty()){
        			JOptionPane.showMessageDialog(null, "填写服务器ip");
        			return;
        		}else{
        			linuxIP=name1.getText().trim();	
        			type="linux";
        		}
        	}else if(comboBox.getSelectedIndex()==Constants.WINDOWCODE){
        		type="window";
        	}else{
        		type="android";
        	}
            // 如果确定按钮被按下，则将文本矿的文本添加到父窗体的文本域中
        	String buttonName=name.getText();
        	String commandText=command.getText();
        	if(name.getText().isEmpty()||command.getText().isEmpty()){
    			JOptionPane.showMessageDialog(null, "按钮名和命令都不能为空，请检查...");
    			return;
    		}
        	
        	
        	try {
        	if(linuxIP.isEmpty()){
        		parent.addButton(buttonName,commandText,type,parent.customOpertion);
        		FileUtil.addJson(buttonName, commandText,type);
        	}else{
        		parent.addButton(buttonName+"("+linuxIP+")",commandText,type,parent.customOpertion);
        		FileUtil.addJson(buttonName+"("+linuxIP+")", commandText,type+"_"+linuxIP);
        	}				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        // 隐藏对话框
        setVisible(false);
    }
}



