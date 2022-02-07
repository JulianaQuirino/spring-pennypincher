package io.github.julianaquirino.pennypincher.model.repository;

        import io.github.julianaquirino.pennypincher.model.entity.Account;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.data.jpa.repository.Query;
        import org.springframework.data.repository.query.Param;

        import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query(" select a from Account a " +
            " join a.appUser u " +
            " where u.username like ( :username )" +
            " order by a.name ")
    List<Account> findAllByUsernameOrderByNameAsc(@Param("username") String username);
}

