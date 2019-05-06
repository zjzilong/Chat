package com.client;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.client.vo.Request_Command;

public class TalkWindow {

	private JFrame jFrame = null; // @jve:decl-index=0:visual-constraint="168,56"
	private JPanel jContentPane = null;
	private JPanel jPanel = null;
	private JTextField jTextField = null;
	private JButton jButton = null;
	private JScrollPane jScrollPane = null;
	private JTextArea jTextArea = null;
	private LinkInfo linkInfo;
	private String me;
	private String target;
	private OutputStream out;// ���ӷ������˵������
	// ׼�����͵��ļ�
	private File readyforsendfile;
	// ׼�����յ��ļ�
	private File readyforgetFile;

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setBounds(new Rectangle(1, 1, 336, 194));
			jScrollPane.setViewportView(getJTextArea());
		}
		return jScrollPane;
	}

	// �رմ��ڵ�ͬʱɾ�����촰����Ϣ
	public void closeFrameAndClearTalkWindowInfo() {
		for (int i = 0; i < linkInfo.getAllTalkWindowInfos().size(); i++) {
			if (linkInfo.getAllTalkWindowInfos().get(i).getTalkingWindow()
					.equals(this)) {
				linkInfo.getAllTalkWindowInfos().remove(i);
				break;
			}
		}
		jFrame.dispose();
	}

	// �ر����촰��
	public void closeFrame() {
		jFrame.dispose();
	}

	/**
	 * This method initializes jTextArea
	 * 
	 * @return javax.swing.JTextArea
	 */
	private JTextArea getJTextArea() {
		if (jTextArea == null) {
			jTextArea = new JTextArea();
			jTextArea.setEnabled(true);
			jTextArea.setEditable(false);
		}
		return jTextArea;
	}

	public TalkWindow(String me, String target, LinkInfo linkInfo) {
		this.getJFrame().setVisible(true);
		this.me = me;
		this.target = target;
		this.linkInfo = linkInfo;
		if (target.equals("ALL")) {
			jFrame.setTitle("Ⱥ����...");
		} else {
			jFrame.setTitle("��" + target + "������...");
		}
		try {
			out = linkInfo.getSocket().getOutputStream();
		} catch (IOException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}

	}

	/**
	 * This method initializes jFrame
	 * 
	 * @return javax.swing.JFrame
	 */
	private JFrame getJFrame() {
		if (jFrame == null) {
			jFrame = new JFrame();
			jFrame.setLayout(null);
			jFrame.setSize(new Dimension(361, 349));
			jFrame.setResizable(false);
			jFrame.setContentPane(getJContentPane());
			jFrame.addWindowListener(new java.awt.event.WindowAdapter() {
				public void windowClosing(java.awt.event.WindowEvent e) {
					jFrame.dispose();
					removeTalkWindowInfo();
				}
			});
		}
		return jFrame;
	}

	private void removeTalkWindowInfo() {
		for (int i = 0; i < this.linkInfo.getAllTalkWindowInfos().size(); i++) {
			if (this.linkInfo.getAllTalkWindowInfos().get(i).getTalkingWindow()
					.equals(this)) {
				this.linkInfo.getAllTalkWindowInfos().remove(i);
				break;
			}

		}
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJPanel(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.setBounds(new Rectangle(0, 1, 345, 310));
			jPanel.add(getJTextField(), null);
			jPanel.add(getJButton(), null);
			jPanel.setVisible(true);
			jPanel.add(getJScrollPane(), null);
		}
		return jPanel;
	}

	/**
	 * This method initializes jTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
			jTextField.setBounds(new Rectangle(5, 203, 333, 35));
			jTextField.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyPressed(java.awt.event.KeyEvent e) {
					if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
						sendMessageToServer();
					}
				}
			});
		}
		return jTextField;
	}

	/**
	 * This method initializes jButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(193, 254, 138, 49));
			jButton.setText("�����ļ�");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					sendFile();
				}
			});
		}
		return jButton;
	}

	public boolean sendMessageToServer() {
		// ����Ѿ�û�к������ߣ����֪�û����˺���������
		if (linkInfo.getFriendUserInfos().size() == 0) {
			jTextArea.setText("�����û�����...\n");
			this.jScrollPane.getVerticalScrollBar().setValue(
					jScrollPane.getVerticalScrollBar().getMaximum());
			return false;
		}
		// û��Ŀ��������Ⱥ��
		if (target.equals("ALL")) {
			IOUtil.writeShort(Request_Command.SEND_TEXT, out);
			IOUtil.writeString(jTextField.getText(), out);
			jTextArea.setText(jTextArea.getText() + "��˵:\n   "
					+ jTextField.getText() + "\n");
			this.jScrollPane.getVerticalScrollBar().setValue(
					jScrollPane.getVerticalScrollBar().getMaximum());
			jTextField.setText("");
			return true;
		}

		if (linkInfo.getFriendUserInfos().size() > 0) {
			boolean friendOnline = false;// ��ǰ��������Ƿ�����
			for (int i = 0; i < linkInfo.getFriendUserInfos().size(); i++) {
				if (linkInfo.getFriendUserInfos().get(i).equals(target)) {
					friendOnline = true;
					break;
				}
			}
			// �����ǰ����������ߣ����֪�û����˺���������
			if (!friendOnline) {
				jTextArea.setText(jTextArea.getText() + this.target
						+ "������..��Ϣ����ʧ��\n");
				this.jScrollPane.getVerticalScrollBar().setValue(
						jScrollPane.getVerticalScrollBar().getMaximum());
				return false;
			}
		}
		// ��������������Ϣ����������Ϣ���͸���������
		if (!jTextField.getText().equals("")) {
			IOUtil.writeShort(Request_Command.SEND_TEXT_TO_ONE, out);
			IOUtil.writeString(target, out);
			IOUtil.writeString(jTextField.getText(), out);
			jTextArea.setText(jTextArea.getText() + "��˵:\n   "
					+ jTextField.getText() + "\n");
			this.jScrollPane.getVerticalScrollBar().setValue(
					jScrollPane.getVerticalScrollBar().getMaximum());
			jTextField.setText("");
			return true;
		}
		return false;
	}

	// ��������Ϣ��ʾ������Ի�����
	public void showMessage(String message) {
		this.jTextArea.setText(this.jTextArea.getText() + target + "˵:\n   "
				+ message + "\n");
		this.jScrollPane.getVerticalScrollBar().setValue(
				jScrollPane.getVerticalScrollBar().getMaximum());
	}

	// ��ʾȺ����Ϣ
	public void showAllMessage(String from, String message) {
		this.jTextArea.setText(this.jTextArea.getText() + from + "˵:\n   "
				+ message + "\n");
		this.jScrollPane.getVerticalScrollBar().setValue(
				jScrollPane.getVerticalScrollBar().getMaximum());
	}

	// ��ʾ�����ļ���Ӧ
	public void alertFileResponse(String from, String fileName) {
		int v = JOptionPane.showConfirmDialog(jPanel, "�û�:" + from + "���������ļ�["
				+ fileName + "],�Ƿ����?");
		if (v == JOptionPane.YES_OPTION) {
			// ͬ�����
			JFileChooser fd = new JFileChooser();
			JTextField text = getTextField(fd);
			text.setText(fileName);
			fd.showSaveDialog(null);
			readyforgetFile = fd.getSelectedFile();
			IOUtil.writeShort(Request_Command.SEND_FILE_TO_ONE_1, out);
			IOUtil.writeString(from, out);
			IOUtil.writeString("OK", out);
		} else {
			// ��ͬ�����
			IOUtil.writeShort(Request_Command.SEND_FILE_TO_ONE_1, out);
			IOUtil.writeString(from, out);
			IOUtil.writeString("NO", out);
		}
	}
	public JTextField getTextField(Container c) {
        JTextField textField = null;
        for (int i = 0; i < c.getComponentCount(); i++) {
            Component cnt = c.getComponent(i);
            if (cnt instanceof JTextField) {
                return (JTextField) cnt;
            }
            if (cnt instanceof Container) {
                textField = getTextField((Container) cnt);
                if (textField != null) {
                    return textField;
                }
            }
        }
        return textField;
    }

	public void getFile(){
		try {
			InputStream in = linkInfo.getSocket().getInputStream();
			long len = IOUtil.readLong(in);
			System.out.println("�ܴ�С��"+len);
			byte[] buf = new byte[1024];	
			//���ζ�ȡ���ֽ���
			int loaded = (int)(1024<len?1024:len);
			//�ܹ���ȡ���ֽ���
			long sum=0;
			FileOutputStream fos = new FileOutputStream(readyforgetFile);
			this.jTextArea.setText(this.jTextArea.getText() + "���ڽ����ļ�������\n");
			this.jScrollPane.getVerticalScrollBar().setValue(
					jScrollPane.getVerticalScrollBar().getMaximum());
			while(true){
				in.read(buf,0,loaded);
				fos.write(buf,0,loaded);
				sum+=loaded;
				loaded = (int)(sum+1024<len?1024:len-sum);
				if(sum==len){
					break;
				}
			}
			fos.close();
			this.jTextArea.setText(this.jTextArea.getText() + "�ļ�������ϡ�\n");
			this.jScrollPane.getVerticalScrollBar().setValue(
					jScrollPane.getVerticalScrollBar().getMaximum());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ���ݻ�ִȷ���Ƿ����ļ�
	 */
	public void sendFile1(String return_info,final String target) {
		if ("OK".equals(return_info)) {
			this.jTextArea.setText(this.jTextArea.getText() + "��ʼ�����ļ�������\n");
			this.jScrollPane.getVerticalScrollBar().setValue(
					jScrollPane.getVerticalScrollBar().getMaximum());
			this.jButton.setEnabled(false);// ����ʱ����
			new Thread() {
				public void run() {
					try {
						IOUtil.writeShort(Request_Command.SEND_FILE_TO_ONE_2,
								out);
						IOUtil.writeString(target, out);
						IOUtil.writeLong(readyforsendfile.length(), out);
						FileInputStream fis = new FileInputStream(
								readyforsendfile);
						byte[] buf = new byte[1024 * 10];
						int len = -1;
						while ((len = fis.read(buf)) != -1) {
							out.write(buf, 0, len);
						}
						fis.close();
						jTextArea.setText(jTextArea.getText() + "������ϡ�����\n");
						jScrollPane.getVerticalScrollBar()
								.setValue(
										jScrollPane.getVerticalScrollBar()
												.getMaximum());
						jButton.setEnabled(true);// ��������
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();

		} else {
			this.jTextArea.setText(this.jTextArea.getText() + "�Է��ܾ������ļ���\n");
			this.jScrollPane.getVerticalScrollBar().setValue(
					jScrollPane.getVerticalScrollBar().getMaximum());
		}
	}

	// �����ļ�����
	public void sendFile() {
		JFileChooser fd = new JFileChooser();
		fd.showOpenDialog(null);
		File f = fd.getSelectedFile();
		System.out.println(f.getName());
		if (f != null) {
			readyforsendfile = f;
			// ֪ͨĿ���û������ļ�
			IOUtil.writeShort(Request_Command.SEND_FILE_TO_ONE, out);
			IOUtil.writeString(target, out);
			IOUtil.writeString(f.getName(), out);
			System.out.println("���������");
		}

	}
}
