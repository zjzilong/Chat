package com.client.vo;

import javax.swing.tree.DefaultMutableTreeNode;

public class TalkingMainTreeInfo {
	private String userInfo;//�û�
	private DefaultMutableTreeNode dmtn;//���û���Ӧ�����Ľڵ�
	public TalkingMainTreeInfo(){
		
	}
	public TalkingMainTreeInfo(String userInfo,DefaultMutableTreeNode dmtn){
		this.userInfo = userInfo;
		this.dmtn = dmtn;
	}
	public DefaultMutableTreeNode getDmtn() {
		return dmtn;
	}
	public void setDmtn(DefaultMutableTreeNode dmtn) {
		this.dmtn = dmtn;
	}
	public String getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}
	
}
