package io.github.julianaquirino.pennypincher.model.rest;

import io.github.julianaquirino.pennypincher.model.repository.CategoryRepository;
import io.github.julianaquirino.pennypincher.model.repository.ProjectRepository;
import io.github.julianaquirino.pennypincher.model.rest.dto.ChartCategoriesDTO;
import io.github.julianaquirino.pennypincher.model.rest.dto.ChartProjectsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/charts")
public class ChartController {

    private final CategoryRepository categoryRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    private ChartController(CategoryRepository categoryRepository, ProjectRepository projectRepository) {
        this.categoryRepository = categoryRepository;
        this.projectRepository = projectRepository;
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


}


