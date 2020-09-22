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
import com.bntu.master.attendance.monitor.impl.dataaccess.SubjectRepository;
import com.bntu.master.attendance.monitor.impl.dataaccess.UserRepository;
import com.bntu.master.attendance.monitor.impl.entity.Attendance;
import com.bntu.master.attendance.monitor.impl.entity.Group;
import com.bntu.master.attendance.monitor.impl.entity.Lesson;
import com.bntu.master.attendance.monitor.impl.entity.LessonSchedule;
import com.bntu.master.attendance.monitor.impl.entity.Person;
import com.bntu.master.attendance.monitor.impl.entity.Role;
import com.bntu.master.attendance.monitor.impl.entity.Speciality;
import com.bntu.master.attendance.monitor.impl.entity.Subject;
import com.bntu.master.attendance.monitor.impl.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    Random random = new Random();

    public void create() {
        Role role1 = new Role(RoleConstant.STUDENT.getId(), RoleConstant.STUDENT.getRole());
        Role role2 = new Role(RoleConstant.PROFESSOR.getId(), RoleConstant.PROFESSOR.getRole());
        Role role3 = new Role(RoleConstant.PARENT.getId(), RoleConstant.PARENT.getRole());
        role1 = roleRepository.save(role1);
        role2 = roleRepository.save(role2);
        role3 = roleRepository.save(role3);
        Role role4 = roleRepository.save(new Role(RoleConstant.ADMIN.getId(), RoleConstant.ADMIN.getRole()));


        Speciality speciality1 = new Speciality(null, "spec1");
        Speciality speciality2 = new Speciality(null, "spec2");
        Speciality speciality3 = new Speciality(null, "spec3");
        speciality1 = specialityRepository.save(speciality1);
        speciality2 = specialityRepository.save(speciality2);
        speciality3 = specialityRepository.save(speciality3);

        List<Group> groups = new ArrayList<>();
        groups.add(new Group(null, "group1_spec1", speciality1));
        groups.add(new Group(null, "group2_spec1", speciality1));
        groups.add(new Group(null, "group3_spec2", speciality2));
        groups.add(new Group(null, "group4_spec2", speciality2));
        groups.add(new Group(null, "group5_spec3", speciality3));
        groups = groupRepository.saveAll(groups);

        List<Person> people = new ArrayList<>();
        List<User> users = new ArrayList<>();
        Set<Role> studRole = new HashSet<>();
        studRole.add(role1);
        for (int i = 0; i < 50; i++) {
            Long id = i + 1L;
            String name = "stud" + id;
            people.add(new Person(null, name, name, name, name, studRole, groups.get(i / 10)));
            users.add(new User(name, bCryptPasswordEncoder.encode(name)));
        }
        Set<Role> profRole = new HashSet<>();
        profRole.add(role2);
        List<Person> profs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Long id = people.size() + 1L + i;
            String name = "prof" + id;
            profs.add(new Person(null, name, name, name, name, profRole, null));
            users.add(new User(name, bCryptPasswordEncoder.encode(name)));
        }
        Set<Role> parentRole = new HashSet<>();
        parentRole.add(role3);
        for (int i = 0; i < 20; i++) {
            Long id = people.size() + profs.size() + 1L;
            String name = "parent" + id;
            people.add(new Person(null, name, name, name, name, parentRole, null));
            users.add(new User(name, bCryptPasswordEncoder.encode(name)));
        }
        Set<Role> adminRole = new HashSet<>();
        adminRole.add(role4);
        personRepository.save(new Person(null, "admin", "admin", "admin", "admin", adminRole, null));
        users.add(new User("admin", bCryptPasswordEncoder.encode("admin")));
        people = personRepository.saveAll(people);
        profs = personRepository.saveAll(profs);
        userRepository.saveAll(users);

        List<LessonSchedule> lessonSchedules = new ArrayList<>();
        for (int i = 0; i<5; i++) {
            LessonSchedule schedule = new LessonSchedule();
            schedule.setOrder(i+1L);
            LocalTime startTime = LocalTime.of(8 + i*2, 30, 0 ,0);
            schedule.setStartTime(startTime);
            schedule.setFinishTime(startTime.plusMinutes(95));
            lessonSchedules.add(schedule);
        }
        lessonScheduleRepository.saveAll(lessonSchedules);

        List<Subject> subjects = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Subject subject = new Subject();
            subject.setName("subject " + i);
            subjects.add(subject);
        }
        subjects = subjectRepository.saveAll(subjects);

        List<Lesson> lessons = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Long id = i + 1L;
            LocalDate date = LocalDate.now().minusDays(7L).plusDays(id / 5);
            if (random.nextInt(100)<75)
            lessons.add(new Lesson(null, date,
                    LocalDateTime.of(date, lessonSchedules.get((int)(id % 5)).getStartTime()),
                    LocalDateTime.of(date, lessonSchedules.get((int)(id % 5)).getFinishTime()),
                    profs.get(random.nextInt(profs.size())),
                    groups.get(random.nextInt(groups.size())),
                    subjects.get(random.nextInt(subjects.size())),
                    SubjectTypeConstant.values()[random.nextInt(SubjectTypeConstant.values().length)]
                    ));
        }
        lessons = lessonRepository.saveAll(lessons);

        List<Attendance> attendances = new ArrayList<>();
        for (Lesson lesson : lessons) {
            long j = 0;
            for (Person student : findByGroup(lesson.getGroup(), people, role1)) {
                attendances.add(new Attendance(null, lesson,
                        student, getRandomAttendance(),
                        profs.get(random.nextInt(profs.size())),
                        lesson.getStartTime().plusMinutes(j++)));
            }
        }
        attendances = attendanceRepository.saveAll(attendances);

    }

    private AttendanceValue getRandomAttendance() {
        return AttendanceValue.values()[random.nextInt(AttendanceValue.values().length)];
    }

    private List<Person> findByGroup(Group group, List<Person> people, Role role) {
        List<Person> result = new ArrayList<>();
        for (Person person : people) {
            if (person.getGroup() != null && person.getGroup().getId().equals(group.getId()) && person.getRoles().contains(role)) {
                result.add(person);
            }
        }
        return result;
    }


}
