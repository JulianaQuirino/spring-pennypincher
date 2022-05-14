package io.github.julianaquirino.pennypincher.model.repository;

import io.github.julianaquirino.pennypincher.model.entity.MoneyTransfer;
import io.github.julianaquirino.pennypincher.model.rest.dto.MoneyTransferListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MoneyTransferRepository extends JpaRepository<MoneyTransfer, Integer> {

    @Query(" select new io.github.julianaquirino.pennypincher.model.rest.dto.MoneyTransferListDTO (mt.id, mt.date, mt.description, da.name, ca.name, mt.value, g.name) " +
            " from MoneyTransfer mt " +
            " join mt.debitRecord dr " +
            " join dr.account da " +
            " join mt.creditRecord cr " +
            " join cr.account ca " +
            " left join mt.goal g " +
            " join dr.dailyRecord daR " +
            " join daR.appUser u " +
            " where u.username like ( :username )" +
            " order by mt.date ")
    List<MoneyTransferListDTO> findAllByUsernameOrderByNameAsc(@Param("username") String username);

}
