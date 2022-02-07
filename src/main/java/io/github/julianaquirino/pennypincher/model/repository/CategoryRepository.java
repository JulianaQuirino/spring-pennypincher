package io.github.julianaquirino.pennypincher.model.repository;

import io.github.julianaquirino.pennypincher.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query(" select c from Category c " +
            " join c.appUser u " +
            " where u.username like ( :username )" +
            " and upper ( c.categoryType ) like upper ( :categoryType ) " +
            " order by c.name ")
    List<Category> findByTypeUsername(@Param("categoryType") String categoryType, @Param("username") String username);

    @Query(" select c from Category c " +
            " order by c.name ")
    List<Category> findAllByOrderByNameAsc();

    @Query(" select c from Category c " +
            " join c.appUser u " +
            " where u.username like ( :username )" +
            " order by c.name ")
    List<Category> findAllByUsernameOrderByNameAsc(@Param("username") String username);
}
