//package ;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicUserRepository extends JpaRepository<PublicUser, Long> {}