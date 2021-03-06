package io.github.julianaquirino.pennypincher.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.github.julianaquirino.pennypincher.model.util.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account", schema = "public")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "{campo.nome.obrigatorio}")
    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "id_app_user")
    private AppUser appUser;

    @OneToMany(targetEntity=Record.class, mappedBy="account", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Record> records = new ArrayList<>();

}

