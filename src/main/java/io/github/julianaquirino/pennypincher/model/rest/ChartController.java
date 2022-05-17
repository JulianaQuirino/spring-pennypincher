package io.github.julianaquirino.pennypincher.model.rest;

import io.github.julianaquirino.pennypincher.model.repository.*;
import io.github.julianaquirino.pennypincher.model.rest.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/charts")
public class ChartController {

    private final CategoryRepository categoryRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final ProjectRepository projectRepository;
    private final GoalRepository goalRepository;
    private final AccountRepository accountRepository;

    @Autowired
    private ChartController(CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository, ProjectRepository projectRepository,
                            GoalRepository goalRepository, AccountRepository accountRepository) {
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.projectRepository = projectRepository;
        this.goalRepository = goalRepository;
        this.accountRepository = accountRepository;
    }

    @RequestMapping(value = "/categoriesByTypeMonthYearUsername", method = RequestMethod.GET)
    public List<ChartCategoriesDTO> getAllByTypeMonthYearUsername(@RequestParam Map<String, String> reqParam) {

        String categoryType = reqParam.get("categoryType");
        Integer month = Integer. parseInt(reqParam.get("month"));
        Integer year = Integer. parseInt(reqParam.get("year"));
        String username = reqParam.get("username");
        System.out.println("categoryType: " + categoryType);
        System.out.println("month: " + month);
        System.out.println("year: " + year);
        System.out.println("username: " + username);

        return categoryRepository.findAllByTypeMonthYearUsername(categoryType, month, year, username);
    }


    @RequestMapping(value = "/debitsOfProject", method = RequestMethod.GET)
    public List<ChartProjectsDTO> getDebitsOfProjectUsername(@RequestParam Map<String, String> reqParam) {

        Integer projectId = Integer. parseInt(reqParam.get("projectId"));
        String username = reqParam.get("username");
        System.out.println("projectId: " + projectId);
        System.out.println("username: " + username);

        return projectRepository.findDebitsOfProjectUsername(projectId, username);
    }

    @GetMapping("{username}")
    @RequestMapping("/goalsStatusByUsername")
    public List<ChartGoalsDTO> getGoalsStatusByUsername(@RequestParam(value = "username", required = true) String username){
        return goalRepository.findGoalsStatusByUsername(username);
    }


    @RequestMapping(value = "/accountsBalanceByMonthYearUsername", method = RequestMethod.GET)
    public ArrayList<ChartGoalsProjectionDTO> getAccountsBalanceByMonthYearUsername(@RequestParam Map<String, String> reqParam) {
        Integer month = Integer. parseInt(reqParam.get("month"));
        Integer year = Integer. parseInt(reqParam.get("year"));
        String username = reqParam.get("username").toUpperCase();

        System.out.println("month: " + month);
        System.out.println("year: " + year);
        System.out.println("username: " + username);

        return accountRepository.findAccountsBalanceByMonthYearUsernameNative(month, year, username);
    }

    @RequestMapping(value = "/subcategoriesByCategoryMonthYearUsername", method = RequestMethod.GET)
    public List<ChartSubcategoriesDTO> getsubcategoriesByCategoryMonthYearUsername(@RequestParam Map<String, String> reqParam) {

        Integer categoryId = Integer. parseInt(reqParam.get("categoryId"));
        Integer month = Integer. parseInt(reqParam.get("month"));
        Integer year = Integer. parseInt(reqParam.get("year"));
        String username = reqParam.get("username");
        System.out.println("categoryId: " + categoryId);
        System.out.println("month: " + month);
        System.out.println("year: " + year);
        System.out.println("username: " + username);

        return subcategoryRepository.findSubcategoriesByCategoryMonthYearUsername(categoryId, month, year, username);
    }


}


