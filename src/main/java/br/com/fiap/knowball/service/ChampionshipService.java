package br.com.fiap.knowball.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.knowball.model.Championship;
import br.com.fiap.knowball.repository.ChampionshipRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ChampionshipService {
    
    @Autowired
    private final ChampionshipRepository championshipRepository;

    public ChampionshipService(ChampionshipRepository championshipRepository) {
        this.championshipRepository = championshipRepository;
    }

    public List<Championship> findAll(){
        return championshipRepository.findAll();
    }

    public Championship findById(Long id) {
        return championshipRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Campeonato não encontrado com id " + id));
    }

    public Championship save(Championship championship) {
        return championshipRepository.save(championship);
    }

    public Championship update(Long id, Championship updatedChampionship) {
        Championship championship = findById(id);
        BeanUtils.copyProperties(updatedChampionship, championship, "id");
        return championshipRepository.save(championship);
    }

    public void destroy(Long id) {
        championshipRepository.deleteById(id);
    }
}
