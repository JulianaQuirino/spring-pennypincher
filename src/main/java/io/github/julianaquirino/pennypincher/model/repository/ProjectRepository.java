package io.github.julianaquirino.pennypincher.model.repository;

import io.github.julianaquirino.pennypincher.model.entity.Account;
import io.github.julianaquirino.pennypincher.model.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query(" select p from Project p " +
            " join p.appUser u " +
            " where u.username like ( :username )" +
            " order by p.name ")
    List<Project> findAllByUsernameOrderByNameAsc(@Param("username") String username);

}
