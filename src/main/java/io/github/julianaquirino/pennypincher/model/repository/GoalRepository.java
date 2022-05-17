package io.github.julianaquirino.pennypincher.model.repository;

        import io.github.julianaquirino.pennypincher.model.entity.Goal;
        import io.github.julianaquirino.pennypincher.model.rest.dto.ChartCategoriesDTO;
        import io.github.julianaquirino.pennypincher.model.rest.dto.ChartGoalsDTO;
        import io.github.julianaquirino.pennypincher.model.rest.dto.GoalDTO;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.data.repository.query.Param;

        import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Integer> {

    @Query(" select new io.github.julianaquirino.pennypincher.model.rest.dto.GoalDTO(g.id, g.name, g.value, u.username) from Goal g " +
            " join g.appUser u " +
            " where u.username like ( :username )" +
            " order by g.name ")
    List<GoalDTO> findAllByUsernameOrderByNameAsc(@Param("username") String username);

    @Query(" select new io.github.julianaquirino.pennypincher.model.rest.dto.ChartGoalsDTO(g.name, g.value, sum(cr.value)) " +
            " from Goal g " +
            " join g.moneyTransfers m " +
            " join m.creditRecord cr " +
            " join g.appUser u " +
            " where upper (u.username) like upper ( :username )" +
            " group by g.name, g.value  " +
            " order by g.name ")
    List<ChartGoalsDTO> findGoalsStatusByUsername(@Param("username") String username);

}

