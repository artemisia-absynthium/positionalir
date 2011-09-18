package com.googlecode.positionalir.model;

public class Document {
	
	private String newid;
	private String title;
	private String body;
	private String all;

	public Document() {}
	
	public Document(String newid, String title,
			String body) {
		this.newid = newid;
		this.title = title;
		this.body = body;
	}

	public String getNewid() {
		return newid;
	}

	public void setNewid(String newid) {
		this.newid = newid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getAll() {
		this.merge();
		return all;
	}

	public void setAll(String all) {
		this.all = all;
	}
	
	private void merge() {
		this.all = this.title + " " + this.body;
	}

}
