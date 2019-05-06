package com.client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.client.vo.Request_Command;


public class Login {

	private JFrame jFrame = null; 
	private JPanel jContentPane = null;
	private JLabel jLabelLoginUserName = null;
	private JLabel jLabelLoginPassword = null;
	private JTextField jTextFieldLoginUserName = null;
	private JPasswordField jTextFieldLoginPassword = null;
	public JButton jButtonLogin = null;
	public JButton jButtonReg = null;
	private LinkInfo linkInfo;
	private InputStream in;  
	private OutputStream out;  
	private GetServerInfo gif;
	public Login(LinkInfo linkInfo){
		this.linkInfo = linkInfo;
		this.getJFrame().setVisible(true);
	}
	/**
	 * This method initializes jFrame	
	 * 	
	 * @return javax.swing.JFrame	
	 */
	public void closeFrame(){
		this.jFrame.dispose();
		this.linkInfo.setLogin(null);
	}
	
	private JFrame getJFrame() {
		if (jFrame == null) {
			jFrame = new JFrame();
			jFrame.setSize(new Dimension(389, 274));
			jFrame.setTitle("��½");
			jFrame.setResizable(false);
			jFrame.setContentPane(getJContentPane());
			jFrame.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					System.exit(0);
				}
			});
		}
		return jFrame;
	}

	/**
	 * This method initializes jContentPane	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabelLoginPassword = new JLabel();
			jLabelLoginPassword.setBounds(new Rectangle(18, 107, 335, 38));
			jLabelLoginPassword.setFont(new Font("Dialog", Font.BOLD, 14));
			jLabelLoginPassword.setText("  ��    �룺");
			jLabelLoginUserName = new JLabel();
			jLabelLoginUserName.setBounds(new Rectangle(18, 53, 335, 38));
			jLabelLoginUserName.setFont(new Font("Dialog", Font.BOLD, 14));
			jLabelLoginUserName.setText("  Talk�ţ�");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(jLabelLoginUserName, null);
			jContentPane.add(jLabelLoginPassword, null);
			jContentPane.add(getJTextFieldLoginUserName(), null);
			jContentPane.add(getJTextFieldLoginPassword(), null);
			jContentPane.add(getJButtonLogin(), null);
			jContentPane.add(getJButtonReg(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jTextFieldLoginUserName	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldLoginUserName() {
		if (jTextFieldLoginUserName == null) {
			jTextFieldLoginUserName = new JTextField();
			jTextFieldLoginUserName.setBounds(new Rectangle(84, 56, 266, 33));
			jTextFieldLoginUserName.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyPressed(java.awt.event.KeyEvent e) {
					if(e.getKeyCode()==java.awt.event.KeyEvent.VK_ENTER){
						//�����û������а��س�ʱ�����õ�½����
						doLogin();
					}
				}
			});
		}
		return jTextFieldLoginUserName;
	}

	/**
	 * This method initializes jTextFieldLoginPassword	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTextFieldLoginPassword() {
		if (jTextFieldLoginPassword == null) {
			jTextFieldLoginPassword = new JPasswordField();
			jTextFieldLoginPassword.setBounds(new Rectangle(84, 111, 266, 33));
			jTextFieldLoginPassword.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyPressed(java.awt.event.KeyEvent e) {
					if(e.getKeyCode()==java.awt.event.KeyEvent.VK_ENTER){
						//����������а��س�ʱ�����õ�½����
						doLogin();
					}
				}
			});
		}
		return jTextFieldLoginPassword;
	}

	/**
	 * This method initializes jButtonLogin	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonLogin() {
		if (jButtonLogin == null) {
			jButtonLogin = new JButton();
			jButtonLogin.setBounds(new Rectangle(143, 162, 120, 41));
			jButtonLogin.setText("��½");
			jButtonLogin.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//������˵�½��ťʱ�����õ�½����
					doLogin(); 
				}
			});
		}
		return jButtonLogin;
	}
	
	//��½����
	private void doLogin(){
		try{
			//���ӷ�������
			this.linkInfo.initSocket();
			
			if(this.linkInfo.getSocket()!=null){
				//��һЩ�򵥵���֤��ʵ��������Ӧ���бȽϸ��ӵ���֤�����������ַ�
				if(jTextFieldLoginUserName.getText()!=null&&!jTextFieldLoginUserName.getText().equals("")&&jTextFieldLoginPassword.getText()!=null&&!jTextFieldLoginPassword.getText().equals("")){
					if(in==null){
						in = this.linkInfo.getSocket().getInputStream();
					}
					if(out==null){
						out = this.linkInfo.getSocket().getOutputStream();
					}
//					�����ȡ���Է���������Ϣ���߳�û�����������������߳�
					if(gif==null){
						gif = new GetServerInfo(this.linkInfo);
						new Thread(gif).start();
					}
					//��½�����У�ʹ��½�����ϵĵ�½��ť������
					jButtonLogin.setEnabled(false);
					//����LinkInfo���Լ�����Ϣ
					this.linkInfo.setMe(jTextFieldLoginUserName.getText());
					//���͵�½����
					IOUtil.writeShort(Request_Command.LOGIN, out);
					IOUtil.writeString(jTextFieldLoginUserName.getText(), out);
					IOUtil.writeString(jTextFieldLoginPassword.getText(), out);
				}else{
					this.linkInfo.getAlert().showAlert("�û���������Ϊ��");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * This method initializes jButtonReg	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonReg() {
		if (jButtonReg == null) {
			jButtonReg = new JButton();
			jButtonReg.setBounds(new Rectangle(295, 214, 82, 28));
			jButtonReg.setText("ȥע��");
			jButtonReg.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//��������ȥע�ᰴť
					//�رյ�½��
					closeFrame();
					//��ʾע���
					linkInfo.setReg(new Reg(linkInfo));
				}
			});
		}
		return jButtonReg;
	}

}
