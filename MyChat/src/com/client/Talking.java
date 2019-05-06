package com.client;

import java.util.Locale;
import java.util.ResourceBundle;


public class Talking {
	private LinkInfo linkInfo = new LinkInfo();
	public static void main(String []args){
		Talking talk = new Talking();
		//���������ļ�
		talk.linkInfo.setResourceBundle(ResourceBundle.getBundle("init",
					Locale.getDefault()));
		//������ʾ����
		talk.linkInfo.setAlert(new Alert());
		talk.showLogin();
	}
	//��ʾ��½����
	public void showLogin(){
		linkInfo.setLogin(new Login(linkInfo));
	}
}
