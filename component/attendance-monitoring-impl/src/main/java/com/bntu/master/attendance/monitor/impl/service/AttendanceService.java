package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.api.model.GroupVolumeConstant;
import com.bntu.master.attendance.monitor.api.model.LessonDto;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.PersonDto;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.api.model.SubjectTypeConstant;
import com.bntu.master.attendance.monitor.api.model.attendance.AttendanceCell;
import com.bntu.master.attendance.monitor.api.model.attendance.AttendanceGrid;
import com.bntu.master.attendance.monitor.api.model.attendance.AttendanceList;
import com.bntu.master.attendance.monitor.api.model.attendance.AttendanceValue;
import com.bntu.master.attendance.monitor.api.model.util.DateSpan;
import com.bntu.master.attendance.monitor.impl.converter.LessonConverter;
import com.bntu.master.attendance.monitor.impl.converter.PersonConverter;
import com.bntu.master.attendance.monitor.impl.dataaccess.AttendanceRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.LessonRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.PersonRepository;
import com.bntu.master.attendance.monitor.impl.entity.Attendance;
import com.bntu.master.attendance.monitor.impl.entity.Lesson;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.resolver.GroupResolver;
import com.bntu.master.attendance.monitor.impl.resolver.LessonResolver;
import com.bntu.master.attendance.monitor.impl.resolver.PersonResolver;
import com.bntu.master.attendance.monitor.impl.resolver.SubjectResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository repository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private LessonService lessonService;

    @Autowired
    private PersonService personService;

    @Autowired
    private StudentService studentService;

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
    private PersonConverter personConverter;

    public List<AttendanceCell> saveAll(List<AttendanceCell> cells) {
        List<Attendance> toSave = new ArrayList<>();
        for (AttendanceCell cell : cells) {
            Lesson lesson = lessonResolver.resolve(cell.getLesson());
            Person student = personResolver.resolvePersonByRole(cell.getPerson(), RoleConstant.STUDENT);
            Attendance fromDb = repository.findFirstByStudentAndLesson(student, lesson);

            Attendance attendance = new Attendance();
            if (fromDb != null) {
                attendance.setId(fromDb.getId());
            }
            attendance.setDateTime(LocalDateTime.now());
            attendance.setStudent(student);
            attendance.setProfessor(lesson.getProfessor());
            attendance.setLesson(lesson);
            attendance.setValue(AttendanceValue.find(cell.getText()));
            attendance.setGoodReason(cell.isGoodReason());
            toSave.add(attendance);
        }
        repository.saveAll(toSave);
        return Collections.EMPTY_LIST; //ToDO
    }

    public AttendanceList getAttendanceList(ObjectRef studentGroup, DateSpan dateSpan, ObjectRef subject, Set<SubjectTypeConstant> subjectTypes, String groupVolumeStr) {
        GroupVolumeConstant groupVolume = GroupVolumeConstant.find(groupVolumeStr).orElse(GroupVolumeConstant.FULL);
        Set<LessonDto> lessons =
//                subjectType.contains(SubjectTypeConstant.LECTURE) ?
//                lessonService.findAllByDateBetweenAndGroupAndSubjectAndSubjectTypeIn(
//                        dateSpan.getStartDate(),
//                        dateSpan.getFinishDate(),
//                        groupResolver.resolve(studentGroup),
//                        subjectResolver.resolve(subject),
//                        subjectType) :
                lessonService.findAllByDateBetweenAndGroupAndSubjectAndSubjectTypeInAndGroupVolumeId(
                        dateSpan.getStartDate(),
                        dateSpan.getFinishDate(),
                        groupResolver.resolve(studentGroup),
                        subjectResolver.resolve(subject),
                        subjectTypes,
                        groupVolume.getId());

        List<PersonDto> students = new ArrayList<>(GroupVolumeConstant.FULL.equals(groupVolume) ?
                studentService.findStudentsGroup(studentGroup) :
                studentService.findStudentsByGroup(studentGroup, groupVolume));

        Set<AttendanceValue> values = new HashSet<>();
        Collections.addAll(values, AttendanceValue.ONE_HOUR, AttendanceValue.TWO_HOUR);

        AttendanceGrid grid = new AttendanceGrid(lessons, students);
        List<Attendance> attendances = repository.findAllByStudentInAndLessonInAndValueIn(
                students.stream()
                        .map(st -> personConverter.convertToEntity(st))
                        .collect(Collectors.toSet()),
                lessons.stream()
                        .map(l -> lessonConverter.convertToEntity(l, null, null))
                        .collect(Collectors.toSet()),
                values);

        attendances.forEach(attendance ->
                grid.setCell(
                        ObjectRef.toObjectRef(attendance.getStudent().getId()),
                        ObjectRef.toObjectRef(attendance.getLesson().getId()),
                        attendance.getValue(),
                        attendance.isGoodReason()));

        LessonDto lessonDto = null;
        if (!lessons.isEmpty()) {
            lessonDto = lessons.iterator().next();
        }
        return new AttendanceList(grid, lessonDto != null ? lessonDto.getSubject() : null, lessonDto != null ? lessonDto.getGroup() : null);
    }

}
