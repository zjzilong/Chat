package com.client.vo;

import com.client.TalkWindow;

//�Ѵ򿪵ĶԻ�������Ϣ
public class TalkWindowInfo {
	private TalkWindow talkingWindow;//�Ի�����ʵ��
	private String targetUserInfo;//����������Ϣ
	public TalkWindowInfo(){
		
	}
	
	public TalkWindowInfo(TalkWindow talkingWindow,String targetUserInfo){
		this.talkingWindow = talkingWindow;
		this.targetUserInfo = targetUserInfo;
	}
	
	public TalkWindow getTalkingWindow() {
		return talkingWindow;
	}
	public void setTalkingWindow(TalkWindow talkingWindow) {
		this.talkingWindow = talkingWindow;
	}
	public String getTargetUserInfo() {
		return targetUserInfo;
	}
	public void setTargetUserInfo(String targetUserInfo) {
		this.targetUserInfo = targetUserInfo;
	}
	
}
