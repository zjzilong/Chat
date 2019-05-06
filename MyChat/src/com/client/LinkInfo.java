package com.client;

import java.net.Socket;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.client.vo.TalkWindowInfo;
//�ͻ�����Ҫ��������Ϣ
public class LinkInfo {
	//�����
	private Alert alert;
	//��½��
	private Login login;
	//ע���
	private Reg reg;
	//����
	private TalkingMain talkingMain;
	private Socket socket;
	private ResourceBundle resourceBundle;
	//��ǰ�û�
	private String me = new String();
	//���к�����Ϣ
	private ArrayList <String>friendUserInfos;
	//�Ѵ򿪵ĶԻ�������Ϣ����
	private ArrayList<TalkWindowInfo> allTalkWindowInfos = new ArrayList<TalkWindowInfo>();
	
	public ArrayList<TalkWindowInfo> getAllTalkWindowInfos() {
		return allTalkWindowInfos;
	}

	public void setAllTalkWindowInfos(ArrayList<TalkWindowInfo> allTalkWindowInfos) {
		this.allTalkWindowInfos = allTalkWindowInfos;
	}

	public String getMe() {
		return me;
	}

	public void setMe(String me) {
		this.me = me;
	}

	//��ȥ�����ļ���Ϣ
	public String getConfigValueByKey(String key){
		return (String)resourceBundle.getObject(key);
	}
	
	public boolean initSocket(){
		try{
			if(socket==null){
				int host = Integer.parseInt(this.getConfigValueByKey("serverHost"));
				this.socket = new Socket(this.getConfigValueByKey("serverLocal"),host);
				return true;
			}
		}catch(Exception e){
			System.out.println(e);
			this.alert.showAlert("���ӷ�����ʧ��..���������Ƿ���������������.");
		}		
		return false;
	}
	
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public void setResourceBundle(ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Alert getAlert() {
		return alert;
	}

	public void setAlert(Alert alert) {
		this.alert = alert;
	}

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public TalkingMain getTalkingMain() {
		return talkingMain;
	}

	public void setTalkingMain(TalkingMain talkingMain) {
		this.talkingMain = talkingMain;
	}

	public ArrayList<String> getFriendUserInfos() {
		return friendUserInfos;
	}

	public void setFriendUserInfos(ArrayList<String> friendUserInfos) {
		this.friendUserInfos = friendUserInfos;
	}

	public Reg getReg() {
		return reg;
	}

	public void setReg(Reg reg) {
		this.reg = reg;
	}
}
