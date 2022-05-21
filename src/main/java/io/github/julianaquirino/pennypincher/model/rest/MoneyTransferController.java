package io.github.julianaquirino.pennypincher.model.rest;

import io.github.julianaquirino.pennypincher.model.entity.*;
import io.github.julianaquirino.pennypincher.model.repository.*;
import io.github.julianaquirino.pennypincher.model.rest.dto.MoneyTransferDTO;
import io.github.julianaquirino.pennypincher.model.rest.dto.MoneyTransferListDTO;
import io.github.julianaquirino.pennypincher.model.util.BigDecimalConverter;
import io.github.julianaquirino.pennypincher.model.util.CategoryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/moneytransfers")
public class MoneyTransferController {

    private final MoneyTransferRepository repository;
    private final AppUserRepository appUserRepository;
    private final DailyRecordRepository dailyRecordRepository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;
    private final GoalRepository goalRepository;
    private final RecordRepository recordRepository;
    private final BigDecimalConverter bigDecimalConverter;
    private final String strTransferencia = "Transferência";

    @Autowired
    private MoneyTransferController(MoneyTransferRepository repository, AppUserRepository appUserRepository,
                                    DailyRecordRepository dailyRecordRepository, CategoryRepository categoryRepository,
                                    AccountRepository accountRepository, GoalRepository goalRepository,
                                    RecordRepository recordRepository, BigDecimalConverter bigDecimalConverter){
        this.repository = repository;
        this.appUserRepository = appUserRepository;
        this.dailyRecordRepository = dailyRecordRepository;
        this.categoryRepository = categoryRepository;
        this.goalRepository = goalRepository;
        this.accountRepository = accountRepository;
        this.recordRepository = recordRepository;
        this.bigDecimalConverter = bigDecimalConverter;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MoneyTransferDTO save(@RequestBody @Valid MoneyTransferDTO moneyTransferDTO) {

        AppUser appUser =
                this.appUserRepository.findByUsername(moneyTransferDTO.getUsernameAppUser())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário inexistente"));

        LocalDate date;
        try {
            date = LocalDate.parse(moneyTransferDTO.getDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Data incorreta! Favor utilizar o formato dd/mm/aaaa");
        }

        DailyRecord dailyRecord = new DailyRecord();
        List<DailyRecord> currentDailyRecord = this.dailyRecordRepository.existsByDateAndUser(date, appUser.getUsername());
        if (currentDailyRecord != null && currentDailyRecord.size() > 0){
            dailyRecord = currentDailyRecord.get(0);
        } else {
            dailyRecord.setDate(date);
            dailyRecord.setAppUser(appUser);
            dailyRecord = dailyRecordRepository.save(dailyRecord);
        }

        Category debitCategory = new Category();
        List<Category> debitCategories =
                this.categoryRepository.findCategoryByUsernameTypeAndName(moneyTransferDTO.getUsernameAppUser(), CategoryType.D.toString(), strTransferencia);
        if (debitCategories != null && debitCategories.size() > 0){
            debitCategory = debitCategories.get(0);
        } else {
            debitCategory.setName(strTransferencia);
            debitCategory.setAppUser(appUser);
            debitCategory.setCategoryType(CategoryType.D);
            categoryRepository.save(debitCategory);
        }

        Category creditCategory = new Category();
        List<Category> creditCategories =
                this.categoryRepository.findCategoryByUsernameTypeAndName(moneyTransferDTO.getUsernameAppUser(), CategoryType.C.toString(), strTransferencia);
        if (creditCategories != null && creditCategories.size() > 0){
            creditCategory = creditCategories.get(0);
        } else {
            creditCategory.setName(strTransferencia);
            creditCategory.setAppUser(appUser);
            creditCategory.setCategoryType(CategoryType.C);
            categoryRepository.save(creditCategory);
        }

        Account debitAccount =
                this.accountRepository.findById(moneyTransferDTO.getDebitAccountId())
                        .orElseThrow(() -> new ResponseStatusException((HttpStatus.BAD_REQUEST), "Conta de débito inexistente"));

        Account creditAccount =
                this.accountRepository.findById(moneyTransferDTO.getCreditAccountId())
                        .orElseThrow(() -> new ResponseStatusException((HttpStatus.BAD_REQUEST), "Conta de crédito inexistente"));


        Goal goal = null;
        if (moneyTransferDTO.getGoalId() != null){
            goal =
                    this.goalRepository.findById(moneyTransferDTO.getGoalId())
                            .orElseThrow(() -> new ResponseStatusException((HttpStatus.BAD_REQUEST), "Meta inexistente"));
        }

        Record debitRecord = new Record();
        debitRecord.setDailyRecord(dailyRecord);
        debitRecord.setAccount(debitAccount);
        debitRecord.setCategory(debitCategory);
        debitRecord.setDescription(moneyTransferDTO.getDescription());
        debitRecord.setValue(bigDecimalConverter.converter(moneyTransferDTO.getValue()));
        debitRecord = recordRepository.save(debitRecord);

        Record creditRecord = new Record();
        creditRecord.setDailyRecord(dailyRecord);
        creditRecord.setAccount(creditAccount);
        creditRecord.setCategory(creditCategory);
        creditRecord.setDescription(moneyTransferDTO.getDescription());
        creditRecord.setValue(bigDecimalConverter.converter(moneyTransferDTO.getValue()));
        creditRecord = recordRepository.save(creditRecord);

        MoneyTransfer moneyTransfer = new MoneyTransfer();
        moneyTransfer.setDate(date);
        moneyTransfer.setDescription(moneyTransferDTO.getDescription());
        moneyTransfer.setValue(bigDecimalConverter.converter(moneyTransferDTO.getValue()));
        moneyTransfer.setDebitRecord(debitRecord);
        moneyTransfer.setCreditRecord(creditRecord);
        moneyTransfer.setGoal(goal);

        MoneyTransfer savedMoneytransfer = repository.save(moneyTransfer);

        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String moneytransferDate = savedMoneytransfer.getDate().format(formatters);

        String moneyTransferValue = savedMoneytransfer.getValue().toString();
        moneyTransferValue = moneyTransferValue.replace(".", ",");

        MoneyTransferDTO savedMoneytransferDTO = new MoneyTransferDTO();
        savedMoneytransferDTO.setId(savedMoneytransfer.getId());
        savedMoneytransferDTO.setDebitAccountId(savedMoneytransfer.getDebitRecord().getAccount().getId());
        savedMoneytransferDTO.setCreditAccountId(savedMoneytransfer.getCreditRecord().getAccount().getId());
        savedMoneytransferDTO.setDate(moneytransferDate);
        savedMoneytransferDTO.setValue(moneyTransferValue);
        savedMoneytransferDTO.setDescription(moneyTransfer.getDescription());
        savedMoneytransferDTO.setGoalId(moneyTransfer.getGoal() != null ? moneyTransfer.getGoal().getId() : null);
        savedMoneytransferDTO.setUsernameAppUser(savedMoneytransfer.getDebitRecord().getDailyRecord().getAppUser().getUsername());
        return savedMoneytransferDTO;
    }

    @GetMapping("{id}")
    public MoneyTransferDTO findById(@PathVariable Integer id){

        System.out.println("ID: " + id);
        MoneyTransfer moneyTransfer = repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transferência não encontrada"));

        MoneyTransferDTO moneyTransferDTO = new MoneyTransferDTO();

        DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = moneyTransfer.getDate().format(formatters);

        moneyTransferDTO.setId(moneyTransfer.getId());
        moneyTransferDTO.setDate(date);
        moneyTransferDTO.setDebitAccountId(moneyTransfer.getDebitRecord().getAccount().getId());
        moneyTransferDTO.setCreditAccountId(moneyTransfer.getCreditRecord().getAccount().getId());
        moneyTransferDTO.setDescription(moneyTransfer.getDescription());

        String value = moneyTransfer.getValue().toString();
        value = value.replace(".", ",");
        moneyTransferDTO.setValue(value);

        moneyTransferDTO.setGoalId(moneyTransfer.getGoal() != null ? moneyTransfer.getGoal().getId() : null);

        return moneyTransferDTO;
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Integer id) {
        this.repository
                .findById(id)
                .map(moneyTransfer -> {
                    repository.delete(moneyTransfer);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transferência não encontrada"));
    }

    @GetMapping("{username}")
    @RequestMapping("/moneytransfersByUsername")
    public List<MoneyTransferListDTO> getAllByUsername(@RequestParam(value = "username", required = true) String username){
        List<MoneyTransferListDTO> moneyTransfers = repository.findAllByUsernameOrderByNameAsc(username);
        System.out.println(moneyTransfers);
        return moneyTransfers;
    }

}


