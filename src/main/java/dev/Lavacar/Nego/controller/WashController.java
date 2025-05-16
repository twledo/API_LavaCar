package dev.Lavacar.Nego.controller;

import dev.Lavacar.Nego.dto.WashDTO;
import dev.Lavacar.Nego.mapper.WashMapper;
import dev.Lavacar.Nego.model.Wash;
import dev.Lavacar.Nego.service.WashService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agendar")
public class WashController {

    private final WashService washService;
    private final WashMapper washMapper;

    public WashController(WashService washService, WashMapper washMapper) {
        this.washService = washService;
        this.washMapper = washMapper;
    }

    @PostMapping
    public ResponseEntity<WashDTO> saveWash(@RequestBody WashDTO washDTO) {
        Wash wash = washMapper.toEntity(washDTO);
        Wash saved = washService.saveWash(wash);
        return ResponseEntity.ok(washMapper.toDTO(saved));
    }

    @GetMapping
    public ResponseEntity<List<WashDTO>> getWash() {
        List<Wash> washes = washService.getWash();
        List<WashDTO> washDTOs = washes.stream()
                .map(washMapper::toDTO)
                .toList();

        return ResponseEntity.ok(washDTOs);
    }


    @PutMapping("/{id}")
    public Wash updateWash(@PathVariable Long id, @RequestBody Wash updateWash) {return washService.updateWash(id, updateWash);}

    @DeleteMapping("/{id}")
    public void deleteWash(@PathVariable Long id) {washService.deleteWash(id);}
}
