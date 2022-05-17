package io.github.julianaquirino.pennypincher.model.repository;

import io.github.julianaquirino.pennypincher.model.entity.Category;
import io.github.julianaquirino.pennypincher.model.entity.Subcategory;
import io.github.julianaquirino.pennypincher.model.rest.dto.ChartCategoriesDTO;
import io.github.julianaquirino.pennypincher.model.rest.dto.ChartSubcategoriesDTO;
import io.github.julianaquirino.pennypincher.model.rest.dto.SubcategoryListDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Integer> {

    @Query(" select new io.github.julianaquirino.pennypincher.model.rest.dto.SubcategoryListDTO(sc.id, sc.name, c.name) from Subcategory sc " +
            " join sc.category c " +
            " join c.appUser u " +
            " where u.username like ( :username )" +
            " order by sc.name ")
    List<SubcategoryListDTO> findAllByUsernameOrderByNameAsc(@Param("username") String username);

    @Query(" select new io.github.julianaquirino.pennypincher.model.rest.dto.SubcategoryListDTO(sc.id, sc.name, c.name) from Subcategory sc " +
            " join sc.category c " +
            " where c.id = :idCategory " +
            " order by sc.name ")
    List<SubcategoryListDTO> getSubcategoriesByIdCategory(@Param("idCategory") Integer idCategory);

    @Query(" select new io.github.julianaquirino.pennypincher.model.rest.dto.ChartSubcategoriesDTO(sc.name, sum(r.value)) " +
            " from Record r " +
            " join r.category c " +
            " join r.subcategory sc " +
            " join r.dailyRecord d " +
            " join c.appUser u " +
            " where c.id = :categoryId " +
            " and month(d.date) = :month " +
            " and year(d.date) = :year " +
            " and upper (u.username) like upper ( :username )" +
            " group by sc.name " +
            " order by sc.name ")
    List<ChartSubcategoriesDTO> findSubcategoriesByCategoryMonthYearUsername(@Param("categoryId") Integer categoryId,
                                                                            @Param("month") Integer month,
                                                                            @Param("year") Integer year,
                                                                            @Param("username") String username);

}
