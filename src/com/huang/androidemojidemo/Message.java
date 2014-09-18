/**
 * 
 */
package com.huang.androidemojidemo;

/**
 * @desc <pre>
 * 私信
 * </pre>
 * @author Erich Lee
 * @Date Mar 20, 2013
 */
public class Message {

	private String content;

	private boolean isSend;

	private String time;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isSend() {
		return isSend;
	}

	public void setSend(boolean isSend) {
		this.isSend = isSend;
	}

}
