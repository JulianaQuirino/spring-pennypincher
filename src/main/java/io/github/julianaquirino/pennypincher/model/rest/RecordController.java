package io.github.julianaquirino.pennypincher.model.rest;

import io.github.julianaquirino.pennypincher.model.entity.*;
import io.github.julianaquirino.pennypincher.model.repository.*;
import io.github.julianaquirino.pennypincher.model.rest.dto.RecordDTO;
import io.github.julianaquirino.pennypincher.model.rest.dto.RecordListDTO;
import io.github.julianaquirino.pennypincher.model.util.BigDecimalConverter;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Part;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/records")
public class RecordController {

    private final RecordRepository repository;
    private final AppUserRepository appUserRepository;
    private final DailyRecordRepository dailyRecordRepository;
    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final AccountRepository accountRepository;
    private final ProjectRepository projectRepository;
    private final BigDecimalConverter bigDecimalConverter;

    @Autowired
    private RecordController(RecordRepository repository, AppUserRepository appUserRepository, DailyRecordRepository dailyRecordRepository, CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository, AccountRepository accountRepository, ProjectRepository projectRepository, BigDecimalConverter bigDecimalConverter){
        this.repository = repository;
        this.appUserRepository = appUserRepository;
        this.dailyRecordRepository = dailyRecordRepository;
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.accountRepository = accountRepository;
        this.projectRepository = projectRepository;
        this.bigDecimalConverter = bigDecimalConverter;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Record save(@RequestBody @Valid RecordDTO recordDTO) {

        DailyRecord dailyRecord =
                this.dailyRecordRepository.findById(recordDTO.getDailyRecordId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data inexistente"));

        Category category =
                this.categoryRepository.findById(recordDTO.getCategoryId())
                        .orElseThrow(() -> new ResponseStatusException((HttpStatus.BAD_REQUEST), "Categoria inexistente"));

        Subcategory subcategory = null;
        if (recordDTO.getSubcategoryId() != null){
            subcategory =
                    this.subcategoryRepository.findById(recordDTO.getSubcategoryId())
                            .orElseThrow(() -> new ResponseStatusException((HttpStatus.BAD_REQUEST), "Subcategoria inexistente"));
        }

        Account account =
                this.accountRepository.findById(recordDTO.getAccountId())
                        .orElseThrow(() -> new ResponseStatusException((HttpStatus.BAD_REQUEST), "Conta inexistente"));

        Project project = null;
        if (recordDTO.getProjectId() != null){
            project =
                    this.projectRepository.findById(recordDTO.getProjectId())
                            .orElseThrow(() -> new ResponseStatusException((HttpStatus.BAD_REQUEST), "Projeto inexistente"));
        }

        Record record = new Record();
        record.setDailyRecord(dailyRecord);
        record.setAccount(account);
        record.setCategory(category);
        record.setSubcategory(subcategory);
        record.setProject(project);
        record.setDescription(recordDTO.getDescription());
        record.setValue(bigDecimalConverter.converter(recordDTO.getValue()));

        return repository.save(record);
    }

    @GetMapping("{id}")
    public RecordDTO findById(@PathVariable Integer id){
        Record record = repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lançamento não encontrado"));

        RecordDTO recordDTO = new RecordDTO();
        recordDTO.setId(record.getId());
        recordDTO.setDailyRecordId(record.getDailyRecord().getId());
        recordDTO.setCategoryId(record.getCategory().getId());
        recordDTO.setType(record.getCategory().getCategoryType().toString());
        recordDTO.setSubcategoryId(record.getSubcategory() != null ? record.getSubcategory().getId() : null);
        String value = record.getValue().toString();
        value = value.replace(".", ",");
        recordDTO.setValue(value);
        recordDTO.setDescription(record.getDescription());
        recordDTO.setAccountId(record.getAccount().getId());
        recordDTO.setProjectId(record.getProject() != null ? record.getProject().getId() : null);

        return recordDTO;
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Integer id) {
        this.repository
                .findById(id)
                .map(record -> {
                    repository.delete(record);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lançamento não encontrado"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update (@PathVariable Integer id, @RequestBody RecordDTO updatedRecordDTO) {
        this.repository
                .findById(id)
                .map(record -> {

                    Category category =
                            this.categoryRepository.findById(updatedRecordDTO.getCategoryId())
                                    .orElseThrow(() -> new ResponseStatusException((HttpStatus.BAD_REQUEST), "Categoria inexistente"));

                    Subcategory subcategory = null;
                    if (updatedRecordDTO.getSubcategoryId() != null){
                        subcategory =
                                this.subcategoryRepository.findById(updatedRecordDTO.getSubcategoryId())
                                        .orElseThrow(() -> new ResponseStatusException((HttpStatus.BAD_REQUEST), "Subcategoria inexistente"));
                    }

                    Account account =
                            this.accountRepository.findById(updatedRecordDTO.getAccountId())
                                    .orElseThrow(() -> new ResponseStatusException((HttpStatus.BAD_REQUEST), "Conta inexistente"));

                    Project project = null;
                    if (updatedRecordDTO.getProjectId() != null){
                        project =
                                this.projectRepository.findById(updatedRecordDTO.getProjectId())
                                        .orElseThrow(() -> new ResponseStatusException((HttpStatus.BAD_REQUEST), "Projeto inexistente"));
                    }

                    record.setAccount(account);
                    record.setCategory(category);
                    record.setSubcategory(subcategory);
                    record.setProject(project);
                    record.setDescription(updatedRecordDTO.getDescription());
                    record.setValue(bigDecimalConverter.converter(updatedRecordDTO.getValue()));
                    System.out.println(record);
                    this.repository.save(record);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lançamento não encontrado"));
    }

    // dominio/api/records/1/foto
    @PutMapping("{id}/foto")
    public byte[] addPhoto(@PathVariable Integer id,
                           @RequestParam("foto") Part arquivo){
        Optional<Record> record = repository.findById(id);
        return record.map( c -> {
            try{
                InputStream is = arquivo.getInputStream();
                byte[] bytes = new byte[(int) arquivo.getSize()];
                IOUtils.readFully(is, bytes);
                c.setFoto(bytes);
                repository.save(c);
                is.close();
                return bytes;
            } catch(IOException e){
                return null;
            }
        }).orElse(null);
    }

    /*@GetMapping("{idDailyRecord}")
    @RequestMapping("/recordsByDailyRecord")
    public List<Record> getAllByDailyRecord(@RequestParam(value = "idDailyRecord", required = true) Integer idDailyRecord){
        return repository.findAllByDailyRecordOrderByIdAsc(idDailyRecord);
    }*/

    @GetMapping("{idDailyRecord}")
    @RequestMapping("/recordsByDailyRecord")
    public List<RecordListDTO> getAllByDailyRecord(@RequestParam(value = "idDailyRecord", required = true) Integer idDailyRecord){
        List<Record> records = repository.findAllByDailyRecordOrderByIdAsc(idDailyRecord);
        List<RecordListDTO> recordsListDTO = new ArrayList<>();
        for (Record record : records){
            RecordListDTO recordList = new RecordListDTO();
            recordList.setId(record.getId());
            recordList.setAccountName(record.getAccount().getName());
            recordList.setCategoryName(record.getCategory().getName());
            recordList.setCategoryType(record.getCategory().getCategoryType().toString());
            recordList.setSubcategoryName(record.getSubcategory() != null ? record.getSubcategory().getName() : null);
            recordList.setValue(record.getValue().toString());
            recordList.setDescription(record.getDescription());
            recordList.setProjectName(record.getProject() != null ? record.getProject().getName() : null);
            recordsListDTO.add(recordList);
        }

        return recordsListDTO;
    }


    @GetMapping("{username}")
    @RequestMapping("/recordsByUsername")
    public List<Record> getAllByUsername(@RequestParam(value = "username", required = true) String username){
        return repository.findAllByUsernameOrderByNameAsc(username);
    }

}

