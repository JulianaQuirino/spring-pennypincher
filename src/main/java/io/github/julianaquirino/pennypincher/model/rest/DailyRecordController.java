package io.github.julianaquirino.pennypincher.model.rest;

import io.github.julianaquirino.pennypincher.model.entity.DailyRecord;
import io.github.julianaquirino.pennypincher.model.entity.AppUser;
import io.github.julianaquirino.pennypincher.model.repository.DailyRecordRepository;
import io.github.julianaquirino.pennypincher.model.repository.AppUserRepository;
import io.github.julianaquirino.pennypincher.model.rest.dto.DailyRecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/dailyRecords")
public class DailyRecordController {

    private final DailyRecordRepository repository;
    private final AppUserRepository appUserRepository;

    @Autowired
    private DailyRecordController(DailyRecordRepository repository, AppUserRepository appUserRepository){
        this.repository = repository;
        this.appUserRepository = appUserRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DailyRecord save(@RequestBody @Valid DailyRecordDTO dailyRecordDTO) {
        String usernameAppUser = dailyRecordDTO.getUsernameAppUser();
        LocalDate date;
        try {
            date = LocalDate.parse(dailyRecordDTO.getDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data incorreta! Favor utilizar o formato dd/mm/aaaa");
        }

        AppUser appUser =
                this.appUserRepository.findByUsername(usernameAppUser)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário inexistente"));
        List<DailyRecord> currentDailyRecord = this.repository.existsByDateAndUser(date, appUser.getUsername());
        if (currentDailyRecord != null && currentDailyRecord.size() > 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de lançamento já existe!");
        }
        DailyRecord dailyRecord = new DailyRecord();
        dailyRecord.setDate(date);
        dailyRecord.setAppUser(appUser);

        return repository.save(dailyRecord);
    }

    @GetMapping("{id}")
    public DailyRecordDTO findById(@PathVariable Integer id){
        DailyRecord dailyRecord = repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dia de lançamento não encontrado"));

        DailyRecordDTO dailyRecordDTO = new DailyRecordDTO();
        if (dailyRecord != null){
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String date = dailyRecord.getDate().format(formatters);

            dailyRecordDTO.setId(dailyRecord.getId());
            dailyRecordDTO.setDate(date);
            dailyRecordDTO.setUsernameAppUser(dailyRecord.getAppUser().getUsername());
        }
        System.out.println(dailyRecordDTO);
        return dailyRecordDTO;
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Integer id) {
        this.repository
                .findById(id)
                .map(dailyRecord -> {
                    repository.delete(dailyRecord);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dia de lançamento não encontrado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update (@PathVariable Integer id, @RequestBody DailyRecordDTO updatedDailyRecordDTO) {
        LocalDate date = LocalDate.parse(updatedDailyRecordDTO.getDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        List<DailyRecord> currentDailyRecord = this.repository.existsByDateAndUser(date, updatedDailyRecordDTO.getUsernameAppUser());

        if (currentDailyRecord != null && currentDailyRecord.size() > 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data de lançamento já existe!");
        }

        this.repository
                .findById(id)
                .map(dailyRecord -> {
                    dailyRecord.setDate(date);
                    this.repository.save(dailyRecord);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dia de lançamento não encontrado"));
    }

    @GetMapping("{username}")
    @RequestMapping("/dailyRecordsByUsername")
    public List<DailyRecordDTO> getAllByUsername(@RequestParam(value = "username", required = true) String username){
        List<DailyRecord> dailyRecords = repository.findAllByUsernameOrderByDateAsc(username);
        List<DailyRecordDTO> dailyRecordsDTO = new ArrayList<>();
        for(DailyRecord dailyRecord : dailyRecords){
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String date = dailyRecord.getDate().format(formatters);

            DailyRecordDTO dailyRecordDTO = new DailyRecordDTO();
            dailyRecordDTO.setId(dailyRecord.getId());
            dailyRecordDTO.setDate(date);
            BigDecimal debits = new BigDecimal(repository.totalDebitById(username, dailyRecord.getId()));
            dailyRecordDTO.setDebits(debits.toString());
            BigDecimal credits = new BigDecimal(repository.totalCreditById(username, dailyRecord.getId()));
            dailyRecordDTO.setCredits(credits.toString());
            dailyRecordDTO.setUsernameAppUser(dailyRecord.getAppUser().getUsername());
            dailyRecordsDTO.add(dailyRecordDTO);
        }
        return dailyRecordsDTO;
    }

}
