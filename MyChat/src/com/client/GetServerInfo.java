package com.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.client.vo.Response_Command;
import com.client.vo.TalkWindowInfo;
//���߳������ڻ�ȡ���Է������˵���Ϣ
public class GetServerInfo implements Runnable{
	private InputStream in;
	private LinkInfo linkInfo;
	public GetServerInfo(LinkInfo linkInfo){
		try {
			this.linkInfo = linkInfo;
			//��ȡ���Է������˵�������������װ�ɸ߼���BufferedReader,������ֱ�Ӵ����ַ���
			in = linkInfo.getSocket().getInputStream();
		} catch (IOException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}		
	}
	public void run() {
		try{
			int command = -1;
			String fromuser;
			while((command = in.read())!=-1){
				switch(command){				
					case Response_Command.REG_RESPONSE:
						String info = IOUtil.readString(in);
						if(info.equals("regSuccess")){
							System.out.println("ע��ɹ�");
//							��ʾע��ɹ���Ϣ
							linkInfo.getAlert().showAlert("<html><center>ע��ɹ�����ӭ��"+this.linkInfo.getMe()+"</center></html>");
							TalkingMain tm = new TalkingMain();
							tm.init(this.linkInfo);
							this.linkInfo.getReg().closeFrame();
						}else{
							//��ʾ������Ϣ
							linkInfo.getAlert().showAlert("<html><center><h1>ע��ʧ��</h1>ԭ��:"+info+"</center></html>");
							linkInfo.getReg().jButtonReg.setEnabled(true);//ʹע�ᰴť����
							linkInfo.getReg().jButtonLogin.setEnabled(true);//ʹȥ��½��ť����
						}
						break;
					case Response_Command.LOGIN_RESPONSE:
						String info1 = IOUtil.readString(in);
						if(info1.equals("loginSuccess")){
							TalkingMain tm = new TalkingMain();
							tm.init(this.linkInfo);
							this.linkInfo.getLogin().closeFrame();
						}else{
							linkInfo.getAlert().showAlert("<html><center><h1>��½ʧ��</h1>ԭ��:"+info1+"</center></html>");
							linkInfo.getLogin().jButtonLogin.setEnabled(true);//ʹ��½��ť����
							linkInfo.getLogin().jButtonReg.setEnabled(true);//ʹȥע�ᰴť����
						}
						break;
					case Response_Command.USER_LIST_RESPONSE:
						String userLine = IOUtil.readString(in);
						String []friends = userLine.split(",");
						if(friends!=null&&friends.length>0){
							ArrayList <String>friendUserInfos = new ArrayList<String>();
							linkInfo.setFriendUserInfos(friendUserInfos);
							for(int i =0;i<friends.length;i++){
								friendUserInfos.add(friends[i]);
							}
							//�������û���ʾ��������ȥ==>TalkingMain
							this.linkInfo.getTalkingMain().showAllFriend(friendUserInfos);
						}
						break;
					case Response_Command.MESSAGE_TO_ONE_RESPONSE:
						fromuser = IOUtil.readString(in);
						String message = IOUtil.readString(in);
						TalkWindow tw = getTalkWindowById(fromuser);
						tw.showMessage(message);
						break;
					case Response_Command.MESSAGE_TO_ALL_RESPONSE:
						fromuser = IOUtil.readString(in);
						String messages = IOUtil.readString(in);
						TalkWindow twAll = getTalkWindowById("ALL");
						twAll.showAllMessage(fromuser,messages);
					
						break;
					case Response_Command.FILE_TO_ONE_RESPONSE:
						fromuser = IOUtil.readString(in);
						String fileName = IOUtil.readString(in);
						TalkWindow t = getTalkWindowById(fromuser);
						t.alertFileResponse(fromuser,fileName);
						break;
					case Response_Command.FILE_TO_ONE_1_RESPONSE:
						fromuser = IOUtil.readString(in);
						String return_info = IOUtil.readString(in);
						TalkWindow t1 = getTalkWindowById(fromuser);
						t1.sendFile1(return_info,fromuser);		
						break;
					case Response_Command.FILE_TO_ONE_2_RESPONSE:
						fromuser = IOUtil.readString(in);	
						TalkWindow t2 = getTalkWindowById(fromuser);
						t2.getFile();		
						break;
//					//��ʾ��������
//					if(info.equals("message")){
//						String messageInfo[] = infos[1].split(Tools.getSysTagModel("from"));//����from�����Ϣ��Դ���Ǹ��û���(ֻ��IDֵ)
//						TalkWindow tw = getTalkWindowById(messageInfo[0]);
//						tw.showMessage(messageInfo[1].replace(Tools.getSysTagModel("n"),"\n"));
//					}
//					//��ʾ�û��б���������
//					else if(infos[0].equals("showAllFriends")){
//						//ʹ��;�Ž�ȡÿ���û�
//						String []friends = infos[1].split(Tools.getSysTagModel(";"));
//						if(friends!=null&&friends.length>0){
//							ArrayList <UserInfo>friendUserInfos = new ArrayList<UserInfo>();
//							linkInfo.setFriendUserInfos(friendUserInfos);
//							for(int i =0;i<friends.length;i++){
//								//��,����û���������Ϣ
//								String []friend = friends[i].split(Tools.getSysTagModel(","));
//								UserInfo fri = new UserInfo(friend[0],friend[1]);
//								friendUserInfos.add(fri);
//							}
//							//�������û���ʾ��������ȥ==>TalkingMain
//							this.linkInfo.getTalkingMain().showAllFriend(friendUserInfos);
//						}
//					}
//					//��¼�ɹ��Ĳ���
//					else if(infos[0].equals("loginSuccess")){
//						this.linkInfo.getMe().setUsername(infos[1]);
//						TalkingMain tm = new TalkingMain();
//						tm.init(this.linkInfo);
//						this.linkInfo.getLogin().closeFrame();
//					}
//					//��¼ʧ�ܵĲ���
//					else if(infos[0].equals("loginError")){
//						linkInfo.getAlert().showAlert("<html><center><h1>��½ʧ��</h1>ԭ��:"+infos[1]+"</center></html>");
//						linkInfo.getLogin().jButtonLogin.setEnabled(true);//ʹ��½��ť����
//						linkInfo.getLogin().jButtonReg.setEnabled(true);//ʹȥע�ᰴť����
//					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			//�����������Ͽ�����
			//��ʾ�û�
			linkInfo.getAlert().showAlert("<html><center><h1>��������Ͽ�����</h1>ԭ��:���п���������ԭ�򣬻���������쳣����</center></html>");
			//�ر�������
			linkInfo.getTalkingMain().closeFrame();
			//�ر������Ѵ򿪵����촰��
			for(int i =0;i<linkInfo.getAllTalkWindowInfos().size();i++){
				linkInfo.getAllTalkWindowInfos().get(i).getTalkingWindow().closeFrame();
			}
			//������촰����Ϣ
			linkInfo.getAllTalkWindowInfos().clear();
			//�򿪵�½��
			linkInfo.setLogin(new Login(this.linkInfo));
		}
	}
	//���ݺ���ID��ȡ�˺��ѵ����촰��
	public TalkWindow getTalkWindowById(String user){
		//�ж���˺�������Ĵ����Ƿ��Ѿ���
		for(int i =0;i<this.linkInfo.getAllTalkWindowInfos().size();i++){
			if(this.linkInfo.getAllTalkWindowInfos().get(i).getTargetUserInfo().equals(user)){
				return this.linkInfo.getAllTalkWindowInfos().get(i).getTalkingWindow();
			}
		}
		//����˺��ѵ����촰��û�д򿪣����Զ���һ����˺�����������촰��
		TalkWindow talkWindow = null;
		
		//����Ⱥ��
		if("ALL".equals(user)){
			talkWindow = new TalkWindow(linkInfo.getMe(),"ALL",linkInfo);
			linkInfo.getAllTalkWindowInfos().add(new TalkWindowInfo(talkWindow,"ALL"));
			return talkWindow;
		}
		
		for(int i =0;i<linkInfo.getFriendUserInfos().size();i++){
			if(linkInfo.getFriendUserInfos().get(i).equals(user)){
				talkWindow = new TalkWindow(linkInfo.getMe(),linkInfo.getFriendUserInfos().get(i),linkInfo);
				linkInfo.getAllTalkWindowInfos().add(new TalkWindowInfo(talkWindow,linkInfo.getFriendUserInfos().get(i)));
			}
		}		
		return talkWindow;
	}
}
