package br.com.fiap.knowball.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import br.com.fiap.knowball.model.Referee;
import br.com.fiap.knowball.model.RefereeStatusType;
import br.com.fiap.knowball.repository.RefereeRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class RefereeService {
    
    private final RefereeRepository refereeRepository;

    public RefereeService(RefereeRepository refereeRepository) {
        this.refereeRepository = refereeRepository;
    }

    public List<Referee> findAll() {
        return refereeRepository.findAll();
    }

    @SuppressWarnings("null")
    public Referee findById(Long id) {
        return refereeRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Arbitro não encontrado com id " + id));
    }

    @SuppressWarnings("null")
    public Referee save(Referee referee) {
        return refereeRepository.save(referee);
    }

    @SuppressWarnings("null")
    public Referee update(Long id, Referee updatedReferee) {
        Referee referee = findById(id);
        BeanUtils.copyProperties(updatedReferee, referee, "id");
        return refereeRepository.save(referee);
    }

    @SuppressWarnings("null")
    public void destroy(Long id) {
        refereeRepository.deleteById(id);
    }

    public void suspendReferee(Long refereeId) {
        Referee referee = findById(refereeId);
        referee.setStatus(RefereeStatusType.SUSPENDED);
        refereeRepository.save(referee);
    }
}
