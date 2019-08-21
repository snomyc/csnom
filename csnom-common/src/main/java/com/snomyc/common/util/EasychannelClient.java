package com.snomyc.common.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class EasychannelClient extends JFrame implements ActionListener,FocusListener,Runnable {

	private static final long serialVersionUID = 1L;
	private static EasychannelClient easyClient;
	private JTextField jTextField;   //文本框
	private JTextField jTextField2;
	private JTextField fromFileText;  //需要复制的文件路径
	private JTextField toFileText;    //目标文件路径
	private JButton jButton;   //自动启动按钮
	private JButton jButton1;   //手动启动按钮
	private JButton jButton2;   //清除面板按钮
	private JButton jButton3;   //导入当前销售数据按钮
	private JButton jButton4;   //导入历史销售数据按钮
	
	private JTextArea jTextArea; //文本域
	private JScrollPane scrollPane;// 创建滚动面板
	private Calendar currentTime; //当前时间
	private JButton jfrom;  //点击打开选择最新war目录
	private JButton jto;    //点击打开webapps目录
	private JFileChooser jFC;
	private EasychannelClient(){
		super();
		String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
		try {
			UIManager.setLookAndFeel(lookAndFeel);
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		JLabel jLabel = new JLabel();
		jLabel.setBounds(35,30,200,20);
		jLabel.setText("数据服务自启动更新时间为:");
		
		JLabel jLabel2 = new JLabel();
		jLabel2.setBounds(205,30,225,20);
		jLabel2.setText("每天");
		
		jTextField = new JTextField();
		jTextField.setBounds(235,30,0,0);
		jTextField.setSize(20, 20);
		jTextField.setText("5");
		
		JLabel jLabel3 = new JLabel();
		jLabel3.setBounds(260,30,275,20);
		jLabel3.setText("时");
		
		jTextField2 = new JTextField();
		jTextField2.setBounds(275,30,0,0);
		jTextField2.setSize(20, 20);
		jTextField2.setText("00");
		
		JLabel jLabel4 = new JLabel();
		jLabel4.setBounds(295,30,300,20);
		jLabel4.setText("分");
		
		JLabel j5 = new JLabel();
		j5.setBounds(65,55,200,20);
		j5.setText("最新系统War文件路径:");
		
		fromFileText = new JTextField();
		fromFileText.setBounds(205,55,0,0);
		fromFileText.setSize(235, 20);
		fromFileText.setEditable(false);
		fromFileText.setText("请输入最新系统war文件路径...");
		
		jfrom = new JButton();
		jfrom.setBounds(445,55,80,20); //左上右下
		jfrom.setText("选择目录");
		
		JLabel j6 = new JLabel();
		j6.setBounds(47,80,200,20);
		j6.setText("Tomcat服务器根目录路径:");
		
		toFileText = new JTextField();
		toFileText.setBounds(205,80,0,0);
		toFileText.setSize(235, 20);
		toFileText.setEditable(false);
		toFileText.setText("请输入Tomcat服务器根目录路径...");
		
		jto = new JButton();
		jto.setBounds(445,80,80,20); //左上右下
		jto.setText("选择目录");
		
		jButton = new JButton();
		jButton.setBounds(445,160,110,28);
		jButton.setText("自动执行");
		jButton.setEnabled(false);
		
		jButton1 = new JButton();
		jButton1.setBounds(445,195,110,28);
		jButton1.setText("手动执行");
		
		jButton3 = new JButton();
		jButton3.setBounds(445,230,110,28);
		jButton3.setText("导入当前销售");
		
		jButton4 = new JButton();
		jButton4.setBounds(445,265,110,28);
		jButton4.setText("导入历史销售");
		
		jButton2 = new JButton();
		jButton2.setBounds(445,300,110,28); //左上右下
		jButton2.setText("清除面板");
		
		jTextArea = new JTextArea();
		jTextArea.setEditable(false);  
		jTextArea.setLineWrap(true);  
		jTextArea.setWrapStyleWord(true);  
		//jTextArea.setBounds(0,140,300,400);
		//jTextArea.setEditable(false);
		
		scrollPane= new JScrollPane(jTextArea);
		scrollPane.setBounds(2,135,420,235);
		scrollPane.setViewportView(jTextArea);
		
		this.add(jLabel);
		this.add(jLabel2);
		this.add(jLabel3);
		this.add(jLabel4);
		this.add(j5);
		this.add(j6);
		this.add(fromFileText);
		this.add(toFileText);
		this.add(jTextField);
		this.add(jTextField2);
		this.add(jButton);
		this.add(jButton1);
		this.add(jButton2);
		this.add(jButton3);
		this.add(jButton4);
		this.add(jfrom);
		this.add(jto);
		//this.add(jTextArea);
		this.add(scrollPane);
		
		//监听手动执行事件
		jButton1.addActionListener(this);
		//监听清除面板事件
		jButton2.addActionListener(this);
		//监听导入当前销售数据事件
		jButton3.addActionListener(this);
		//监听导入历史销售数据事件
		jButton4.addActionListener(this);
		//选择文件
		jfrom.addActionListener(this);
		//选择目录
		jto.addActionListener(this);
		
		this.getContentPane().setLayout(null);
		this.setSize(580, 400);
		this.setTitle("数据管理系统自动化");
		//this.pack(); //智能调整大小
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false); //禁止拉边框拉长拉断即不能自动最大化
		this.setLocationRelativeTo(null); //窗口居中显示
		this.setVisible(true); //显示窗口面板
		easyClient = this;
	}
	
	public static synchronized EasychannelClient getInstance() {
		if(easyClient == null) {
			easyClient = new EasychannelClient();
		}
		return easyClient;
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jButton1) {
			new Thread(new Runnable() {
				public void run() {
					SwingUtilities.invokeLater(new Runnable() {
			            public void run() {
			            	//按钮置灰
			    			jButton1.setEnabled(false);
			    			jButton1.setText("正在执行");
			            }
			        });
					jTextArea.append(getTimeText("正在手动执行自动化..."));
					jTextArea.setCaretPosition(jTextArea.getText().length());
					//手动执行
					hmInit();
					SwingUtilities.invokeLater(new Runnable() {
			            public void run() {
			            	//恢复按钮状态
			    			jButton1.setEnabled(true);
			    			jButton1.setText("手动执行");
			            }
			        });
				}
			}).start();

		}else if(e.getSource() == jButton2) {
			jTextArea.setText("");
			jTextArea.setCaretPosition(jTextArea.getText().length());
		}else if(e.getSource() == jButton3) {
			//导入当前销售数据
			//选择最新当前销售数据文件地址
			jFC = new JFileChooser();
			jFC.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jFC.showDialog(new JFrame(), "选择当前销售excel文件");
			if(jFC.getSelectedFile() != null ) {
				
				new Thread(new Runnable() {
					public void run() {
						//刷新页面组件
						SwingUtilities.invokeLater(new Runnable() {
				            public void run() {
				            	jButton3.setEnabled(false);
								jButton3.setText("正在导入...");
				            }
				        });
						//执行耗时任务
						String easyExcel = jFC.getSelectedFile().getAbsolutePath();
						importEasychannel(easyExcel);
						//刷新页面组件
						SwingUtilities.invokeLater(new Runnable() {
				            public void run() {
				            	jButton3.setText("导入当前销售");
				        		jButton3.setEnabled(true);
				            }
				        });
					}
				}).start();
			}
		}else if(e.getSource() == jButton4) {
			//导入历史销售数据
			jFC = new JFileChooser();
			jFC.setFileSelectionMode(JFileChooser.FILES_ONLY);
			jFC.showDialog(new JFrame(), "选择历史销售excel文件");
			if(jFC.getSelectedFile() != null ) {
				
				new Thread(new Runnable() {
					public void run() {
						//刷新页面组件
						SwingUtilities.invokeLater(new Runnable() {
				            public void run() {
				            	jButton4.setEnabled(false);
				        		jButton4.setText("正在导入...");
				            }
				        });
						//执行耗时任务
						String hisExcel = jFC.getSelectedFile().getAbsolutePath();
						importHisEasychannel(hisExcel);
						//刷新页面组件
						SwingUtilities.invokeLater(new Runnable() {
				            public void run() {
				            	jButton4.setText("导入历史销售");
				        		jButton4.setEnabled(true);
				            }
				        });
					}
				}).start();
			}
		}else if(e.getSource() == jfrom) {
			//选择最新war文件目录
			jFC = new JFileChooser();
			jFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			jFC.showDialog(new JFrame(), "选择");
			if(jFC.getSelectedFile() != null ) {
				String from = jFC.getSelectedFile().getAbsolutePath();
				fromFileText.setText(from);
			}
		}else if(e.getSource() == jto ) {
			//选择webapps目录
			jFC = new JFileChooser();
			jFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			jFC.showDialog(new JFrame(), "选择");
			if(jFC.getSelectedFile() != null ) {
				String to = jFC.getSelectedFile().getAbsolutePath();
				toFileText.setText(to);
			}
		}
	}
	
	//格式化文本
	public String getTimeText(String text) {
		text = "["+DateTool.getStandardTime()+"] "+text+"\n";
		return text;
	}
	
	/**
	 *格式化文件路径比如c:\123\12 --c:/123/12
	 ***/
	public String getFileStr(String fileString) {
		fileString = fileString.replace("\\", "/");
		return fileString;
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
//		 if(e.getSource() == jTextField) {
//			jTextField.setText("获得焦点!");
//			jTextArea.append("文本1获得焦点!\n");
//			jTextArea.setCaretPosition(jTextArea.getText().length());
//		 }
	}

	@Override
	public void focusLost(FocusEvent e) {
		// TODO Auto-generated method stub
//		 if(e.getSource() == jTextField) {
//			jTextField.setText("失去焦点!");
//			jTextArea.append("文本1失去焦点!\n");
//			jTextArea.setCaretPosition(jTextArea.getText().length());
//		 }
	}

	@Override
	public void run() {
		jTextArea.append(getTimeText("系统初始化中..."));
		jTextArea.append(getTimeText("系统开始执行自动化服务..."));
		jTextArea.append(getTimeText("系统扫描时间线程开启..."));
		jTextArea.setCaretPosition(jTextArea.getText().length());
		currentTime = Calendar.getInstance();
		for (;;) {
			currentTime.setTimeInMillis(System.currentTimeMillis());
			try {
				//每隔60秒扫描一次
				this.init();
				Thread.sleep(1000*60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	//初始化的时候开始自动化启动更新服务
	public void init() {
		//获得当前时间
		//jTextArea.append(getTimeText("现在的时间为"+currentTime.get(currentTime.HOUR_OF_DAY)+"点"+currentTime.get(currentTime.MINUTE)+"分"));
		//jTextArea.setCaretPosition(jTextArea.getText().length());
		int curHour = currentTime.get(currentTime.HOUR_OF_DAY);
		int curMinute = currentTime.get(currentTime.MINUTE);
		//对比当前时间(时-分)和自动化设置的时分,如果一致则执行自动化服务
		int hour = 0; 
		int minute = 0;
		if(jTextField.getText() != null && !jTextField.getText().equals("")) {
			try{
				hour = Integer.valueOf(jTextField.getText());
			}catch (Exception e) {
				jTextArea.append(getTimeText("时钟只能是数字,请输入数字(0-23)..."));
				jTextArea.setCaretPosition(jTextArea.getText().length());
			}
			
		}
		if(jTextField2.getText() != null && !jTextField2.getText().equals("")) {
			try{
				minute = Integer.valueOf(jTextField2.getText());
			}catch (Exception e) {
				jTextArea.append(getTimeText("分钟只能是数字,请输入数字(00-59)..."));
				jTextArea.setCaretPosition(jTextArea.getText().length());
			}
			
		}
		if(hour == curHour && minute == curMinute) {
			jTextArea.append(getTimeText("检测当前时间与自动化时间一致,开始执行服务..."));
			jTextArea.setCaretPosition(jTextArea.getText().length());
			//调用手动执行方法
			this.hmInit();
			
		}
	}
	
	
	/***
	 * 手动执行
	 * */
	public void hmInit() {
		//获得最新war文件路径
		String from = fromFileText.getText()+"\\easychannel.war";
		File fromFile = new File(from);
		
		//运行sql脚本文件
		//获得最新sql脚本文件路径
		String sqlFrom = fromFileText.getText()+"\\easychannel.sql";
		File sqlFile = new File(sqlFrom);
		if(sqlFile.exists()) {
			
			jTextArea.append(getTimeText("正在执行sql脚本,请等待..."));
			jTextArea.setCaretPosition(jTextArea.getText().length());
			
			String sqlcmd = "sqlcmd -Usa -P123456 -i "+ sqlFrom;
			try {
				Runtime.getRuntime().exec(sqlcmd);
				Thread.sleep(10000);
				//执行成功,删除脚本文件
				boolean flag = sqlFile.delete();
				if(!flag) {
					jTextArea.append(getTimeText(sqlFrom+"脚本文件删除失败,请手动删除..."));
					jTextArea.setCaretPosition(jTextArea.getText().length());
				}
				jTextArea.append(getTimeText(sqlFrom+"脚本文件执行成功!"));
				jTextArea.setCaretPosition(jTextArea.getText().length());
			} catch (Exception e) {
				jTextArea.append(getTimeText(sqlFrom+"脚本文件执行失败,请手动执行..."));
				jTextArea.setCaretPosition(jTextArea.getText().length());
			}
		}else {
			jTextArea.append(getTimeText("没有发现该sql脚本文件"+from+",无sql脚本更新!"));
			jTextArea.setCaretPosition(jTextArea.getText().length());
		}
		
		
		if(fromFile.exists()) {
			//如果最新war文件存在
			//获得Tomcat存放项目文件路径(webapps文件夹下)
			String tomcat = toFileText.getText();
			String webapps = tomcat+"\\webapps";
			if(tomcat.endsWith("Tomcat 7.0")) {
				File toFile = new File(webapps);
				if(toFile.exists()) {
					//关闭服务Tomcat7.exe
					jTextArea.append(getTimeText("开始关闭数据服务系统,请等待..."));
					jTextArea.setCaretPosition(jTextArea.getText().length());
					//结束进程
					try {
						Runtime.getRuntime().exec("taskkill -f -im Tomcat7.exe");
						Thread.sleep(3000);
					} catch (Exception e) {
						jTextArea.append(getTimeText("关闭Tomcat.exe失败..."));
						jTextArea.setCaretPosition(jTextArea.getText().length());
						return;
					}
					jTextArea.append(getTimeText("关闭success..."));
					jTextArea.setCaretPosition(jTextArea.getText().length());
					//删除webapps下的文件及文件夹
					boolean flag = FileUtils.deleteFiles(webapps);
					if(flag) {
						jTextArea.append(getTimeText("删除webapps下的文件success..."));
						jTextArea.setCaretPosition(jTextArea.getText().length());
						//剪切最新war到webapps下
						flag = FileUtils.CutFile(from, webapps);
						if(flag) {
							jTextArea.append(getTimeText("剪切最新war到webapps下success,系统更新成功..."));
							jTextArea.setCaretPosition(jTextArea.getText().length());
							jTextArea.append(getTimeText("开启启动数据服务系统,请等待..."));
							jTextArea.setCaretPosition(jTextArea.getText().length());
							//启动服务Tomcat7.exe
							try {
								java.awt.Desktop.getDesktop().open(new File(tomcat+"\\bin\\Tomcat7.exe"));
								//Runtime.getRuntime().exec(tomcat+"\\bin\\Tomcat7.exe");
								Thread.sleep(3000);
							} catch (Exception e) {
								jTextArea.append(getTimeText("启动失败,请手动启动Tomcat7.exe..."));
								jTextArea.setCaretPosition(jTextArea.getText().length());
								return;
							}
							jTextArea.append(getTimeText("CMD窗口启动success,请不要关闭CMD窗口..."));
							jTextArea.setCaretPosition(jTextArea.getText().length());
							//自动化执行success
							jTextArea.append(getTimeText("自动化启动服务完成,预计1分钟后,请登录网站验证新系统功能..."));
							jTextArea.setCaretPosition(jTextArea.getText().length());
						}else {
							jTextArea.append(getTimeText("紧急!!!从"+from+"剪切最新War包失败,请尽快手动发布war包并启动Tomcat服务..."));
							jTextArea.setCaretPosition(jTextArea.getText().length());
						}
						
					}else {
						jTextArea.append(getTimeText("删除"+webapps+"下的文件失败..."));
						jTextArea.setCaretPosition(jTextArea.getText().length());
					}
					
				}else {
					jTextArea.append(getTimeText("Tomcat下未发现webapps文件夹..."));
					jTextArea.setCaretPosition(jTextArea.getText().length());
				}
			}else {
				jTextArea.append(getTimeText(tomcat+"必须是Tomcat 7.0文件夹下"));
				jTextArea.setCaretPosition(jTextArea.getText().length());
			}
			
		}else {
			jTextArea.append(getTimeText("没有发现该war包文件"+from+",无新war更新!"));
			jTextArea.setCaretPosition(jTextArea.getText().length());
		}
		
	}
	
	/***
	 * 导入当前销售数据方法
	 * */
	public void importEasychannel(String easyExcel) {
		jTextArea.append(getTimeText("正在导入当前销售数据"+easyExcel+"文件"));
		jTextArea.setCaretPosition(jTextArea.getText().length());
		String sql = "insert into easychannel select * from OPENROWSET('Microsoft.ACE.OLEDB.12.0' ,'Excel 8.0;HDR=YES;DATABASE="+easyExcel+"',数据总表$)";
		//int row = C3P0Util.UpDate(sql, null);
		int row = 0;
		if(row>0) {
			jTextArea.append(getTimeText("当前销售数据导入成功,请登录网站查看"));
			jTextArea.setCaretPosition(jTextArea.getText().length());
		}else {
			jTextArea.append(getTimeText("导入失败,请检查excel文件,工作簿名称,excel后缀名是否匹配正确 ,ID主键是否唯一!"));
			jTextArea.setCaretPosition(jTextArea.getText().length());
		}
	}
	
	/***
	 * 导入历史销售数据方法
	 * */
	public void importHisEasychannel(String hisExcel) {
		jTextArea.append(getTimeText("正在导入历史销售数据"+hisExcel+"文件"));
		jTextArea.setCaretPosition(jTextArea.getText().length());
		
		String sql = "insert into hiseasychannel select * from OPENROWSET('Microsoft.ACE.OLEDB.12.0' ,'Excel 8.0;HDR=YES;DATABASE="+hisExcel+"',数据总表$)";
		//int row = C3P0Util.UpDate(sql, null);
		int row = 0;
		if(row>0) {
			jTextArea.append(getTimeText("历史销售数据导入成功,请登录网站查看"));
			jTextArea.setCaretPosition(jTextArea.getText().length());
		}else {
			jTextArea.append(getTimeText("导入失败,请检查excel文件,工作簿名称,excel后缀名是否匹配正确 ,ID主键是否唯一!"));
			jTextArea.setCaretPosition(jTextArea.getText().length());
		}
	}
	
	public static void main(String[] args) {
		EasychannelClient e = new EasychannelClient();
		//启动一个线程每隔60秒返回当前时间
		Thread t = new Thread(e);
		t.start();
	}
	
}
