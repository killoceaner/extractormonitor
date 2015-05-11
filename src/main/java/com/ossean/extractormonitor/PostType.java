package com.ossean.extractormonitor;

import java.util.Date;

public class PostType implements Comparable<PostType>{
	 
	private String title;
	private String content;
	private Date postTime;
	private String url;
	private String crawlerTime;
	private String extractTime;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public Date getPostTime() {
		return postTime;
	}
	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getCrawlerTime() {
		return crawlerTime;
	}
	public void setCrawlerTime(String crawlerTime) {
		this.crawlerTime = crawlerTime;
	}
	public String getExtractTime() {
		return extractTime;
	}
	public void setExtractTime(String extractTime) {
		this.extractTime = extractTime;
	}
	
	public int compareTo(PostType o) {
		// TODO Auto-generated method stub
	    if(this.postTime.after(o.postTime)){
	    	return 1;
	    }else{
	    	return 0;
	    }
	}
			
}
