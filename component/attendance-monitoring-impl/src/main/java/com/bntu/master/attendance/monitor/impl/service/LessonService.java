package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.api.exception.AttendanceMonitorException;
import com.bntu.master.attendance.monitor.api.exception.NotFoundException;
import com.bntu.master.attendance.monitor.api.model.GroupVolumeConstant;
import com.bntu.master.attendance.monitor.api.model.LessonDto;
import com.bntu.master.attendance.monitor.api.model.LessonScheduleDto;
import com.bntu.master.attendance.monitor.api.model.LessonSeries;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.api.model.SubjectTypeConstant;
import com.bntu.master.attendance.monitor.api.model.scheduleGrid.ScheduleGrid;
import com.bntu.master.attendance.monitor.api.model.scheduleGrid.ScheduleList;
import com.bntu.master.attendance.monitor.impl.converter.LessonConverter;
import com.bntu.master.attendance.monitor.impl.converter.PersonConverter;
import com.bntu.master.attendance.monitor.impl.dataaccess.LessonRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.ProfessorPositionRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.StudentGroupRepository;
import com.bntu.master.attendance.monitor.impl.entity.Account;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Lesson;
import com.bntu.master.attendance.monitor.impl.entity.LessonSchedule;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.ProfessorPosition;
import com.bntu.master.attendance.monitor.impl.entity.Role;
import com.bntu.master.attendance.monitor.impl.entity.StudentGroup;
import com.bntu.master.attendance.monitor.impl.entity.Subject;
import com.bntu.master.attendance.monitor.impl.resolver.GroupResolver;
import com.bntu.master.attendance.monitor.impl.resolver.LessonResolver;
import com.bntu.master.attendance.monitor.impl.resolver.LessonScheduleResolver;
import com.bntu.master.attendance.monitor.impl.resolver.PersonResolver;
import com.bntu.master.attendance.monitor.impl.resolver.RoleResolver;
import com.bntu.master.attendance.monitor.impl.resolver.SubjectResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Arrays;
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
    private LessonScheduleResolver lessonScheduleResolver;

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
    private ProfessorPositionRepository professorRepository;

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
        ProfessorPosition professorPosition = professorRepository.findFirstByProfessor(professor);
        Lesson lesson = converter.convertToEntity(lessonDto, professorPosition, group);

        lesson = repository.save(lesson);

        return converter.convertToDto(lesson);
    }

    public LessonDto update(Long id, LessonDto lessonDto) {
        lessonDto.setId(id);
        resolver.resolve(lessonDto);
        Group group = groupResolver.resolve(lessonDto.getGroup());
        Person professor = personResolver.resolvePersonByRole(lessonDto.getProfessor(), RoleConstant.PROFESSOR);
        ProfessorPosition professorPosition = professorRepository.findFirstByProfessor(professor);
        Lesson lesson = converter.convertToEntity(lessonDto, professorPosition, group);

        lesson = repository.save(lesson);

        return converter.convertToDto(lesson);
    }

    public void delete(ObjectRef objectRef) {
        repository.delete(resolver.resolve(objectRef));
    }

    public List<LessonDto> createSeries(LessonSeries lessonSeries) {
        if (!lessonSeries.isNullId()) {
            throw new AttendanceMonitorException();
        }
        Group group = groupResolver.resolve(lessonSeries.getGroup());
        Person professor = personResolver.resolvePersonByRole(lessonSeries.getProfessor(), RoleConstant.PROFESSOR);
        ProfessorPosition professorPosition = professorRepository.findFirstByProfessor(professor);
        List<LessonDto> result = new ArrayList<>();
        Set<Integer> weeks = new HashSet<>();
        LocalDate startDate = LocalDate.parse(lessonSeries.getStart().toString());
        while (!lessonSeries.getFinish().plusDays(7 - lessonSeries.getFinish().getDayOfWeek().getValue()).isBefore(startDate)) {
            weeks.add(startDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR));
            startDate = startDate.plusWeeks(lessonSeries.getRepeatWeek());
        }
        for (LocalDate date = lessonSeries.getStart(); !lessonSeries.getFinish().isBefore(date); date = date.plusDays(1)) {
            if (!weeks.contains(date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR))) continue;

            if (lessonSeries.getDays().contains(date.getDayOfWeek().getValue())) {
                Lesson lesson = converter.convertToEntity(lessonSeries, professorPosition, group);
                lesson.setDate(date);
                result.add(converter.convertToDto(repository.save(lesson)));
            }
        }
        return result;
    }

    public void deleteSeries(LessonSeries lessonSeries) {
        if (!lessonSeries.isNullId()) {
            throw new AttendanceMonitorException();
        }
        Group group = groupResolver.resolve(lessonSeries.getGroup());
        Person professor = personResolver.resolvePersonByRole(lessonSeries.getProfessor(), RoleConstant.PROFESSOR);
        ProfessorPosition professorPosition = professorRepository.findFirstByProfessor(professor);
        LessonSchedule lessonSchedule = lessonScheduleResolver.resolve(ObjectRef.toObjectRef(lessonSeries.getTime().getId()));
        GroupVolumeConstant groupVolume = GroupVolumeConstant.find(lessonSeries.getGroupVolume()).orElseThrow(NotFoundException::new);
        Subject subject = subjectResolver.resolve(lessonSeries.getSubject());
        List<Lesson> toDelete = new ArrayList<>();

        Set<Integer> weeks = new HashSet<>();
        LocalDate startDate = LocalDate.parse(lessonSeries.getStart().toString());
        while (!startDate.isBefore(lessonSeries.getFinish())) {
            weeks.add(startDate.get(ChronoField.ALIGNED_WEEK_OF_YEAR));
            startDate = startDate.plusWeeks(lessonSeries.getRepeatWeek());
        }
        for (LocalDate date = lessonSeries.getStart(); date.isBefore(lessonSeries.getFinish()) || date.isEqual(lessonSeries.getFinish()); date = date.plusDays(1)) {
            if (!weeks.contains(date.get(ChronoField.ALIGNED_WEEK_OF_YEAR))) continue;

            if (lessonSeries.getDays().contains(date.getDayOfWeek().getValue())) {
                repository.findFirstByDateAndGroupAndTimeAndProfessorIdAndGroupVolumeIdAndSubjectAndSubjectType(
                        date,
                        group,
                        lessonSchedule,
                        professorPosition.getId(),
                        groupVolume.getId(),
                        subject,
                        lessonSeries.getSubjectType()).ifPresent(toDelete::add);
            }
        }
        repository.deleteAll(toDelete);
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

    public List<LessonDto> findByDateRangeAndProfessor(LocalDate startDate, LocalDate finalDate, ProfessorPosition prof) {
        return repository.findAllByDateBetweenAndProfessor(startDate, finalDate, prof)
                .stream()
                .map(lesson -> converter.convertToDto(lesson))
                .collect(Collectors.toList());
    }

    public List<LessonDto> findByDateRangeAndStudent(LocalDate startDate, LocalDate finalDate, Person stud) {
        StudentGroup studentGroup = studentGroupRepository.findFirstByStudent(stud);
        Group group = studentGroup.getGroup();
        return repository.findAllByDateBetweenAndGroupAndGroupVolumeIdIn(startDate, finalDate, group,
                Arrays.asList(GroupVolumeConstant.FULL.getId(), studentGroup.getGroupVolumeId()))
                .stream()
                .map(lesson -> converter.convertToDto(lesson))
                .collect(Collectors.toList());
    }


    public Set<LessonDto> findAllByDateBetweenAndGroupAndSubjectAndSubjectTypeInAndGroupVolumeId(LocalDate startDate, LocalDate finalDate, Group group, Subject subject, Set<SubjectTypeConstant> subjectType, Long groupVolumeId) {
        return repository.findAllByDateBetweenAndGroupAndSubjectAndSubjectTypeInAndGroupVolumeId(startDate, finalDate, group, subject, subjectType, groupVolumeId)
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

    public ScheduleList findGridByDateRange(LocalDate startDate, LocalDate finalDate, String email, boolean topDateHeader) {
        Person person = personResolver.resolvePerson(ObjectRef.toObjectRef(email));
        return findGridByDateRange(startDate, finalDate, person.getId(), topDateHeader);
    }


    public ScheduleList findGridByDateRange(LocalDate startDate, LocalDate finalDate, Long personId, boolean topDateHeader) {
        Person person = personResolver.resolvePerson(ObjectRef.toObjectRef(personId));
        Account account = personResolver.resolveUser(ObjectRef.toObjectRef(person.getEmail()));

        Role roleProf = roleResolver.resolve(ObjectRef.toObjectRef("PROFESSOR"));
        Role roleStud = roleResolver.resolve(ObjectRef.toObjectRef("STUDENT"));
        List<LessonDto> lessons = new ArrayList<>();
        if (account.getRoles().contains(roleProf)) {
            ProfessorPosition professorPosition = professorRepository.findFirstByProfessor(person);
            lessons = findByDateRangeAndProfessor(startDate, finalDate, professorPosition);
        } else if (account.getRoles().contains(roleStud)) {
            lessons = findByDateRangeAndStudent(startDate, finalDate, person);
        }

        Set<LocalDate> header = new HashSet<>();
        lessons.forEach(l -> header.add(l.getDate()));
        header.stream().sorted();

        Set<LessonScheduleDto> headerRow = new HashSet<>();
        lessons.forEach(l -> headerRow.add(l.getTime()));
        headerRow.stream().sorted(Comparator.comparing(LessonScheduleDto::getOrder));

        ScheduleGrid grid = new ScheduleGrid(header, headerRow);
        lessons.forEach(grid::setCell);

        return new ScheduleList(grid, personConverter.convertToDto(person), topDateHeader);
    }


}
