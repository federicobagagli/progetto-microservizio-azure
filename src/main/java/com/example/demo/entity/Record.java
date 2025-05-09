package com.example.demo.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "records")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cd_number")
    private String cdNumber;

    @Column(name = "drawer")
    private String drawer; //cassetto

    @Column(name = "composer_author")
    private String composerAuthor;

    @Column(name = "album_title")
    private String albumTitle;

    @Column(name = "track_title")
    private String trackTitle;

    @Column(name = "ensemble")
    private String ensemble;

    @Column(name = "composition_date")
    @Temporal(TemporalType.DATE)
    private Date compositionDate;

    @Column(name = "performers")
    private String performers;

    @Column(name = "genre")
    private String genre;

    @Column(name = "user")
    private String user;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCdNumber() {
        return cdNumber;
    }

    public void setCdNumber(String cdNumber) {
        this.cdNumber = cdNumber;
    }

    public String getDrawer() {
        return drawer;
    }

    public void setDrawer(String drawer) {
        this.drawer = drawer;
    }

    public String getComposerAuthor() {
        return composerAuthor;
    }

    public void setComposerAuthor(String composerAuthor) {
        this.composerAuthor = composerAuthor;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public String getTrackTitle() {
        return trackTitle;
    }

    public void setTrackTitle(String trackTitle) {
        this.trackTitle = trackTitle;
    }

    public String getEnsemble() {
        return ensemble;
    }

    public void setEnsemble(String ensemble) {
        this.ensemble = ensemble;
    }

    public Date getCompositionDate() {
        return compositionDate;
    }

    public void setCompositionDate(Date compositionDate) {
        this.compositionDate = compositionDate;
    }

    public String getPerformers() {
        return performers;
    }

    public void setPerformers(String performers) {
        this.performers = performers;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
