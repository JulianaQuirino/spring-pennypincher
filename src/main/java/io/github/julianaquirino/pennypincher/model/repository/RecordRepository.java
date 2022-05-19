package io.github.julianaquirino.pennypincher.model.repository;

import io.github.julianaquirino.pennypincher.model.entity.Category;
import io.github.julianaquirino.pennypincher.model.entity.Record;
import io.github.julianaquirino.pennypincher.model.rest.dto.ChartSubcategoriesDTO;
import io.github.julianaquirino.pennypincher.model.rest.dto.RecordsReportDTO;
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

    @Query(" select new io.github.julianaquirino.pennypincher.model.rest.dto.RecordsReportDTO(d.date, c.name, sc.name, " +
            " c.categoryType, r.description, r.value, a.name, p.name) " +
            " from Record r " +
            " join r.account a " +
            " left join r.project p " +
            " join r.category c " +
            " left join r.subcategory sc " +
            " join r.dailyRecord d " +
            " join c.appUser u " +
            " where (c.id = :categoryId or :categoryId is null) " +
            " and (month(d.date) = :month or :month is null) " +
            " and (year(d.date) = :year or :year is null) " +
            " and upper (u.username) like upper ( :username )" +
            " order by d.date, c.name, r.description ")
    List<RecordsReportDTO> findRecordsByTypeIdMonthYearUsername(@Param("categoryId") Integer categoryId,
                                                                        @Param("month") Integer month,
                                                                        @Param("year") Integer year,
                                                                        @Param("username") String username);

}
