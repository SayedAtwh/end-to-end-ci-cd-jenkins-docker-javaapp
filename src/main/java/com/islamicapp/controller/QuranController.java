package com.islamicapp.controller;

import com.islamicapp.model.Surah;
import com.islamicapp.service.QuranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quran")
@CrossOrigin(origins = "*")
public class QuranController {

    @Autowired
    private QuranService quranService;

    @GetMapping("/surahs")
    public List<Surah> getAllSurahs() {
        return quranService.getAllSurahs();
    }

    @GetMapping("/surah/{number}")
    public Surah getSurah(@PathVariable int number) {
        return quranService.getSurahByNumber(number);
    }
}