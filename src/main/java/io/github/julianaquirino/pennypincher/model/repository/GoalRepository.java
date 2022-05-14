package io.github.julianaquirino.pennypincher.model.repository;

        import io.github.julianaquirino.pennypincher.model.entity.Goal;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.data.repository.query.Param;

        import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Integer> {
    @Query(" select g from Goal g " +
            " join g.appUser u " +
            " where u.username like ( :username )" +
            " order by g.name ")
    List<Goal> findAllByUsernameOrderByNameAsc(@Param("username") String username);

}

