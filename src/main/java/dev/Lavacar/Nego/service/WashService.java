package dev.Lavacar.Nego.service;

import dev.Lavacar.Nego.dto.WashDTO;
import dev.Lavacar.Nego.model.Wash;
import dev.Lavacar.Nego.repository.WashRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WashService {

    private final WashRepository washRepository;

    public WashService(WashRepository washRepository) {
        this.washRepository = washRepository;
    }

    //Get
    public List<Wash> getWash() {return washRepository.findAll();}

    //Post
    public Wash saveWash(Wash wash) {return washRepository.save(wash);}

    //Put
    public Wash updateWash(Long id, Wash updateWash) {
        Wash existingWash = washRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wash not found with id: " + id));

        existingWash.setClient(updateWash.getClient());
        existingWash.setDate(updateWash.getDate());
        existingWash.setValueWash(updateWash.getValueWash());
        existingWash.setDescription(updateWash.getDescription());

        return washRepository.save(existingWash);
    }

    //Delete
    public void deleteWash(Long id) {washRepository.deleteById(id);}

    public Wash findById(Long id) {
        return washRepository.findById(id).orElseThrow(() -> new RuntimeException("Lavagem não encontrada"));
    }
}
