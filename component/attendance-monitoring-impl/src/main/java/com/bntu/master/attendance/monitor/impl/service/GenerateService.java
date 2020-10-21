package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.api.model.SubjectTypeConstant;
import com.bntu.master.attendance.monitor.api.model.attendance.AttendanceValue;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.impl.dataaccess.AttendanceRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.GroupRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.LessonRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.LessonScheduleRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.MarkRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.PersonRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.RoleRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.SpecialityRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.StudentGroupRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.SubjectRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.UserRepository;
import com.bntu.master.attendance.monitor.impl.entity.Attendance;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Lesson;
import com.bntu.master.attendance.monitor.impl.entity.LessonSchedule;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.Role;
import com.bntu.master.attendance.monitor.impl.entity.Speciality;
import com.bntu.master.attendance.monitor.impl.entity.StudentGroup;
import com.bntu.master.attendance.monitor.impl.entity.Subject;
import com.bntu.master.attendance.monitor.impl.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenerateService {

    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private MarkRepository markRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SpecialityRepository specialityRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private LessonScheduleRepository lessonScheduleRepository;
    @Autowired
    private StudentGroupRepository studentGroupRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    Random random = new Random();

    private List<String> names = new ArrayList<>();
    private List<String> lastName = new ArrayList<>();
    private List<String> patronymic = new ArrayList<>();
    private List<String> subject = new ArrayList<>();


    private void loadAll() {
        try{
//            names = new ObjectMapper().readValue(new File("D:/maga/attendance-monitoring/component/attendance-monitoring-impl/src/main/resources/dataToGenerate/names.json"), List.class);
//            lastName = new ObjectMapper().readValue(new File("D:/maga/attendance-monitoring/component/attendance-monitoring-impl/src/main/resources/dataToGenerate/lastNames.json"), List.class);
//            patronymic = new ObjectMapper().readValue(new File("D:/maga/attendance-monitoring/component/attendance-monitoring-impl/src/main/resources/dataToGenerate/patronymic.json"), List.class);
//            subject = new ObjectMapper().readValue(new File("D:/maga/attendance-monitoring/component/attendance-monitoring-impl/src/main/resources/dataToGenerate/subject.json"), List.class);

            names = new ObjectMapper().readValue(new File("/home/nikskonda/Documents/att-monitor/attendance-monitor-back/component/attendance-monitoring-impl/src/main/resources/dataToGenerate/names.json"), List.class);
            lastName = new ObjectMapper().readValue(new File("/home/nikskonda/Documents/att-monitor/attendance-monitor-back/component/attendance-monitoring-impl/src/main/resources/dataToGenerate/lastNames.json"), List.class);
            patronymic = new ObjectMapper().readValue(new File("/home/nikskonda/Documents/att-monitor/attendance-monitor-back/component/attendance-monitoring-impl/src/main/resources/dataToGenerate/patronymic.json"), List.class);
            subject = new ObjectMapper().readValue(new File("/home/nikskonda/Documents/att-monitor/attendance-monitor-back/component/attendance-monitoring-impl/src/main/resources/dataToGenerate/subject.json"), List.class);

        }catch (Exception ex) {
        }
    }

    private String get(List<String> list, boolean removeThen) {
        int i = random.nextInt(list.size());
        String result = list.get(i);
        if (removeThen) {
            list.remove(i);
        }
        return result;
    }

    public List<LessonSchedule> createBase() {
        roleRepository.save(new Role(RoleConstant.STUDENT.getId(), RoleConstant.STUDENT.getRole()));
        roleRepository.save(new Role(RoleConstant.PROFESSOR.getId(), RoleConstant.PROFESSOR.getRole()));
        roleRepository.save(new Role(RoleConstant.PARENT.getId(), RoleConstant.PARENT.getRole()));
        Role role4 = roleRepository.save(new Role(RoleConstant.ADMIN.getId(), RoleConstant.ADMIN.getRole()));

        Set<Role> adminRole = new HashSet<>();
        adminRole.add(role4);
        personRepository.save(new Person(null, "admin", "admin", "admin", "admin@bntu.by"));
        userRepository.save(new User("admin@bntu.by", bCryptPasswordEncoder.encode("admin"), LocalDate.now().plusYears(10), adminRole));

        LocalTime startTime = LocalTime.of(8, 0, 0 ,0);
        List<LessonSchedule> lessonSchedules = new ArrayList<>();
        for (int i = 0; i<10; i++) {
            if (i==5) {
                startTime = LocalTime.of(12, 10, 0 ,0);
            }
            LessonSchedule schedule = new LessonSchedule();
            schedule.setOrder(i+1L);
            schedule.setStartTime(startTime);
            schedule.setFinishTime(startTime = startTime.plusMinutes(95));
            schedule.setShift(i<5?"1":"2");
            lessonSchedules.add(schedule);
            startTime = startTime.plusMinutes(15);
        }
        return lessonScheduleRepository.saveAll(lessonSchedules);
    }

    public void create() {
        List<LessonSchedule> lessonSchedules = lessonScheduleRepository.findAll();
        loadAll();

        Role role1 = roleRepository.findById(RoleConstant.STUDENT.getId()).get();
        Role role2 = roleRepository.findById(RoleConstant.PROFESSOR.getId()).get();
        Role role3 = roleRepository.findById(RoleConstant.PARENT.getId()).get();
        Role role4 = roleRepository.findById(RoleConstant.ADMIN.getId()).get();


        Speciality speciality1 = new Speciality(null, "ПОИТ");
        Speciality speciality2 = new Speciality(null, "ИСИТ");
        Speciality speciality3 = new Speciality(null, "ПОИСИТ");
        speciality1 = specialityRepository.save(speciality1);
        speciality2 = specialityRepository.save(speciality2);
        speciality3 = specialityRepository.save(speciality3);

        List<Group> groups = new ArrayList<>();
        groups.add(new Group(null, "10701115", speciality1));
        groups.add(new Group(null, "10701215", speciality1));
        groups.add(new Group(null, "10702115", speciality2));
        groups.add(new Group(null, "10702215", speciality2));
        groups.add(new Group(null, "10703116", speciality3));
        groups.add(new Group(null, "10703215", speciality3));
        groups = groupRepository.saveAll(groups);

        List<Person> studs = new ArrayList<>();
        List<User> users = new ArrayList<>();
        Set<Role> studRole = new HashSet<>();
        studRole.add(role1);
        for (int i = 0; i < 130; i++) {
            String name = "stud" + i;
            String email = "stud" + i + "@bntu.by";
            studs.add(new Person(null, get(names, false), get(lastName, false), get(patronymic, false), email));
            users.add(new User(email, bCryptPasswordEncoder.encode(name), LocalDate.now().plusYears(1), studRole));
        }
        studs = personRepository.saveAll(studs);
        Set<StudentGroup> studentGroups = new HashSet<>();
        for (Person p : studs) {
            studentGroups.add(new StudentGroup(p, groups.get(random.nextInt(groups.size()))));
        }
        studentGroupRepository.saveAll(studentGroups);
        List<Person> people = new ArrayList<>();
        Set<Role> profRole = new HashSet<>();
        profRole.add(role2);
        List<Person> profs = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            String name = "prof" + i;
            String email = name + "@bntu.by";
            profs.add(new Person(null, get(names, false), get(lastName, false), get(patronymic, false), email));
            users.add(new User(email, bCryptPasswordEncoder.encode(name), LocalDate.now().plusYears(1), profRole));
        }
        Set<Role> parentRole = new HashSet<>();
        parentRole.add(role3);
        for (int i = 0; i < 20; i++) {
            String name = "parent" + i;
            String email = name + "@bntu.by";
            people.add(new Person(null, get(names, false), get(lastName, false), get(patronymic, false), email));
            users.add(new User(email, bCryptPasswordEncoder.encode(name), LocalDate.now().plusYears(1), parentRole));
        }

        people = personRepository.saveAll(people);
        profs = personRepository.saveAll(profs);
        userRepository.saveAll(users);

        List<Subject> subjects = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            Subject subject = new Subject();
            subject.setName(get(this.subject, true));
            subjects.add(subject);
        }
        subjects = subjectRepository.saveAll(subjects);

        List<Lesson> lessons = new ArrayList<>();
        for (Person prof : profs) {
            LocalDate date = LocalDate.of(2020, 9, 1);
            date = date.minusDays(date.getDayOfWeek().getValue() - 1);
            for (int i = 0; i < 16; i++) {
                for (int dayOfWeek = 0; dayOfWeek < 6; dayOfWeek++) {
                    for (LessonSchedule lessonSchedule : lessonSchedules.subList(0, 5)) {
                        if (random.nextInt(100)>55) continue;
                        lessons.add(new Lesson(null,
                                date,
                                lessonSchedule,
                                prof,
                                groups.get(random.nextInt(groups.size())),
                                subjects.get(random.nextInt(subjects.size())),
                                SubjectTypeConstant.values()[random.nextInt(SubjectTypeConstant.values().length)]
                        ));
                    }
                    date = date.plusDays(1);
                }
                date = date.plusDays(1);
            }
        }

        lessons = lessonRepository.saveAll(lessons);

        List<Attendance> attendances = new ArrayList<>();
        for (Lesson lesson : lessons) {
            long j = 0;
            if (lesson.getDate().isAfter(LocalDate.now())) continue;
            for (Person student : findByGroup(lesson.getGroup())) {
                attendances.add(new Attendance(null, lesson,
                        student, getRandomAttendance(),
                        lesson.getProfessor(),
                        LocalDateTime.of(lesson.getDate(), lesson.getTime().getStartTime().plusMinutes(j++)),
                        random.nextBoolean()));
            }
        }
        attendances = attendanceRepository.saveAll(attendances);

    }

    private AttendanceValue getRandomAttendance() {
        return random.nextInt(100) > 70 ? random.nextInt(100) > 70 ? AttendanceValue.ONE_HOUR : AttendanceValue.TWO_HOUR : AttendanceValue.COME;
    }

    private Set<Person> findByGroup(Group group) {
        return studentGroupRepository.findAllByGroup(group).stream().map(StudentGroup::getStudent).collect(Collectors.toSet());
    }


}
