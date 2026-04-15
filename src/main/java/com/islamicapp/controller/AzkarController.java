package com.islamicapp.controller;

import com.islamicapp.model.Azkar;
import com.islamicapp.service.AzkarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/azkar")
@CrossOrigin(origins = "*")
public class AzkarController {

    @Autowired
    private AzkarService azkarService;

    @GetMapping
    public List<Azkar> getAllAzkar() {
        return azkarService.getAllAzkar();
    }

    @GetMapping("/{category}")
    public Azkar getAzkarByCategory(@PathVariable String category) {
        return azkarService.getAzkarByCategory(category);
    }
}