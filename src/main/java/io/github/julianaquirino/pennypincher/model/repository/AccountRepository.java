package io.github.julianaquirino.pennypincher.model.repository;

import io.github.julianaquirino.pennypincher.model.entity.Account;
import io.github.julianaquirino.pennypincher.model.rest.dto.AccountDTO;
import io.github.julianaquirino.pennypincher.model.rest.dto.ChartAccountsDTO;
import io.github.julianaquirino.pennypincher.model.rest.dto.ChartGoalsProjectionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query(" select new io.github.julianaquirino.pennypincher.model.rest.dto.AccountDTO(a.id, a.name, a.description, u.username) " +
            " from Account a " +
            " join a.appUser u " +
            " where u.username like ( :username )" +
            " order by a.name ")
    List<AccountDTO> findAllByUsernameOrderByNameAsc(@Param("username") String username);


    @Query(" select new io.github.julianaquirino.pennypincher.model.rest.dto.ChartAccountsDTO(a.name, coalesce(sum(rc.value),0) - coalesce(sum(rd.value),0)) " +
            " from Account a " +
            " left join a.records rc " +
            " join rc.category catCredit on catCredit.categoryType = 'C' " +
            " join rc.dailyRecord drc on (month(drc.date) = :month and year(drc.date) = :year) " +
            " join drc.appUser uc on upper (uc.username) like upper ( :username ) " +
            " left join a.records rd " +
            " join rd.category catDebit on catDebit.categoryType = 'D' " +
            " join rd.dailyRecord drd on (month(drd.date) = :month and year(drd.date) = :year) " +
            " join drd.appUser ud on ud.username like upper ( :username ) " +
            //" where (month(drc.date) = :month or month(drd.date) = :month) " +
            // " and (year(drc.date) = :year or year(drd.date) = :year) " +
            //" and (upper (uc.username) like upper ( :username ) or upper (ud.username) like upper ( :username ))" +
            " group by a.name " +
            " order by a.name ")
    List<ChartAccountsDTO> findAccountsBalanceByMonthYearUsername(@Param("month") Integer month,
                                                                      @Param("year") Integer year,
                                                                      @Param("username") String username);


    @Query(value = "select a.name as accountName,\n" +
            "(select coalesce(sum(rc.value),0)\n" +
            "from record rc\n" +
            "inner join daily_record drc on drc.id = rc.daily_record_id and extract(month from drc.date) = ?1 and extract(year from drc.date) = ?2 \n" +
            "inner join category cac on cac.id = rc.id_category and cac.category_type = 'C'\n" +
            "where a.id = rc.id_account) -\n" +
            "(select coalesce(sum(rd.value),0)  \n" +
            "from record rd\n" +
            "inner join daily_record drd on drd.id = rd.daily_record_id and extract(month from drd.date) = ?1 and extract(year from drd.date) = ?2 \n" +
            "inner join category cad on cad.id = rd.id_category and cad.category_type = 'D'\n" +
            "where a.id = rd.id_account) as balance\n" +
            "from account a\n" +
            "inner join app_user u on a.id_app_user = u.id and upper(u.login) = ?3 ", nativeQuery = true)
    ArrayList<ChartGoalsProjectionDTO> findAccountsBalanceByMonthYearUsernameNative(Integer month,
                                                                                    Integer year,
                                                                                    String username);
}

