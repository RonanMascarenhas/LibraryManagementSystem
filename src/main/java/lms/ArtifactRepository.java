package lms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ArtifactRepository extends JpaRepository<Artifact, Long> {
    //List<Artifact> findByName(String name);

    Artifact findByName(String name);
}