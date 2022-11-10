package com.company.musicstorecatalog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "track")
public class Track {
    @Id
    @Column(name = "track_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer trackId;
    @NotEmpty(message = "Track title is required.")
    private String title;
    @Column(name = "run_time")
    @NotNull(message = "Run time is required.")
    @Min(value = 1, message = "Run time is supposed to be greater than 0!")
    private Integer runTime;
    @Column(name = "album_id")
    @NotNull(message = "Album ID is required.")
    private Integer albumId;

    public Track() {}

    public Integer getTrackId() {
        return trackId;
    }

    public void setTrackId(Integer trackId) {
        this.trackId = trackId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRunTime() {
        return runTime;
    }

    public void setRunTime(Integer runTime) {
        this.runTime = runTime;
    }

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return Objects.equals(trackId, track.trackId) && Objects.equals(title, track.title) && Objects.equals(runTime, track.runTime) && Objects.equals(albumId, track.albumId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackId, title, runTime, albumId);
    }

    @Override
    public String toString() {
        return "Track{" +
                "trackId=" + trackId +
                ", title='" + title + '\'' +
                ", runTime=" + runTime +
                ", albumId=" + albumId +
                '}';
    }
}
