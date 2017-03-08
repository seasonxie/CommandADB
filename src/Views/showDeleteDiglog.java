package Views;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.json.JSONException;

import Utils.FileUtil;

public class showDeleteDiglog  extends JDialog implements ActionListener {
 

    // 删除
    JComboBox comboBox;
    // 对话框的父窗体。
    Main parent;
    // “确定”按钮
    JButton setButton;
 
    /**
     * 构造函数，参数为父窗体和对话框的标题
     * @throws JSONException 
     */
    showDeleteDiglog(JFrame prentFrame, String title) throws JSONException {
        // 调用父类的构造函数，
        // 第三个参数用false表示允许激活其他窗体。为true表示不能够激活其他窗体
        super(prentFrame, title, false);
        parent = (Main) prentFrame;
 
      System.out.println("setup");
        
        // 添加Label和输入文本框
        JPanel p2 = new JPanel();
        JLabel label2 = new JLabel("选择按钮:");
        p2.add(label2);
        // 添加Label和输入文本框
        comboBox=new JComboBox();
        String jsonButton=FileUtil.readTxtFile(Constants.JSONPATH);     
		Object[] datas = FileUtil.getJsonArrayValues(jsonButton, "buttons");
		int num = datas.length;
		for (int i = 0; i < num; i++) {
			 comboBox.addItem(FileUtil.getValue(datas[i].toString(), "name"));				
		}	

        p2.add(comboBox);
        getContentPane().add("Center", p2);
 
        // 添加确定和取消按钮
        JPanel p3 = new JPanel();
        p3.setLayout(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelButton = new JButton("取 消");
        cancelButton.addActionListener(this);
        setButton = new JButton("删除");
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
        if ((source == setButton)) {
         System.out.println(comboBox.getSelectedItem().toString().trim());
         
        
        		Main.removeButton(comboBox.getSelectedItem().toString().trim());
        		try {
					FileUtil.deleteJson(comboBox.getSelectedItem().toString().trim());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
        }
        // 隐藏对话框
        //setVisible(false);
        Main.detedialog.dispose();
        Main.detedialog=null;
    }
}



