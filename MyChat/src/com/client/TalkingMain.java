package com.client;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.client.vo.TalkWindowInfo;
import com.client.vo.TalkingMainTreeInfo;


public class TalkingMain {

	private JFrame jFrame = null;  //  @jve:decl-index=0:visual-constraint="357,12"
	private JPanel jContentPane = null;
	private JButton jButtonFind = null;
	private JScrollPane jScrollPane = null;
	private JTree jTree = null;
	private ArrayList<String> allFriends;//���к���
	private ArrayList<TalkingMainTreeInfo> allTreeNodes = new ArrayList<TalkingMainTreeInfo>();//���к��Ѷ�Ӧ���ڵ�
	private LinkInfo linkInfo;  //  @jve:decl-index=0:
	private String me;  //  @jve:decl-index=0:
	public TalkingMain(){
		this.getJFrame().setVisible(true);
	}
	
	public void init(LinkInfo linkInfo){
		this.me = linkInfo.getMe();
		this.linkInfo = linkInfo;
		this.linkInfo.setTalkingMain(this);
	}
	
	public void showAllFriend(ArrayList<String> allFriends){
		this.allFriends = allFriends;
		jScrollPane.setViewportView(getJTree(initTree()));
	}
	
	/**
	 * This method initializes jFrame	
	 * 	
	 * @return javax.swing.JFrame	
	 */
	public void closeFrame(){
		jFrame.dispose();
		this.linkInfo.setTalkingMain(null);
	}
	private JFrame getJFrame() {
		if (jFrame == null) {
			jFrame = new JFrame();
			jFrame.setSize(new Dimension(207, 441));
			jFrame.setTitle("Talking");
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
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJButtonFind(), null);
			jContentPane.add(getJScrollPane(), null);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jButtonFind	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonFind() {
		if (jButtonFind == null) {
			jButtonFind = new JButton();
			jButtonFind.setBounds(new Rectangle(1, 384, 199, 29));
			jButtonFind.setText("����Ⱥ��");
			jButtonFind.addActionListener(new ActionListener() {		
				@Override
				public void actionPerformed(ActionEvent e) {
					openTalkingWindow(null);			
				}
			});
		}
		return jButtonFind;
	}

	/**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setBounds(new Rectangle(2, 2, 200, 376));
		}
		return jScrollPane;
	}

	/**
	 * This method initializes jTree	
	 * 	
	 * @return javax.swing.JTree	
	 */
	private JTree getJTree(DefaultMutableTreeNode nodes) {
		//if (jTree == null) {
			jTree = new JTree(nodes);
			//����ǰ���������һ��˫���¼�
			jTree.addMouseListener(new   MouseAdapter(){   
				  public   void   mouseClicked(MouseEvent   e){   
					  if(e.getClickCount()   ==   2){   
						  DefaultMutableTreeNode selectedNode=(DefaultMutableTreeNode)jTree.getLastSelectedPathComponent();
						  if(!selectedNode.toString().equals("�����б�("+me+")")){
							  openTalkingWindow(selectedNode);
						  }					 
					  }   
					  }   
					  });
		//}
		return jTree;
	}
	
	public void openTalkingWindow(DefaultMutableTreeNode node){
		if(node==null){
			//����Ⱥ��
			if(!checkTalkWindowHasOpenedByTargetUserId("ALL")){
				TalkWindow tw = new TalkWindow(me,"ALL",this.linkInfo);
				this.linkInfo.getAllTalkWindowInfos().add(new TalkWindowInfo(tw,"ALL"));
			}
			return;
		}
		String userInfo = this.getUserInfoByTreeNode(node);
		if(!checkTalkWindowHasOpenedByTargetUserId(userInfo)){
			TalkWindow tw = new TalkWindow(me,userInfo,this.linkInfo);
			this.linkInfo.getAllTalkWindowInfos().add(new TalkWindowInfo(tw,userInfo));
		}	
	}
	
	//������ID�������������촰���Ƿ��Ѿ�����
	public boolean checkTalkWindowHasOpenedByTargetUserId(String id){
		//������Լ�����ֱ�ӷ���TRUE���Լ�����Ҫ�򿪶��Լ�˵���ĶԻ���
		if(this.me.equals(id)){
			return true;
		}
		for(int i =0;i<this.linkInfo.getAllTalkWindowInfos().size();i++){
			if(linkInfo.getAllTalkWindowInfos().get(i).getTargetUserInfo().equals(id)){
				return true;
			}
		}
		return false;
	}
	
	public String getUserInfoByTreeNode(DefaultMutableTreeNode node){
		if(allTreeNodes!=null&&allTreeNodes.size()>0){
			for(int i =0;i<allTreeNodes.size();i++){
				if(node.equals(allTreeNodes.get(i).getDmtn())){
					return allTreeNodes.get(i).getUserInfo();
				}
			}
		}
		return null;
	}
	
	//��ʼ���������ķ���
	public DefaultMutableTreeNode initTree(){
		DefaultMutableTreeNode root = new DefaultMutableTreeNode ("�����б�("+me+")");
		if(this.allFriends!=null&&this.allFriends.size()>0){
			allTreeNodes.clear();
			for(int i =0;i<allFriends.size();i++){
				//�����Լ����ں����б���
				if(allFriends.get(i).equals(me)){
					continue;
				}
				DefaultMutableTreeNode friend = new DefaultMutableTreeNode (allFriends.get(i));
				TalkingMainTreeInfo treeNodeInfo = new TalkingMainTreeInfo(allFriends.get(i),friend);
				allTreeNodes.add(treeNodeInfo);
				root.add(friend);
			}
		}
		return root;
	}
}
