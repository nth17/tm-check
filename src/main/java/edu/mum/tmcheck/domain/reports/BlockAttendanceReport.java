package edu.mum.tmcheck.domain.reports;


import edu.mum.tmcheck.domain.entities.Attendance;
import edu.mum.tmcheck.utils.Dates;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Synchronize;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDate;

@Entity(name = "BlockAttendanceReport")
@Subselect("SELECT " +
        "       concat(s.id,a.block_id) as id , " +
        "       s.student_reg_id, " +
        "       s.name, " +
        "       a.block_id, " +
        "       a.block_start, " +
        "       a.block_end, " +
        "       a.standard_tm, " +
        "       a.retreats, " +
        "       a.checks " +
        " from STUDENT as s " +
        "         left join ( " +
        "    select ai.STUDENT_ID, " +
        "           b.id                                                         as block_id, " +
        "           b.START_DATE                                                 AS block_start, " +
        "           b.END_DATE                                                   AS block_end, " +
        "           SUM(CASE WHEN LOWER(mt.name) = 'standard' THEN 1 ELSE 0 END) AS standard_tm, " +
        "           SUM(CASE WHEN LOWER(mt.name) = 'retreat' THEN 1 ELSE 0 END)  AS retreats, " +
        "           SUM(CASE WHEN LOWER(mt.name) = 'check' THEN 1 ELSE 0 END)    AS checks " +
        "    from ATTENDANCE ai " +
        "             LEFT JOIN meditation_type AS mt ON mt.id = ai.meditation_type_id " +
        "             LEFT JOIN STUDENT_ENROLLED_COURSES as sc on sc.STUDENTS_ID = ai.STUDENT_ID " +
        "             LEFT JOIN OFFERED_COURSE as oc on oc.ID = sc.ENROLLED_COURSES_ID " +
        "             LEFT JOIN BLOCK as b on b.ID = oc.BLOCK_ID " +
        "    group by ai.STUDENT_ID,b.id, b.START_DATE, b.END_DATE " +
        ") as a on s.id = a.student_id " +
        " where a.block_id is not null")
@Synchronize({"attendance", "student", "block", "OFFERED_COURSE", "STUDENT_ENROLLED_COURSES", "meditation_type"})
public class BlockAttendanceReport {
    @Id
    String id;
    String studentRegId;
    String name;
    long blockId;
    LocalDate blockStart;
    LocalDate blockEnd;
    int standardTm;
    int retreats;
    int checks;

    @Transient
    double overrallAttendance = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentRegId() {
        return studentRegId;
    }

    public void setStudentRegId(String studentRegId) {
        this.studentRegId = studentRegId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRetreats() {
        return retreats;
    }

    public void setRetreats(int retreats) {
        this.retreats = retreats;
    }

    public int getChecks() {
        return checks;
    }

    public long getBlockId() {
        return blockId;
    }

    public void setBlockId(long blockId) {
        this.blockId = blockId;
    }

    public void setChecks(int checks) {
        this.checks = checks;
    }

    public LocalDate getBlockStart() {
        return blockStart;
    }

    public void setBlockStart(LocalDate blockStart) {
        this.blockStart = blockStart;
    }

    public LocalDate getBlockEnd() {
        return blockEnd;
    }

    public void setBlockEnd(LocalDate blockEnd) {
        this.blockEnd = blockEnd;
    }

    public int getStandardTm() {
        return standardTm;
    }

    public void setStandardTm(int standardTm) {
        this.standardTm = standardTm;
    }

    public double getOverrallAttendance() {
        long days = Dates.countWeekDays(blockStart, blockEnd);

        if (days == 0) return 0;

        double _retreats = convertRetreats(retreats, days);

        double _overrallAttendance = (Double.valueOf(standardTm + _retreats) / Double.valueOf(days)) * 100;
        return Math.round(_overrallAttendance);
    }

    public double convertRetreats(int count, long days) {
        return Attendance.RETREAT_TO_STANDARD_TM_RATE * count * days;
    }
}
