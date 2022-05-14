package io.github.julianaquirino.pennypincher.model.repository;

import io.github.julianaquirino.pennypincher.model.entity.Account;
import io.github.julianaquirino.pennypincher.model.entity.Project;
import io.github.julianaquirino.pennypincher.model.rest.dto.ChartCategoriesDTO;
import io.github.julianaquirino.pennypincher.model.rest.dto.ChartProjectsDTO;
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

    @Query(" select new io.github.julianaquirino.pennypincher.model.rest.dto.ChartProjectsDTO(month(d.date), year(d.date), sum(r.value)) " +
            " from Record r " +
            " join r.dailyRecord d " +
            " join r.project p " +
            " join p.appUser u " +
            " where p.id = :projectId " +
            " and upper (u.username) like upper ( :username )" +
            " group by month(d.date), year(d.date) " +
            " order by month(d.date), year(d.date) ")
    List<ChartProjectsDTO> findDebitsOfProjectUsername(@Param("projectId") Integer projectId,
                                                            @Param("username") String username);
}
