package lms;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtifactRepo extends JpaRepository<> {
    List<Atrifact> findallbyName(String name);
}