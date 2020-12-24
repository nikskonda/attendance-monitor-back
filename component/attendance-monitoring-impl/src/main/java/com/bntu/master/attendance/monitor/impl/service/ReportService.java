package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.api.model.GroupVolumeConstant;
import com.bntu.master.attendance.monitor.api.model.ObjectRef;
import com.bntu.master.attendance.monitor.api.model.PersonDto;
import com.bntu.master.attendance.monitor.api.model.ProfessorDto;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.api.model.StudentDto;
import com.bntu.master.attendance.monitor.api.model.StudentWithParentDto;
import com.bntu.master.attendance.monitor.api.model.SubjectTypeConstant;
import com.bntu.master.attendance.monitor.api.model.attendance.AttendanceValue;
import com.bntu.master.attendance.monitor.api.model.report.ReportByGroup;
import com.bntu.master.attendance.monitor.api.model.report.ReportByStudent;
import com.bntu.master.attendance.monitor.api.model.report.ReportByStudentAndSubjects;
import com.bntu.master.attendance.monitor.impl.converter.LessonConverter;
import com.bntu.master.attendance.monitor.impl.converter.PersonConverter;
import com.bntu.master.attendance.monitor.impl.converter.StudentConverter;
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
import com.bntu.master.attendance.monitor.impl.entity.Subject;
import com.bntu.master.attendance.monitor.impl.resolver.GroupResolver;
import com.bntu.master.attendance.monitor.impl.resolver.LessonResolver;
import com.bntu.master.attendance.monitor.impl.resolver.PersonResolver;
import com.bntu.master.attendance.monitor.impl.resolver.SubjectResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bntu.master.attendance.monitor.api.model.GroupVolumeConstant.FULL;

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

    @Autowired
    private StudentConverter studentConverter;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ProfessorService professorService;


    public List<List<String>> findGridForStudentReport(LocalDate start, LocalDate finish, ObjectRef person) {
        Person student = personResolver.resolvePersonByRole(person, RoleConstant.STUDENT);
        StudentGroup studentGroup = studentGroupRepository.findFirstByStudent(student);
        Group group = studentGroup.getGroup();
        List<Lesson> lessons = lessonRepository.findAllByDateBetweenAndGroupAndGroupVolumeIdIn(start, finish, group, Arrays.asList(studentGroup.getGroupVolumeId(), FULL.getId()));
        List<Attendance> attendances = attendanceRepository.findAllByStudentAndLessonIn(student, lessons);

        PersonDto studentDto = personConverter.convertToDto(student);

        Set<ObjectRef> subjectSet = lessons.stream()
                .map(l -> subjectConverter.convertToDto(l.getSubject()))
                .collect(Collectors.toSet());

        Set<SubjectTypeConstant> subjectTypeConstants = lessons.stream()
                .map(l -> l.getSubjectType())
                .collect(Collectors.toSet());

        ReportByStudentAndSubjects report = new ReportByStudentAndSubjects(subjectTypeConstants, subjectSet);

        attendances.forEach(a -> report.addAttHours(subjectConverter.convertToDto(a.getLesson().getSubject()), a.getLesson().getSubjectType(), a.getValue().getHours(), a.isGoodReason()));
        lessons.forEach(l -> report.addTotalHours(subjectConverter.convertToDto(l.getSubject()), l.getSubjectType()));

        return report.toStringGrid();
    }

    public List<List<String>> findGridForStudentDetailsReport(LocalDate start, LocalDate finish, ObjectRef person) {
        Person student = personResolver.resolvePersonByRole(person, RoleConstant.STUDENT);
        StudentGroup studentGroup = studentGroupRepository.findFirstByStudent(student);
        Group group = studentGroup.getGroup();
        List<Lesson> lessons = lessonRepository.findAllByDateBetweenAndGroupAndGroupVolumeIdIn(start, finish, group, Arrays.asList(studentGroup.getGroupVolumeId(), FULL.getId()));
        List<Attendance> attendances = attendanceRepository.findAllByStudentAndLessonIn(student, lessons);

        StudentDto studentDto = studentConverter.convertToDto(new StudentGroup(student, group));

        ReportByStudent report = new ReportByStudent(studentDto, start, finish);

        attendances.forEach(a -> report.add(lessonConverter.convertToDto(a.getLesson()), a.getValue().getHours(), a.isGoodReason()));

        return report.toStringGrid();
    }

    public List<List<String>> findGridForStudentAndSubjectDetailsReport(LocalDate start, LocalDate finish, ObjectRef person, ObjectRef subject) {
        Person student = personResolver.resolvePersonByRole(person, RoleConstant.STUDENT);
        Subject subj = subject.isNullable() ? null : subjectResolver.resolve(subject);
        StudentGroup studentGroup = studentGroupRepository.findFirstByStudent(student);
        Group group = studentGroup.getGroup();
        List<Lesson> lessons = subj == null ?
                lessonRepository.findAllByDateBetweenAndGroupAndGroupVolumeIdIn(start, finish, group, Arrays.asList(studentGroup.getGroupVolumeId(), FULL.getId()))
                :
                lessonRepository.findAllByDateBetweenAndGroupAndSubjectAndGroupVolumeIdIn(start, finish, group, subj, Arrays.asList(studentGroup.getGroupVolumeId(), FULL.getId()));
        List<Attendance> attendances = attendanceRepository.findAllByStudentAndLessonIn(student, lessons);

        StudentDto studentDto = studentConverter.convertToDto(new StudentGroup(student, group));

        ReportByStudent report = new ReportByStudent(studentDto, start, finish);

        attendances.forEach(a -> report.add(lessonConverter.convertToDto(a.getLesson()), a.getValue().getHours(), a.isGoodReason()));

        return report.toStringGrid();
    }

    public List<List<String>> findGridForGroupReport(ObjectRef groupRef, ObjectRef subjectRef, LocalDate start, LocalDate finish) {
        Group group = groupResolver.resolve(groupRef);
        Subject subject = subjectResolver.resolve(subjectRef);

        Set<Person> students = studentGroupRepository.findAllByGroup(group).stream().map(StudentGroup::getStudent).collect(Collectors.toSet());

        List<Lesson> lessons = lessonRepository.findAllByDateBetweenAndGroupAndSubject(start, finish, group, subject);

        Set<AttendanceValue> values = new HashSet<>();
        Collections.addAll(values, AttendanceValue.ONE_HOUR, AttendanceValue.TWO_HOUR);
        List<Attendance> attendances = attendanceRepository.findAllByStudentInAndLessonInAndValueIn(students, new HashSet<>(lessons), values);

        Set<PersonDto> studentSet = students.stream()
                .map(s -> personConverter.convertToDto(s))
                .collect(Collectors.toSet());

        Set<SubjectTypeConstant> subjectTypeConstants = lessons.stream()
                .map(l -> l.getSubjectType())
                .collect(Collectors.toSet());

        ReportByGroup report = new ReportByGroup(subjectTypeConstants, studentSet);

        attendances.forEach(a -> report.addHours(personConverter.convertToDto(a.getStudent()), a.getLesson().getSubjectType(), a.getValue().getHours(), a.isGoodReason()));
        lessons.forEach(l -> report.addLessonTotal(l.getSubjectType()));

        return report.toStringGrid();
    }

    public List<List<String>> findGridForStudentReport(Long groupId) {
        List<StudentWithParentDto> students = studentService.findStudentsByGroup(ObjectRef.toObjectRef(groupId));
        List<List<String>> result = new ArrayList<>();
        result.add(Arrays.asList("ФИО", "Подгр.", "Email", "Email  родителя"));
        for (StudentWithParentDto stud : students) {
            result.add(Arrays.asList(stud.getFullName(), GroupVolumeConstant.find(stud.getGroupVolume()).get().getRuText(), stud.getEmail(), stud.getParentEmail()));
        }
        return result;
    }

    public List<List<String>> findGridForProfessorReport() {
        List<ProfessorDto> profs = professorService.findAll();
        List<List<String>> result = new ArrayList<>();
        result.add(Arrays.asList("ФИО", "Должность", "Email", "Телефон"));
        for (ProfessorDto prof : profs) {
            List<String> row = Arrays.asList(prof.getFullName(), prof.getPosition().getQualifier(), prof.getEmail(), prof.getPhone());
            for (String col : row) {
                if (col == null) {
                    col = "";
                }
            }
            result.add(row);
        }
        return result;
    }

}
