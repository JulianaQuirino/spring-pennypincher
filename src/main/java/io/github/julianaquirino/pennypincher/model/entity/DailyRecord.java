package io.github.julianaquirino.pennypincher.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="daily_record", schema = "public", uniqueConstraints={
        @UniqueConstraint(columnNames = {"date", "id_app_user"})
})
public class DailyRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @NotNull(message = "{campo.data.obrigatorio}")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "id_app_user")
    private AppUser appUser;

    @OneToMany(targetEntity=Record.class, mappedBy="dailyRecord", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Record> records = new ArrayList<>();

}
