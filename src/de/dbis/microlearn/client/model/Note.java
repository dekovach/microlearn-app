package de.dbis.microlearn.client.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

public class Note {
	private int noteId; 
	private String noteTitle; 
	private List<String> clips;
	private List<String> tags;
	
	public Note() {
		
	}
	
	public Note(int noteId, String noteTitle, List<String> clips, List<String> tags){
		this.noteId = noteId;
		this.noteTitle = noteTitle;
		this.clips = clips;
		this.tags = tags;
	}
	public Note(int noteId, String noteTitle, String[] clips, String[] tags){
		this.noteId = noteId;
		this.noteTitle = noteTitle;
		this.clips = Arrays.asList(clips);
		this.tags = Arrays.asList(tags);
	}

	public int getNoteId() {
		return noteId;
	}

	public String getNoteTitle() {
		return noteTitle;
	}

	public String[] getClips() {
		return (String[]) clips.toArray();
	}
	
	public String[] getTags() {
		return (String[]) tags.toArray();
	}
	
	public String getTagsAsString() {
		StringBuilder tagsString = new StringBuilder();
		
		for(String s : tags) {
			tagsString.append(s).append(", ");
		}
		
		return tagsString.toString();
	}

	public void setTitle(String title) {
		this.noteTitle = title;
	}

	public String getClipsAsHtml() {
		StringBuilder htmlContent = new StringBuilder();
		String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
		String wrapperStart = "<div>";
		String wrapperEnd = "</div>";

		htmlContent.append(header);
		for (String s : clips) {
			htmlContent.append(wrapperStart).append(s).append(wrapperEnd);
		}
		
		try {
			return URLEncoder.encode(htmlContent.toString(),"utf-8").replaceAll("\\+"," ");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void setClips(List<String> clips) {
		this.clips = clips;
	}

	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}
	
	public void setTags(List<String> tags) {
		this.tags = tags;
	}

}
