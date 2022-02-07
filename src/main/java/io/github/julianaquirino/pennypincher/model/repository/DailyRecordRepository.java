package io.github.julianaquirino.pennypincher.model.repository;

import io.github.julianaquirino.pennypincher.model.entity.Category;
import io.github.julianaquirino.pennypincher.model.entity.DailyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface DailyRecordRepository extends JpaRepository<DailyRecord, Integer> {

    @Query(" select d from DailyRecord d " +
            " join d.appUser u " +
            " where u.username like ( :username )" +
            " order by d.date ")
    List<DailyRecord> findAllByUsernameOrderByDateAsc(@Param("username") String username);

    @Query(" select d from DailyRecord d " +
            " join d.appUser u " +
            " where u.username like ( :username )" +
            " and d.date = :date " +
            " order by d.date ")
    List<DailyRecord> existsByDateAndUser(@Param("date") LocalDate date, @Param("username") String username);

    @Query(" select coalesce(sum(r.value), 0) from Record r " +
            " join r.dailyRecord d " +
            " join r.category c " +
            " join d.appUser u " +
            " where u.username like ( :username )" +
            " and d.id = :idDailyRecord " +
            " and c.categoryType = 'D' ")
    double totalDebitById(@Param("username") String username, @Param("idDailyRecord") Integer idDailyRecord);

    @Query(" select coalesce(sum(r.value), 0) from Record r " +
            " join r.dailyRecord d " +
            " join r.category c " +
            " join d.appUser u " +
            " where u.username like ( :username )" +
            " and d.id = :idDailyRecord " +
            " and c.categoryType = 'C' ")
    double totalCreditById(@Param("username") String username, @Param("idDailyRecord") Integer idDailyRecord);


}
