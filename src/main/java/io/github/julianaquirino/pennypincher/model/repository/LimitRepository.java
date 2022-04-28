package io.github.julianaquirino.pennypincher.model.repository;

import io.github.julianaquirino.pennypincher.model.entity.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LimitRepository extends JpaRepository<Limit, Integer> {
    @Query(" select l from Limit l " +
            " join l.category c " +
            " join c.appUser u " +
            " where u.username like ( :username )" +
            " order by l.year, l.month ")
    List<Limit> findAllByUsernameOrderByYearMonthAsc(@Param("username") String username);
}
