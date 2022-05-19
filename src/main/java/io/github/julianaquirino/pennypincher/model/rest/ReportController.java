package io.github.julianaquirino.pennypincher.model.rest;

import io.github.julianaquirino.pennypincher.model.repository.RecordRepository;
import io.github.julianaquirino.pennypincher.model.rest.dto.RecordsReportDTO;
import io.github.julianaquirino.pennypincher.model.util.CategoryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final RecordRepository recordRepository;

    @Autowired
    private ReportController(RecordRepository recordRepository){
        this.recordRepository = recordRepository;
    }

    @RequestMapping(value = "/recordsByTypeIdMonthYearUsername", method = RequestMethod.GET)
    public List<RecordsReportDTO> getRecordsByTypeIdMonthYearUsername(@RequestParam Map<String, String> reqParam) {

        Integer categoryId = (reqParam.get("categoryId") != null
                && !reqParam.get("categoryId").equals("undefined")
                && !reqParam.get("categoryId").equals(""))
                ? Integer. parseInt(reqParam.get("categoryId")) : null;
        Integer month = (reqParam.get("month") != null
                && !reqParam.get("month").equals("undefined")
                && !reqParam.get("month").equals("")) ? Integer. parseInt(reqParam.get("month")) : null;
        Integer year = (reqParam.get("year") != null
                && !reqParam.get("year").equals("undefined")
                && !reqParam.get("year").equals("")) ? Integer. parseInt(reqParam.get("year")) : null;
        String username = reqParam.get("username");
        System.out.println("categoryId: " + categoryId);
        System.out.println("month: " + month);
        System.out.println("year: " + year);
        System.out.println("username: " + username);

        return recordRepository.findRecordsByTypeIdMonthYearUsername(categoryId, month, year, username);
    }

}

