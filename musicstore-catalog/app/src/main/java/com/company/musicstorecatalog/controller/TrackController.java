package com.company.musicstorecatalog.controller;

import com.company.musicstorecatalog.model.Track;
import com.company.musicstorecatalog.repository.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/track")
public class TrackController {
    @Autowired
    TrackRepository trackRepository;

    @GetMapping()
    public List<Track> getTracks() {
        List<Track> trackList = trackRepository.findAll();
        if (trackList.isEmpty() || trackList == null) {
            throw new IllegalArgumentException("Tracks data is empty!");
        }
        return trackList; }

    @GetMapping("/{id}")
    public Track getTrackById(@PathVariable Integer id) {
        Optional<Track> returnVal = trackRepository.findById(id);
        if (returnVal.isPresent()) {
            return returnVal.get();
        } else {
            throw new IllegalArgumentException("No track was found with Id: " + id);
        }
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Track addTrack(@RequestBody @Valid Track track) {
        if (track==null) throw new IllegalArgumentException("No Track data is added! Track object is null!");
        return trackRepository.save(track);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTrack(@RequestBody @Valid Track track) {
        // validate incoming Track data
        if (track==null)
            throw new IllegalArgumentException("No Track data is passed! Track object is null!");

        //make sure the track exists. and if not, throw exception...
        if (track.getTrackId()==null)
            throw new IllegalArgumentException("No such track to update.");

        trackRepository.save(track);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTrack(@PathVariable Integer id) {
        Optional<Track> track = trackRepository.findById(id);
        if(track.isPresent()) {
            trackRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("No track was found with Id: " + id);
        }
    }}
