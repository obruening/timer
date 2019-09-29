package obruening.timer.starter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import obruening.timer.model.primary.berechtigung.Antragsteller;
import obruening.timer.model.primary.berechtigung.Berechtigung;
import obruening.timer.model.primary.berechtigung.Profil;
import obruening.timer.service.primary.berechtigung.BerechtigungService;

@Component
public class MapProcessStarter implements ProcessStarter {
    
    @Autowired
    private RuntimeService runtimeService;
    
    @Autowired
    private BerechtigungService berechtigungService;

    @Override
    public Map<String, Object> getMap() {
        
        Map<String, Object> map = new TreeMap<>();
        map.put("tier", "katze");
        map.put("alter", 5L);
        map.put("groesse", 20.4);
        
        Berechtigung berechtigung = new Berechtigung();
        
        List<Antragsteller> antragstellerList = new ArrayList<>();
        
        Antragsteller antragsteller1 = new Antragsteller();
        antragsteller1.setUserId("A001");
        antragsteller1.setVerantwortlicher("VA001");
        antragsteller1.setBerechtigung(berechtigung);
        antragsteller1.setGenehmigt(true);
        antragstellerList.add(antragsteller1);

        Antragsteller antragsteller2 = new Antragsteller();
        antragsteller2.setUserId("A002");
        antragsteller2.setVerantwortlicher("VA002");
        antragsteller2.setBerechtigung(berechtigung);
        antragsteller2.setGenehmigt(true);
        antragstellerList.add(antragsteller2);
        
        berechtigung.setAntragstellerList(antragstellerList);
        
        
        List<Profil> profilList = new ArrayList<>();
        
        Profil profil1 = new Profil();
        profil1.setName("sam");
        profil1.setVerantwortlicher("VP001");
        profil1.setBerechtigung(berechtigung);
        profilList.add(profil1);

        Profil profil2 = new Profil();
        profil2.setName("spf");
        profil2.setVerantwortlicher("VP002");
        profil2.setBerechtigung(berechtigung);
        profilList.add(profil2);
        
        berechtigung.setProfilList(profilList);
        berechtigung.setGenehmigt(true);
        
        map.put("berechtigung", berechtigung);

        return map;
    }

    @Override
    public void start(Map<String, Object> map) {
        
        Berechtigung berechtigung = (Berechtigung)map.get("berechtigung");
        berechtigungService.save(berechtigung);
        
        runtimeService.startProcessInstanceByKey(getKey(), map);
    }

    @Override
    public String getKey() {

        return "map_process";
    }
}
