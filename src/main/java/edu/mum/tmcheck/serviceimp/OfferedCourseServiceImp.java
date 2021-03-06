package edu.mum.tmcheck.serviceimp;

import edu.mum.tmcheck.domain.entities.Block;
import edu.mum.tmcheck.domain.entities.OfferedCourse;
import edu.mum.tmcheck.domain.repository.OfferedCourseRepository;
import edu.mum.tmcheck.services.OfferedCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferedCourseServiceImp implements OfferedCourseService {
    @Autowired
    OfferedCourseRepository offeredCourseRepository;

    @Override
    public void create() {
    }


    public List<Block>  getfacultytaughtblock(Long userid){
        return offeredCourseRepository.findAll().stream().filter(offcourse -> offcourse.getFaculty().getId()==userid).map(offcourse -> offcourse.getBlock()).collect(Collectors.toList());
    }

    @Override
    public OfferedCourse get() {
        return null;
    }

    @Override
    public OfferedCourse save(OfferedCourse instance) {
        return offeredCourseRepository.save(instance);
    }

    @Override
    public List<OfferedCourse> findAll() {
        List<OfferedCourse> records = new ArrayList<>();
        offeredCourseRepository.findAll().forEach(records::add);

        return records;
    }
}
