package de.dbis.microlearn.client.model;

public class Tagmap {
	private final int noteId; 
	private final String tagName; 
	
	public Tagmap(int noteId, String tagName){
		this.noteId = noteId;
		this.tagName = tagName;
	}

	public int getNoteId() {
		return noteId;
	}

	public String getTagName() {
		return tagName;
	}

}
