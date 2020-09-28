package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.PersonDto;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.api.model.report.ReportByStudentAndSubjects;
import com.bntu.master.attendance.monitor.impl.converter.LessonConverter;
import com.bntu.master.attendance.monitor.impl.converter.PersonConverter;
import com.bntu.master.attendance.monitor.impl.converter.SubjectConverter;
import com.bntu.master.attendance.monitor.impl.dataaccess.AttendanceRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.LessonRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.PersonRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.StudentGroupRepository;
import com.bntu.master.attendance.monitor.impl.entity.Attendance;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Lesson;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.StudentGroup;
import com.bntu.master.attendance.monitor.impl.resolver.GroupResolver;
import com.bntu.master.attendance.monitor.impl.resolver.LessonResolver;
import com.bntu.master.attendance.monitor.impl.resolver.PersonResolver;
import com.bntu.master.attendance.monitor.impl.resolver.SubjectResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentGroupRepository studentGroupRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private PersonRepository personRepository;


    @Autowired
    private LessonResolver lessonResolver;

    @Autowired
    private PersonResolver personResolver;

    @Autowired
    private GroupResolver groupResolver;

    @Autowired
    private SubjectResolver subjectResolver;


    @Autowired
    private LessonConverter lessonConverter;

    @Autowired
    private SubjectConverter subjectConverter;

    @Autowired
    private PersonConverter personConverter;



    public List<ReportByStudentAndSubjects> getDataByStudentInDateRange(LocalDate start, LocalDate finish, ObjectRef person){
        Person student = personResolver.resolveByRole(person, RoleConstant.STUDENT);
        StudentGroup studentGroup = studentGroupRepository.findFirstByStudent(student);
        Group group = studentGroup.getGroup();
        List<Lesson> lessons = lessonRepository.findAllByDateBetweenAndGroup(start, finish, group);
        List<Attendance> attendances = attendanceRepository.findAllByStudentAndLessonIn(student, lessons);

        PersonDto studentDto = personConverter.convertToDto(student);

        //key = subject id
        Map<Long, ReportByStudentAndSubjects> map = new HashMap<>();
        for (Attendance attendance : attendances) {
            Long subjectId = attendance.getLesson().getSubject().getId();
            if (map.containsKey(subjectId)) {
                map.get(subjectId).addHours(attendance.getValue().getHours());
            } else {
                map.put(subjectId,
                        new ReportByStudentAndSubjects(
                            studentDto,
                            subjectConverter.convertToDto(attendance.getLesson().getSubject()),
                            attendance.getValue().getHours()));
            }
        }
        return map.values()
                .stream()
                .sorted(Comparator.comparing(e -> e.getSubject().getQualifier()))
                .collect(Collectors.toList());
    }

}
