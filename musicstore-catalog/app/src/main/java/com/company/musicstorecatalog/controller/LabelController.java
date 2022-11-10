package com.company.musicstorecatalog.controller;

import com.company.musicstorecatalog.model.Label;
import com.company.musicstorecatalog.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/label")
public class LabelController {
    @Autowired
    LabelRepository labelRepository;

    @GetMapping()
    public List<Label> getLabels() {
        List<Label> labelList = labelRepository.findAll();
        if (labelList.isEmpty() || labelList == null) {
            throw new IllegalArgumentException("Labels data is empty!");
        }
        return labelList; }

    @GetMapping("/{id}")
    public Label getLabelById(@PathVariable Integer id) {
        Optional<Label> returnVal = labelRepository.findById(id);
        if (returnVal.isPresent()) {
            return returnVal.get();
        } else {
            throw new IllegalArgumentException("No label was found with Id: " + id);
        }
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Label addLabel(@RequestBody @Valid Label label) {
        if (label==null) throw new IllegalArgumentException("No Label data is added! Label object is null!");
        return labelRepository.save(label);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateLabel(@RequestBody @Valid Label label) {
        // validate incoming Label data
        if (label==null)
            throw new IllegalArgumentException("No Label data is passed! Label object is null!");

        //make sure the label exists. and if not, throw exception...
        if (label.getLabelId()==null)
            throw new IllegalArgumentException("No such label to update.");

        labelRepository.save(label);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLabel(@PathVariable Integer id) {
        Optional<Label> label = labelRepository.findById(id);
        if(label.isPresent()) {
            labelRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("No label was found with Id: " + id);
        }
    }
}
