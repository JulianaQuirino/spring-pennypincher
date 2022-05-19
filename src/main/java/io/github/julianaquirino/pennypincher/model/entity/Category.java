package io.github.julianaquirino.pennypincher.model.entity;

import io.github.julianaquirino.pennypincher.model.util.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category", schema = "public")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "{campo.nome.obrigatorio}")
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "enum('D','C')")
    private CategoryType categoryType;

    @ManyToOne
    @JoinColumn(name = "id_app_user")
    private AppUser appUser;

}
