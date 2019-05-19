package edu.mum.tmcheck.serviceimp;

import edu.mum.tmcheck.domain.reports.BlockAttendanceReport;
import edu.mum.tmcheck.domain.repository.BlockAttendanceReportRepository;
import edu.mum.tmcheck.services.BlockAttendanceReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@Service
public class BlockAttendanceReportServiceImp implements BlockAttendanceReportService {
    @Autowired
    BlockAttendanceReportRepository blockAttendanceReportRepository;

    @Override
    public List<BlockAttendanceReport> findByBlock(LocalDate block_start, LocalDate block_end) {
        return blockAttendanceReportRepository.findByBlock(block_start, block_end);
    }
}