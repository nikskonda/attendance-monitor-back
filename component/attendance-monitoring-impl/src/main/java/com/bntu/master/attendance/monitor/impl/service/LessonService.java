package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.api.exception.AttendanceMonitorException;
import com.bntu.master.attendance.monitor.api.model.LessonScheduleDto;
import com.bntu.master.attendance.monitor.api.model.SubjectTypeConstant;
import com.bntu.master.attendance.monitor.api.model.scheduleGrid.ScheduleGrid;
import com.bntu.master.attendance.monitor.api.model.LessonDto;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.api.model.scheduleGrid.ScheduleList;
import com.bntu.master.attendance.monitor.impl.converter.LessonConverter;
import com.bntu.master.attendance.monitor.impl.converter.PersonConverter;
import com.bntu.master.attendance.monitor.impl.dataaccess.LessonRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.StudentGroupRepository;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Lesson;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.Role;
import com.bntu.master.attendance.monitor.impl.entity.StudentGroup;
import com.bntu.master.attendance.monitor.impl.entity.Subject;
import com.bntu.master.attendance.monitor.impl.entity.User;
import com.bntu.master.attendance.monitor.impl.resolver.GroupResolver;
import com.bntu.master.attendance.monitor.impl.resolver.LessonResolver;
import com.bntu.master.attendance.monitor.impl.resolver.PersonResolver;
import com.bntu.master.attendance.monitor.impl.resolver.RoleResolver;
import com.bntu.master.attendance.monitor.impl.resolver.SubjectResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LessonService {

    @Autowired
    private LessonRepository repository;

    @Autowired
    private StudentGroupRepository studentGroupRepository;

    @Autowired
    private LessonResolver resolver;

    @Autowired
    private PersonResolver personResolver;

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonConverter personConverter;

    @Autowired
    private GroupResolver groupResolver;

    @Autowired
    private RoleResolver roleResolver;

    @Autowired
    private SubjectResolver subjectResolver;

    @Autowired
    private LessonConverter converter;

    public LessonDto find(ObjectRef objectRef) {
        return converter.convertToDto(resolver.resolve(objectRef));
    }

    public LessonDto create(LessonDto lessonDto) {
        if (!lessonDto.isNullId()) {
            throw new AttendanceMonitorException();
        }
        Group group = groupResolver.resolve(lessonDto.getGroup());
        Person professor = personResolver.resolvePersonByRole(lessonDto.getProfessor(), RoleConstant.PROFESSOR);
        Lesson lesson = converter.convertToEntity(lessonDto, professor, group);

        lesson = repository.save(lesson);

        return converter.convertToDto(lesson);
    }

    public List<LessonDto> createSeries(LessonDto lessonDto, Long inWeek, Long count) {
        if (!lessonDto.isNullId()) {
            throw new AttendanceMonitorException();
        }
        Group group = groupResolver.resolve(lessonDto.getGroup());
        Person professor = personResolver.resolvePersonByRole(lessonDto.getProfessor(), RoleConstant.PROFESSOR);
        List<LessonDto> result = new ArrayList<>();
        for (int i = 0; i<count; i++) {
            Lesson lesson = converter.convertToEntity(lessonDto, professor, group);
            lesson.setDate(lesson.getDate().plusWeeks(i*inWeek));
            result.add(converter.convertToDto(repository.save(lesson)));
        }
        return result;
    }

    public LessonDto update(Long id, LessonDto lessonDto) {
        lessonDto.setId(id);
        resolver.resolve(lessonDto);
        Group group = groupResolver.resolve(lessonDto.getGroup());
        Person professor = personResolver.resolvePersonByRole(lessonDto.getProfessor(), RoleConstant.PROFESSOR);
        Lesson lesson = converter.convertToEntity(lessonDto, professor, group);

        lesson = repository.save(lesson);

        return converter.convertToDto(lesson);
    }

    public void delete(ObjectRef objectRef) {
        repository.delete(resolver.resolve(objectRef));
    }

    public List<LessonDto> findAll() {
        return repository.findAll()
                .stream()
                .map(lesson -> converter.convertToDto(lesson))
                .collect(Collectors.toList());
    }

    public List<LessonDto> findByDateRange(LocalDate startDate, LocalDate finalDate) {
        return repository.findAllByDateBetween(startDate, finalDate)
                .stream()
                .map(lesson -> converter.convertToDto(lesson))
                .collect(Collectors.toList());
    }

    public Page<LessonDto> findPageByDateRange(LocalDate startDate, LocalDate finalDate, Pageable pageable) {
        return new PageImpl<>(repository.findAllByDateBetween(startDate, finalDate, pageable)
                .stream()
                .map(lesson -> converter.convertToDto(lesson))
                .collect(Collectors.toList()),
                pageable,
                repository.count());
    }

    public List<LessonDto> findByDateRangeAndProfessor(LocalDate startDate, LocalDate finalDate, Person prof) {
        return repository.findAllByDateBetweenAndProfessor(startDate, finalDate, prof)
                .stream()
                .map(lesson -> converter.convertToDto(lesson))
                .collect(Collectors.toList());
    }

    public List<LessonDto> findByDateRangeAndStudent(LocalDate startDate, LocalDate finalDate, Person stud) {
        StudentGroup studentGroup = studentGroupRepository.findFirstByStudent(stud);
        Group group = studentGroup.getGroup();
        List<Lesson> lessons = repository.findAllByDateBetweenAndGroup(startDate, finalDate, group);
        return repository.findAllByDateBetweenAndGroup(startDate, finalDate, group)
                .stream()
                .map(lesson -> converter.convertToDto(lesson))
                .collect(Collectors.toList());
    }



    public Set<LessonDto> findAllByDateBetweenAndSubjectAndSubjectTypeIn(LocalDate startDate, LocalDate finalDate, Subject subject, Set<SubjectTypeConstant> subjectType) {
        return repository.findAllByDateBetweenAndSubjectAndSubjectTypeIn(startDate, finalDate, subject, subjectType)
                .stream()
                .map(lesson -> converter.convertToDto(lesson))
                .collect(Collectors.toSet());
    }

    public Set<LessonDto> findAllByDateBetweenAndGroupAndSubjectAndSubjectTypeIn(LocalDate startDate, LocalDate finalDate, Group group, Subject subject, Set<SubjectTypeConstant> subjectType) {
        return repository.findAllByDateBetweenAndGroupAndSubjectAndSubjectTypeIn(startDate, finalDate, group, subject, subjectType)
                .stream()
                .map(lesson -> converter.convertToDto(lesson))
                .collect(Collectors.toSet());
    }

    public ScheduleList findGridByDateRange(LocalDate startDate, LocalDate finalDate, String email) {
        Person person = personResolver.resolvePerson(ObjectRef.toObjectRef(email));
        return findGridByDateRange(startDate, finalDate, person.getId());
    }


    public ScheduleList findGridByDateRange(LocalDate startDate, LocalDate finalDate, Long personId) {
        Person person = personResolver.resolvePerson(ObjectRef.toObjectRef(personId));
        User user = personResolver.resolveUser(ObjectRef.toObjectRef(person.getEmail()));

        Role roleProf = roleResolver.resolve(ObjectRef.toObjectRef("PROFESSOR"));
        Role roleStud = roleResolver.resolve(ObjectRef.toObjectRef("STUDENT"));
        List<LessonDto> lessons = new ArrayList<>();
        if (user.getRoles().contains(roleProf)) {
            lessons = findByDateRangeAndProfessor(startDate, finalDate, person);
        } else
        if (user.getRoles().contains(roleStud)) {
            lessons = findByDateRangeAndStudent(startDate, finalDate, person);
        }

        Set<LocalDate> header = new HashSet<>();
        lessons.forEach(l -> header.add(l.getDate()));
        header.stream().sorted();

        Set<LessonScheduleDto> headerRow = new HashSet<>();
        lessons.forEach(l -> headerRow.add(l.getTime()));
        headerRow.stream().sorted(Comparator.comparing(LessonScheduleDto::getOrder));

        ScheduleGrid grid = new ScheduleGrid(header, headerRow);
        lessons.forEach(l -> grid.setCell(l));

        return new ScheduleList(grid, personConverter.convertToDto(person));
    }


}
