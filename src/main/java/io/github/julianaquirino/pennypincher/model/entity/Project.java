package io.github.julianaquirino.pennypincher.model.entity;

import io.github.julianaquirino.pennypincher.model.util.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project", schema = "public")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "{campo.nome.obrigatorio}")
    @Column(nullable = false)
    private String name;

    @NotEmpty(message = "{campo.descricao.obrigatorio}")
    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_app_user")
    private AppUser appUser;


}