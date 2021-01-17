package com.bntu.master.attendance.monitor.impl.service;

import com.bntu.master.attendance.monitor.api.model.GroupVolumeConstant;
import com.bntu.master.attendance.monitor.api.model.RoleConstant;
import com.bntu.master.attendance.monitor.api.model.SubjectTypeConstant;
import com.bntu.master.attendance.monitor.api.model.attendance.AttendanceValue;
import com.bntu.master.attendance.monitor.impl.dataaccess.AccountRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.AttendanceRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.GroupRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.LessonRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.LessonScheduleRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.MarkRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.ParentContactRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.PersonRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.PositionRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.ProfessorPositionRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.RoleRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.SpecialityRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.StudentGroupRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.SubjectRepository;
import com.bntu.master.attendance.monitor.impl.entity.Account;
import com.bntu.master.attendance.monitor.impl.entity.Attendance;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Lesson;
import com.bntu.master.attendance.monitor.impl.entity.LessonSchedule;
import com.bntu.master.attendance.monitor.impl.entity.ParentContact;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.Position;
import com.bntu.master.attendance.monitor.impl.entity.ProfessorPosition;
import com.bntu.master.attendance.monitor.impl.entity.Role;
import com.bntu.master.attendance.monitor.impl.entity.Speciality;
import com.bntu.master.attendance.monitor.impl.entity.StudentGroup;
import com.bntu.master.attendance.monitor.impl.entity.Subject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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
    private AccountRepository accountRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private LessonScheduleRepository lessonScheduleRepository;
    @Autowired
    private StudentGroupRepository studentGroupRepository;
    @Autowired
    private ParentContactRepository parentContactRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private PositionRepository positionRepository;
    @Autowired
    private ProfessorPositionRepository professorPositionRepository;

    private Map<Integer, List<Person>> profMap = new HashMap<>();

    Random random = new Random();

    private Map<String, Object> names = new HashMap<>();
    private Map<String, Object> lastName = new HashMap<>();
    private Map<String, Object> patronymic = new HashMap<>();
    private List<String> subject = new ArrayList<>();


    private void loadAll() {
        try {
            names = new ObjectMapper().readValue(new File("D:/maga/attendance-monitoring/component/attendance-monitoring-impl/src/main/resources/dataToGenerate/names.json"), Map.class);
            lastName = new ObjectMapper().readValue(new File("D:/maga/attendance-monitoring/component/attendance-monitoring-impl/src/main/resources/dataToGenerate/lastNames.json"), Map.class);
            patronymic = new ObjectMapper().readValue(new File("D:/maga/attendance-monitoring/component/attendance-monitoring-impl/src/main/resources/dataToGenerate/patronymic.json"), Map.class);
            subject = new ObjectMapper().readValue(new File("D:/maga/attendance-monitoring/component/attendance-monitoring-impl/src/main/resources/dataToGenerate/subject.json"), List.class);

//            names = new ObjectMapper().readValue(new File("/home/nikskonda/Documents/att-monitor/attendance-monitor-back/component/attendance-monitoring-impl/src/main/resources/dataToGenerate/names.json"), List.class);
//            lastName = new ObjectMapper().readValue(new File("/home/nikskonda/Documents/att-monitor/attendance-monitor-back/component/attendance-monitoring-impl/src/main/resources/dataToGenerate/lastNames.json"), List.class);
//            patronymic = new ObjectMapper().readValue(new File("/home/nikskonda/Documents/att-monitor/attendance-monitor-back/component/attendance-monitoring-impl/src/main/resources/dataToGenerate/patronymic.json"), List.class);
//            subject = new ObjectMapper().readValue(new File("/home/nikskonda/Documents/att-monitor/attendance-monitor-back/component/attendance-monitoring-impl/src/main/resources/dataToGenerate/subject.json"), List.class);

        } catch (Exception ex) {
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
    List<Integer> sums = new ArrayList<>();
    private String getPopular(List<String> list) {
        if (sums.isEmpty()) {
            sums.add(1);
            for(int i = 1; i<100; i++) {
                sums.add(sums.get(i-1) + i);
            }
        }
        int numb = random.nextInt(sums.get(list.size()-1));
        for (int i = 0; i<list.size() - 1; i++) {
            if (numb > sums.get(list.size() - i - 2)) {
                return list.get(i);
            }
        }
        return list.get(0);
    }

    public void createBase() {
        if (roleRepository.findAll().size() > 0) {
            System.out.println("READY!");
            return;
        }
        System.out.println("CREATION ...");
        Set<Role> adminRole = new HashSet<>();
        adminRole.add(roleRepository.save(new Role(null, RoleConstant.STUDENT.getRole())));
        adminRole.add(roleRepository.save(new Role(null, RoleConstant.PROFESSOR.getRole())));
        adminRole.add(roleRepository.save(new Role(null, RoleConstant.PARENT.getRole())));
        adminRole.add(roleRepository.save(new Role(null, RoleConstant.EDITOR.getRole())));
        adminRole.add(roleRepository.save(new Role(null, RoleConstant.REPORT_VIEW.getRole())));
//        Role role4 = roleRepository.save(new Role(RoleConstant.ADMIN.getId(), RoleConstant.ADMIN.getRole()));
//        adminRole.add(role4);
        personRepository.save(new Person(null, "Никита", "Андреевич", "Шконда", "admin@bntu.by", null));
        accountRepository.save(new Account("admin@bntu.by", bCryptPasswordEncoder.encode("pass"+"admin"), LocalDate.now().plusYears(10), adminRole));

        LocalTime startTime = LocalTime.of(8, 0, 0, 0);
        List<LessonSchedule> lessonSchedules = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                startTime = LocalTime.of(12, 10, 0, 0);
            }
            LessonSchedule schedule = new LessonSchedule();
            schedule.setOrder(i + 1L);
            schedule.setStartTime(startTime);
            schedule.setFinishTime(startTime = startTime.plusMinutes(95));
            schedule.setShift(i < 5 ? "1" : "2");
            lessonSchedules.add(schedule);
            startTime = startTime.plusMinutes(15);
        }
        lessonScheduleRepository.saveAll(lessonSchedules);
        System.out.println("READY!");
    }

    public void create() {
        createBase();
        System.out.println("CREATION ...");
        List<LessonSchedule> lessonSchedules = lessonScheduleRepository.findAll();
        loadAll();

        Role role1 = roleRepository.findById(RoleConstant.STUDENT.getId()).get();
        Role role2 = roleRepository.findById(RoleConstant.PROFESSOR.getId()).get();
        Role role3 = roleRepository.findById(RoleConstant.PARENT.getId()).get();
        Role role4 = roleRepository.findById(RoleConstant.REPORT_VIEW.getId()).get();


        Speciality speciality1 = new Speciality(null, "ПОИТ");
        Speciality speciality2 = new Speciality(null, "ИСИТ");
        speciality1 = specialityRepository.save(speciality1);
        speciality2 = specialityRepository.save(speciality2);

        List<Group> groups = new ArrayList<>();
        groups.add(new Group(null, "10701119", speciality1));
        groups.add(new Group(null, "10701219", speciality1));
        groups.add(new Group(null, "10702119", speciality2));
        groups.add(new Group(null, "10702219", speciality2));
        groups.add(new Group(null, "10701120", speciality1));
        groups.add(new Group(null, "10701220", speciality1));
        groups.add(new Group(null, "10702120", speciality2));
        groups.add(new Group(null, "10702220", speciality2));
        groups = groupRepository.saveAll(groups);

        List<Position> positions = new ArrayList<>();
        positions.add(new Position(null, "Заведающий кафедрой"));
        positions.add(new Position(null, "Профессор"));
        positions.add(new Position(null, "Доцент"));
        positions.add(new Position(null, "Старший преподаватель"));
        positions.add(new Position(null, "Преподаватель"));
        positions.add(new Position(null, "Accистент"));

        positions = positionRepository.saveAll(positions);

        List<StudentGroup> studs = new ArrayList<>();
        List<ParentContact> parents = new ArrayList<>();
        List<Account> accounts = new ArrayList<>();
        Set<Role> studRole = new HashSet<>();
        studRole.add(role1);
        Set<Role> parentRole = new HashSet<>();
        parentRole.add(role3);
        List<String> pols = Arrays.asList("male", "female");
        for (int i = 0; i < groups.size() * 20; i++) {
            String name = "stud" + i;
            String email = "stud" + i + "@bntu.by";
            String pol = get(pols, false);
            String fname = getPopular((List<String>)names.get(pol));
            String sname = get((List<String>)lastName.get(pol), false);
            String pname = get((List<String>)patronymic.get(pol), false);
            Person stud = personRepository.save(new Person(null, fname, sname, pname, email, null));
            accounts.add(new Account(email, bCryptPasswordEncoder.encode("pass"+name), LocalDate.now().plusYears(1), studRole));
            studs.add(new StudentGroup(groups.get(i/25), stud,
                    GroupVolumeConstant.values()[i%25/12 + 1].getId()));
            if (random.nextInt(100) > 20) {
                Account parent = accountRepository.save(new Account("parent" + i + "@bntu.by", bCryptPasswordEncoder.encode("pass"+"parent" + i), LocalDate.now().plusYears(1), parentRole));
                parents.add(new ParentContact(parent, stud));
            }
        }
        studentGroupRepository.saveAll(studs);
        parentContactRepository.saveAll(parents);

        Set<Role> profRole = new HashSet<>();
        profRole.add(role2);
        List<ProfessorPosition> profs = new ArrayList<>();

        List<String> codes = Arrays.asList("29", "44", "33");
        for (int i = 0; i < 20; i++) {
            String name = "prof" + i;
            String email = name + "@bntu.by";
            String phone = RandomStringUtils.randomNumeric(7);
            if (StringUtils.isNotBlank(phone)) {
                phone = phone.replaceAll("\\D", "");
                phone = (String.format("(%s) %s-%s-%s", get(codes, false), phone.substring(0, 3), phone.substring(3, 5), phone.substring(5)));
            }

            String pol = get(pols, false);
            String fname = getPopular((List<String>)names.get(pol));
            String sname = get((List<String>)lastName.get(pol), false);
            String pname = get((List<String>)patronymic.get(pol), false);

            Person prof = personRepository.save(new Person(
                    null,
                    fname,
                    sname,
                    pname,
                    email,
                    phone));
            accounts.add(new Account(email, bCryptPasswordEncoder.encode("pass"+name), LocalDate.now().plusYears(1), profRole));
            profs.add(new ProfessorPosition(positions.get(random.nextInt(positions.size())), prof));
        }

        profs = professorPositionRepository.saveAll(profs);
        accountRepository.saveAll(accounts);

        List<Subject> subjects = new ArrayList<>();
        for (String s : subject) {
            Subject sub = new Subject();
            sub.setName(s);
            subjects.add(sub);
        }
        subjects = subjectRepository.saveAll(subjects);


        List<Lesson> lessons = new ArrayList<>();

        for (int dayOfWeek = 0; dayOfWeek < 13; dayOfWeek++) {
            if (dayOfWeek == 6) continue;
            for (LessonSchedule lessonSchedule : lessonSchedules.subList(0, 5)) {
                List<ProfessorPosition> profList = new ArrayList(profs);
                for (Group group : groups) {
                    if (random.nextInt(100) > 75) continue;
                    ProfessorPosition prof = profList.get(random.nextInt(profList.size()));
                    profList.remove(prof);
                    GroupVolumeConstant volume = GroupVolumeConstant.values()[random.nextInt(GroupVolumeConstant.values().length)];
                    Subject subject = subjects.get(random.nextInt(subjects.size()));
                    SubjectTypeConstant subjectTypeConstant = GroupVolumeConstant.FULL.equals(volume) ?
                            SubjectTypeConstant.LECTURE :
                            SubjectTypeConstant.values()[random.nextInt(SubjectTypeConstant.values().length - 1) + 1];

                    for (int week = 0; week < 10; week++) {
                        LocalDate date = LocalDate.of(2020, 11, 2);
                        date = date.plusWeeks(week * 2);
                        date = date.plusDays(dayOfWeek);
                        lessons.add(new Lesson(null,
                                date,
                                lessonSchedule,
                                prof,
                                group,
                                volume.getId(),
                                subject,
                                subjectTypeConstant
                        ));
                    }


                }
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
        System.out.println("CREATED!");

    }

    private AttendanceValue getRandomAttendance() {
        return random.nextInt(100) > 70 ? random.nextInt(100) > 70 ? AttendanceValue.ONE_HOUR : AttendanceValue.TWO_HOUR : AttendanceValue.COME;
    }

    private Set<Person> findByGroup(Group group) {
        return studentGroupRepository.findAllByGroup(group).stream().map(StudentGroup::getStudent).collect(Collectors.toSet());
    }


}
