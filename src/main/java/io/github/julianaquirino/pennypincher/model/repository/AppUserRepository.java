package io.github.julianaquirino.pennypincher.model.repository;

import io.github.julianaquirino.pennypincher.model.entity.AppUser;
import io.github.julianaquirino.pennypincher.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {

    @Query(" select a from AppUser a " +
            " where a.username like ( :username )")
    Optional<AppUser> findByUsername(@Param("username") String username);

    // Query method. Mesmo que -> select count(*) > 0 from user where username = :login
    boolean existsByUsername(String username);

    @Query(" select a from AppUser a " +
            " order by a.username ")
    List<AppUser> findAllOrderByUsernameAsc();
}
