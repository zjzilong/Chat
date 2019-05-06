package com.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;

import com.server.vo.UserInfo;

public class DBManager {
	private static HashMap<String,UserInfo> users = new HashMap<String,UserInfo>();
	private static File userdata = new File("user.dat");

	/**
	 * ע�����û�
	 * 
	 * @return
	 */
	public static synchronized void addUser(UserInfo userInfo)throws Exception {
		//���û����Ѵ���
		if(users.containsKey(userInfo.getUsername())){
			throw new Exception("���û����Ѿ���ʹ��");
		}		
		try {
			//������û�
			FileOutputStream fos
				= new FileOutputStream(userdata,true);
			OutputStreamWriter osw
				= new OutputStreamWriter(fos,"utf-8");
			PrintWriter pw
				= new PrintWriter(osw);
			pw.println(userInfo);
			pw.close();
		} catch (Exception e) {
			throw new Exception("ע��ʧ��");
		}
		//ע���ˢ�»���
		users.put(userInfo.getUsername(), userInfo);
	}
	
	public static boolean login(UserInfo userInfo){
		UserInfo user = users.get(userInfo.getUsername());
		//û����û�
		if(user == null){
			return false;
		}
		//���벻��
		if(!user.getPassword().equals(userInfo.getPassword())){
			return false;
		}
		//�ɹ�
		return true;
	}
	
	
	// ��̬�����ڳ�ʼ��
	static {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ʼ��
	 */
	private static void init() {
		try {
			if (!userdata.exists()) {
				userdata.createNewFile();
			}
			loadUsers();
		} catch (Exception e) {

		}
	}

	/**
	 * ��ȡ�����û�
	 */
	private synchronized static void loadUsers() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream("user.dat"), "utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				UserInfo user = new UserInfo(line);
				users.put(user.getUsername(),user);
			}
		} catch (Exception e) {

		}
	}

}
