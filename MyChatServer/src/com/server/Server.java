package com.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import com.server.vo.UserInfo;

public class Server {
	//ϵͳ�����û�����Ϣ
	private HashMap<String,UserInfo> users;
	public Server(){
		users = new HashMap<String,UserInfo>();
	}
	/**
	 * ��������������
	 */
	public void start(){
		try {
			System.out.println("��������������");
			ServerSocket ss = new ServerSocket(7788);
			System.out.println("�������������");
			while(true){
				DoServiceForClient dsfc = new DoServiceForClient(ss.accept());
				new Thread(dsfc).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String []args){
		Server server = new Server();
		server.start();
	}
	
	class DoServiceForClient implements Runnable{
		//��ǰ���ӿͻ��˵��׽���
		private Socket client;
		//��ǰ�ͻ��˵�������
		private InputStream in;
		//��ǰ�ͻ��˵������
		private OutputStream out;
		//��ǰ�ͻ��˶�Ӧ���û���Ϣ
		private UserInfo userinfo;
		
		//���췽��
		public DoServiceForClient(Socket client){
			this.client = client;
		}
		//�߳���
		public void run() {
			try {
				//��ȡ��ǰ���ӿͻ��˵������������ڻ�ȡ���Կͻ��˵���Ϣ
				in = client.getInputStream();
				out = client.getOutputStream();
				int command = -1;
				while((command = in.read())!=-1){
					switch(command){
						//��½����
						case Request_Command.LOGIN:
							System.out.println("message:��½����");
							doLogin();
							break;
						//ע�����
						case Request_Command.REG:
							System.out.println("message:ע�����");
							doReg();
							break;
						//�㲥�ı���Ϣ
						case Request_Command.SEND_TEXT:
							System.out.println("message:�㲥��Ϣ");
							sendMessageToAll();
							break;
						//�ı�˽��
						case Request_Command.SEND_TEXT_TO_ONE:
							System.out.println("message:˽����Ϣ");
							sendMessageToOne();
							break;
						//�㲥�ļ�
						case Request_Command.SEND_FILE:
							break;
						//������˴����ļ�
						case Request_Command.SEND_FILE_TO_ONE:
							System.out.println("message:�������ļ���˽��");
							sendFileToOne();
							break;
						//����ظ������˽��
						case Request_Command.SEND_FILE_TO_ONE_1:
							System.out.println("message:�ظ��Ƿ�����ļ�");
							sendFileToOne1();
							break;
						//����ʼ�����ļ�
						case Request_Command.SEND_FILE_TO_ONE_2:
							System.out.println("message:��ʼ��˽�˴����ļ�");
							sendFileToOne2();
							break;
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				//�ӹ�����ɾ����ǰ�û�
				users.remove(userinfo.getUsername());
				//ˢ�����пͻ��˵ĺ����б�
				try {
					reshowFriendList();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		/**
		 * ��ʼ�����ļ�
		 */
		public void sendFileToOne2()throws Exception{
			try{
				String target = IOUtil.readString(in);
				long len = IOUtil.readLong(in);
				byte[] buf = new byte[1024];	
				//���ζ�ȡ���ֽ���
				int loaded = (int)(1024<len?1024:len);
				//�ܹ���ȡ���ֽ���
				long sum=0;
				OutputStream out = users.get(target).getSocket().getOutputStream();
				IOUtil.writeShort(Response_Command.FILE_TO_ONE_2_RESPONSE, out);
				IOUtil.writeString(userinfo.getUsername(), out);
				IOUtil.writeLong(len, out);
				while(true){
					in.read(buf,0,loaded);
					out.write(buf,0,loaded);
					sum+=loaded;
					loaded = (int)(sum+1024<len?1024:len-sum);
					if(sum==len){
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
				
		}
		/**
		 * ����ظ������˽��
		 */
		public void sendFileToOne1()throws Exception{
			String target = IOUtil.readString(in);
			String return_info = IOUtil.readString(in);
			try {
				if("OK".equals(return_info)){
					OutputStream out = users.get(target).getSocket().getOutputStream();
					IOUtil.writeShort(Response_Command.FILE_TO_ONE_1_RESPONSE, out);
					IOUtil.writeString(userinfo.getUsername(), out);
					IOUtil.writeString("OK", out);
				}else{
					OutputStream out = users.get(target).getSocket().getOutputStream();
					IOUtil.writeShort(Response_Command.FILE_TO_ONE_1_RESPONSE, out);
					IOUtil.writeString(userinfo.getUsername(), out);
					IOUtil.writeString("NO", out);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		/**
		 * ���ռ���֪ͨ��ѯ���Ƿ�����ļ�
		 */
		public void sendFileToOne()throws Exception{
			String target = IOUtil.readString(in);
			String fileName = IOUtil.readString(in);
			try {
				OutputStream out = users.get(target).getSocket().getOutputStream();
				IOUtil.writeShort(Response_Command.FILE_TO_ONE_RESPONSE, out);
				//���͵�ǰ�û���Ŀ��
				IOUtil.writeString(userinfo.getUsername(), out);
				//�����ļ���
				IOUtil.writeString(fileName, out);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
		}
		
		/**
		 * �㲥����
		 */
		public void sendMessageToAll()throws Exception{
			//��ȡ��Ϣ
			String message = IOUtil.readString(in);
			//��ȡ�Է������
			try {
				for(UserInfo userInfo : users.values()){
					//����ת�����Լ�
					if(userInfo.getUsername().equals(userinfo.getUsername())){
						continue;
					}
					OutputStream out = userInfo.getSocket().getOutputStream();
					IOUtil.writeShort(Response_Command.MESSAGE_TO_ALL_RESPONSE, out);
					//���͵�ǰ�û���Ŀ��
					IOUtil.writeString(userinfo.getUsername(), out);
					IOUtil.writeString(message, out);
				}
			} catch (Exception e) {
				throw e;
			}
			
			
		}
		/**
		 * ˽�Ĺ���
		 */
		public void sendMessageToOne()throws Exception{
			//��ȡ�������
			String target = IOUtil.readString(in);
			//��ȡ��Ϣ
			String message = IOUtil.readString(in);
			//��ȡ�Է������
			try {
				OutputStream out = users.get(target).getSocket().getOutputStream();
				IOUtil.writeShort(Response_Command.MESSAGE_TO_ONE_RESPONSE, out);
				//���͵�ǰ�û���Ŀ��
				IOUtil.writeString(userinfo.getUsername(), out);
				IOUtil.writeString(message, out);
			} catch (Exception e) {
				throw e;
			}
			
			
		}
		
		//ˢ�������û��ĺ����б�
		public void reshowFriendList()throws Exception{
			StringBuffer friendsInfo=new StringBuffer();
			for(String user:users.keySet()){
				friendsInfo.append(user+",");
			}
			for(UserInfo userinfo : users.values()){
				
				try{
					IOUtil.writeShort(Response_Command.USER_LIST_RESPONSE, userinfo.getSocket().getOutputStream());
					IOUtil.writeString(friendsInfo.toString(), userinfo.getSocket().getOutputStream());
				}catch(Exception e){
					throw e;
				}
			}
		}
		
		//ע�᷽��
		public void doReg()throws Exception{
			try{
				//��ȡ�û���
				String username = IOUtil.readString(in);
				//��ȡ����
				String password = IOUtil.readString(in);
				DBManager.addUser(new UserInfo(username,password));
				//���빲��
				userinfo = new UserInfo();
				userinfo.setUsername(username);
				userinfo.setPassword(password);
				userinfo.setSocket(client);
				users.put(username, userinfo);
			}catch(Exception e){
				//������Ӧ
				IOUtil.writeShort(Response_Command.REG_RESPONSE, out);
				IOUtil.writeString(e.getMessage(), out);
				throw e;
			}
			//������Ӧ
			IOUtil.writeShort(Response_Command.REG_RESPONSE, out);
			IOUtil.writeString("regSuccess", out);
			//ˢ�����пͻ��˵ĺ����б�
			reshowFriendList();
			
		}
		
		//��½����
		public void doLogin()throws Exception{
			try{
				//��ȡ�û���
				String username = IOUtil.readString(in);
				//��ȡ����
				String password = IOUtil.readString(in);
				if(!checkUserInfoHasLoginByUserName(username)){	
					UserInfo user = new UserInfo(username,password);
					if(DBManager.login(user)){
						userinfo = user;
						userinfo.setSocket(client);
						//���빲��
						users.put(username,userinfo);
						//������Ӧ
						IOUtil.writeShort(Response_Command.LOGIN_RESPONSE, out);
						IOUtil.writeString("loginSuccess", out);	
						//ˢ�����пͻ��˵ĺ����б�
						reshowFriendList();
					}else{
						IOUtil.writeShort(Response_Command.LOGIN_RESPONSE, out);
						IOUtil.writeString("�û������������...", out);
					}
				}else{
					IOUtil.writeShort(Response_Command.LOGIN_RESPONSE, out);
					IOUtil.writeString("�˺�:"+username+" ���ڵ�½״̬...", out);
				}
			}catch(Exception e){
				IOUtil.writeShort(Response_Command.LOGIN_RESPONSE, out);
				IOUtil.writeString("��½�쳣"+e.getMessage(), out);
				throw e;
			}
		}
		//�жϵ�ǰ�û��Ƿ��ڵ�½״̬
		public boolean checkUserInfoHasLoginByUserName(String username){
			return users.containsKey(username);
		}
		
	}
}
