package pt.ul.fc.css.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ul.fc.css.example.demo.entities.Theme;

public interface ThemeRepository extends JpaRepository<Theme, Long> {}
