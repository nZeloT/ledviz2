package com.nzelot.ledviz2.sound.meta;

import java.awt.image.BufferedImage;

public class METAData {

	private final String fileName;

	private String title;
	private String artist;
	private String album;

	private BufferedImage albumCover;

	public METAData(String fileName) {
		super();
		this.fileName = fileName;
		title = "";
		artist = "";
		album = "";
	}

	public METAData(String fileName, String title, String artist, String album, BufferedImage albumCover) {
		super();
		this.fileName = fileName;
		this.title = title;
		this.artist = artist;
		this.album = album;
		this.albumCover = albumCover;
	}
	
	public METAData(METAData d){
		this(d.getFileName(), d.getTitle(), d.getArtist(), d.getAlbum(), d.getAlbumCover());
	}

	public String getFileName() {
		return fileName;
	}

	public String getTitle() {
		return title;
	}

	public String getArtist() {
		return artist;
	}

	public String getAlbum() {
		return album;
	}

	public BufferedImage getAlbumCover() {
		return albumCover;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public void setAlbumCover(BufferedImage albumCover) {
		this.albumCover = albumCover;
	}

	@Override
	public String toString() {
		return "METAData [fileName=" + fileName + ", title=" + title
				+ ", artist=" + artist + ", album=" + album + ", albumCover="
				+ albumCover + "]";
	}
}
