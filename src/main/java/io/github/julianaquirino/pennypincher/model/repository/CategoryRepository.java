package io.github.julianaquirino.pennypincher.model.repository;

import io.github.julianaquirino.pennypincher.model.entity.Category;
import io.github.julianaquirino.pennypincher.model.rest.dto.ChartCategoriesDTO;
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

    @Query(" select c from Category c " +
            " join c.appUser u " +
            " where u.username like ( :username )" +
            " and upper ( c.categoryType ) like upper ( :categoryType ) " +
            " and upper ( c.name ) like upper ( :name ) " +
            " order by c.name ")
    List<Category> findCategoryByUsernameTypeAndName(@Param("username") String username,
                                                     @Param("categoryType") String categoryType,
                                                     @Param("name") String name);

    @Query(" select new io.github.julianaquirino.pennypincher.model.rest.dto.ChartCategoriesDTO(month(d.date), year(d.date), c.name, sum(r.value)) " +
            " from Record r " +
            " join r.category c " +
            " join r.dailyRecord d " +
            " join c.appUser u " +
            " where upper ( c.categoryType ) like upper ( :categoryType ) " +
            " and month(d.date) = :month " +
            " and year(d.date) = :year " +
            " and upper (u.username) like upper ( :username )" +
            " group by month(d.date), year(d.date), c.name " +
            " order by c.name ")
    List<ChartCategoriesDTO> findAllByTypeMonthYearUsername(@Param("categoryType") String categoryType,
                                                            @Param("month") Integer month,
                                                            @Param("year") Integer year,
                                                            @Param("username") String username);


}
