package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.api.model.LessonDto;
import com.bntu.master.attendance.monitor.api.model.PersonDto;
import com.bntu.master.attendance.monitor.api.model.StudentDto;
import com.bntu.master.attendance.monitor.api.model.SubjectTypeConstant;
import com.bntu.master.attendance.monitor.api.model.attendance.AttendanceCell;
import com.bntu.master.attendance.monitor.api.model.attendance.AttendanceGrid;
import com.bntu.master.attendance.monitor.api.model.attendance.AttendanceList;
import com.bntu.master.attendance.monitor.api.model.attendance.AttendancePage;
import com.bntu.master.attendance.monitor.api.model.attendance.AttendanceValue;
import com.bntu.master.attendance.monitor.api.model.util.DateSpan;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.impl.converter.LessonConverter;
import com.bntu.master.attendance.monitor.impl.converter.PersonConverter;
import com.bntu.master.attendance.monitor.impl.dataaccess.AttendanceRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.LessonRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.PersonRepository;
import com.bntu.master.attendance.monitor.impl.entity.Attendance;
import com.bntu.master.attendance.monitor.impl.entity.Group;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

    public List<AttendanceCell> saveAll(List<AttendanceCell> cells){
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

    public AttendanceList getAttendanceForGroup(ObjectRef studentGroup, DateSpan dateSpan, ObjectRef subject, Set<SubjectTypeConstant> subjectType) {
        Set<LessonDto> lessons = lessonService.findAllByDateBetweenAndGroupAndSubjectAndSubjectTypeIn(dateSpan.getStartDate(), dateSpan.getFinishDate(), groupResolver.resolve(studentGroup), subjectResolver.resolve(subject), subjectType);

        List<PersonDto> students = new ArrayList<>(personService.findStudentsByGroup(studentGroup));

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
        attendances
                .forEach(attendance ->
                        grid.setCell(
                                ObjectRef.toObjectRef(attendance.getStudent().getId()),
                                ObjectRef.toObjectRef(attendance.getLesson().getId()),
                                attendance.getValue(),
                                attendance.isGoodReason()));

        LessonDto lessonDto = null;
        if (!lessons.isEmpty()) {
            lessonDto = lessons.iterator().next();
        }
        return new AttendanceList(grid, lessonDto!=null?lessonDto.getSubject():null, lessonDto!=null?lessonDto.getGroup():null);
    }

    private AttendancePage convert(List<Attendance> list) {
        Map<ObjectRef, Map<ObjectRef, AttendanceValue>> map = new HashMap<>();
        for (Attendance attendance : list) {
            ObjectRef lessonRef = new ObjectRef(attendance.getLesson().getId());
            ObjectRef studentRef = new ObjectRef(attendance.getStudent().getId());
            if (map.containsKey(lessonRef)) {
                map.get(lessonRef).put(studentRef, attendance.getValue());
            } else {
                Map<ObjectRef, AttendanceValue> innerMap = new HashMap<>();
                innerMap.put(studentRef, attendance.getValue());
                map.put(lessonRef, innerMap);
            }
        }
        AttendancePage page = new AttendancePage();
        page.setMap(map);
        return page;
    }

}
