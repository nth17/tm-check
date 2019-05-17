package edu.mum.tmcheck.controllers;

import edu.mum.tmcheck.domain.entities.Student;
import edu.mum.tmcheck.domain.entities.User;
import edu.mum.tmcheck.domain.repository.UserRepository;
import edu.mum.tmcheck.serviceimp.AttendanceServiceImp;
import edu.mum.tmcheck.serviceimp.BlockEndEachStudentMeditationData;
import edu.mum.tmcheck.serviceimp.ExcelReportGeneratorServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/students/attendancereport")
public class BlockEndECReportController {

    @Autowired
    AttendanceServiceImp attendanceServiceImp;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ExcelReportGeneratorServiceImp excelReportGeneratorServiceImp;

    @GetMapping("/ExtraCredit")
    public String ExtraCreditReport(HttpSession session, Model model) {
        Long userid = (Long) session.getAttribute("userid");
        List<BlockEndEachStudentMeditationData> StudentData = attendanceServiceImp.ComputeBlockEC((userid));
        model.addAttribute("StudentExtraCreditData", StudentData);
        return "ExtraCreditList";
    }

    @GetMapping("/ExtraCredit/download/xls")
    public ResponseEntity<InputStreamResource> excelExtraCreditReport(HttpSession session, Model model) throws IOException {
        Long userid = (Long) session.getAttribute("userid");
        List<BlockEndEachStudentMeditationData> StudentData = attendanceServiceImp.ComputeBlockEC((long) 3);
        ByteArrayInputStream in = excelReportGeneratorServiceImp.ExtraCreditToExcel((List<BlockEndEachStudentMeditationData>) StudentData);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=ECRecord.xlsx");
        return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));
    }


}
