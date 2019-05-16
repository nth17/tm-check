package edu.mum.tmcheck.fixtures;

import edu.mum.tmcheck.domain.entities.*;
import edu.mum.tmcheck.serviceimp.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class AttendanceFixture extends BaseFixture {
    @Autowired
    AttendanceServiceImp attendanceServiceImp;

    @Autowired
    StudentServiceImp studentServiceImp;

    @Autowired
    TMtypeServiceImp tMtypeServiceImp;

    @Autowired
    MeditationTypeServiceImp meditationTypeServiceImp;

    @Autowired
    LocationServiceImp locationServiceImp;

    List<Student> students = new ArrayList<>();
    List<TMType> tmTypes = new ArrayList<>();
    List<MeditationType> meditationTypes = new ArrayList<>();
    List<Location> locations = new ArrayList<>();

    @Override
    public void generate(int size) {
        students = studentServiceImp.findAll();
        tmTypes = tMtypeServiceImp.findAll();
        meditationTypes = meditationTypeServiceImp.findAll();
        locations = locationServiceImp.findAll();

        Date now = java.sql.Date.valueOf(LocalDate.now());

        while (size-- > 0) {
            Attendance attendance = new Attendance();

            attendance.setStudent(randomStudent());
            attendance.setTmType(randomTMType());
            attendance.setMeditationType(randomMeditationType());
            attendance.setLocation(randomLocations());
            Date from = faker.date().past(2 * 30, TimeUnit.DAYS);

            LocalDate createdAt = toLocalDate(faker.date().between(from, now));
            attendance.setCreatedAt(createdAt);

            attendanceServiceImp.save(attendance);
        }
    }

    public Student randomStudent() {
        int index = random.nextInt(students.size() - 1) + 1;

        return students.get(index);
    }

    public TMType randomTMType() {
        int index = random.nextInt(tmTypes.size() - 1) + 1;

        return tmTypes.get(index);
    }

    public MeditationType randomMeditationType() {
        int index = random.nextInt(meditationTypes.size() - 1) + 1;

        return meditationTypes.get(index);
    }

    public Location randomLocations() {
        int index = random.nextInt(locations.size() - 1) + 1;

        return locations.get(index);
    }
}