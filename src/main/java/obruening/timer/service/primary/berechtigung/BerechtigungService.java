package obruening.timer.service.primary.berechtigung;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import obruening.timer.model.primary.berechtigung.Antragsteller;
import obruening.timer.model.primary.berechtigung.Berechtigung;
import obruening.timer.model.primary.berechtigung.Profil;
import obruening.timer.repository.primary.berechtigung.BerechtigungRepository;

@Service
public class BerechtigungService {

    @Autowired
    private BerechtigungRepository berechtigungRepository;

    @Transactional("primaryTransactionManager")
    public Berechtigung save(Berechtigung berechtigung) {
        return berechtigungRepository.save(berechtigung);
    }
    
    @Transactional("primaryTransactionManager")
    public List<Berechtigung> findAll() {
        return berechtigungRepository.findAll();
    }
    
    @Transactional("primaryTransactionManager")
    public Optional<Berechtigung> findById(Long id) {
        return berechtigungRepository.findById(id);
    }
    
    @Transactional("primaryTransactionManager")
    public List<String> findUserVerantwortliche(DelegateExecution execution) {
        Berechtigung berechtigung = (Berechtigung)execution.getVariable("berechtigung");
        
        return findById(berechtigung.getId())
            .orElseThrow(() -> new RuntimeException("berechtigung not found"))
            .getAntragstellerList()
            .stream()
            .map(Antragsteller::getVerantwortlicher)
            .distinct()
            .collect(Collectors.toList());
    }
    
    @Transactional("primaryTransactionManager")
    public List<String> findProfilVerantwortliche(DelegateExecution execution) {
        Berechtigung berechtigung = (Berechtigung)execution.getVariable("berechtigung");
        
        berechtigung = findById(berechtigung.getId()).orElseThrow(() -> new RuntimeException("berechtigung not found"));
        
        //prüfen, ob es noch antragsteller gibt, für die etwas beanttragt werden soll.
        if (berechtigung.getAntragstellerList().stream().filter(Antragsteller::isGenehmigt).count() == 0) {
            return Collections.emptyList();
        }

        return findById(berechtigung.getId())
            .orElseThrow(() -> new RuntimeException("berechtigung not found"))
            .getProfilList()
            .stream()
            .map(Profil::getVerantwortlicher)
            .distinct()
            .collect(Collectors.toList());
    }
}
