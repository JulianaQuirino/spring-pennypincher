package io.github.julianaquirino.pennypincher.model.repository;

import io.github.julianaquirino.pennypincher.model.entity.Category;
import io.github.julianaquirino.pennypincher.model.entity.Subcategory;
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
}
