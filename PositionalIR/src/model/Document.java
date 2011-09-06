package model;

public class Document {
	
	private String newid;
	private String date;
	private String[] places;
	private String title;
	private String body;
	private String all;

	public Document() {}
	
	public Document(String newid, String date, String[] places, String title,
			String body) {
		this.newid = newid;
		this.date = date;
		this.places = places;
		this.title = title;
		this.body = body;
	}

	public String getNewid() {
		return newid;
	}

	public void setNewid(String newid) {
		this.newid = newid;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String[] getPlaces() {
		return places;
	}

	public void setPlaces(String[] places) {
		this.places = places;
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
		this.all = this.date;
		for (String place : places) {
			this.all = this.all + " " + place;
		}
		this.all = this.all + " " + this.title + " " + this.body;
	}

}
