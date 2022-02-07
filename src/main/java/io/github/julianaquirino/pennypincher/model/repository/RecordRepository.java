package io.github.julianaquirino.pennypincher.model.repository;

import io.github.julianaquirino.pennypincher.model.entity.Category;
import io.github.julianaquirino.pennypincher.model.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Integer> {
    @Query(" select r from Record r " +
            " join r.dailyRecord d " +
            " where d.id = :idDailyRecord" +
            " order by r.id ")
    List<Record> findAllByDailyRecordOrderByIdAsc(@Param("idDailyRecord") Integer idDailyRecord);

    @Query(" select r from Record r " +
            " join r.dailyRecord d " +
            " join d.appUser u " +
            " where u.username like ( :username )" +
            " order by r.description ")
    List<Record> findAllByUsernameOrderByNameAsc(@Param("username") String username);
}
