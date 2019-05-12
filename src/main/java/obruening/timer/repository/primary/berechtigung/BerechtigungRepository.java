package obruening.timer.repository.primary.berechtigung;

import org.springframework.data.jpa.repository.JpaRepository;

import obruening.timer.model.primary.berechtigung.Berechtigung;

public interface BerechtigungRepository extends JpaRepository<Berechtigung, Long> {
}