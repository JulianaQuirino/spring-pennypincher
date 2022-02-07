package io.github.julianaquirino.pennypincher.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "record", schema = "public")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="daily_record_id")
    @JsonBackReference
    private DailyRecord dailyRecord;

    @ManyToOne
    @JoinColumn(name = "id_category")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "id_subcategory")
    private Subcategory subcategory;

    @NotEmpty(message = "{campo.descricao.obrigatorio}")
    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "id_account")
    private Account account;

    @Column
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "id_project")
    private Project project;

    @Column
    @Lob
    private byte[] foto;

}
