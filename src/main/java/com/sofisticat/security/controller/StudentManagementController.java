package com.sofisticat.security.controller;

import com.sofisticat.security.model.Student;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("management/api/v1/students")
public class StudentManagementController {
    static Logger logger = Logger.getLogger(StudentManagementController.class.getName());

    private static List<Student> students = new LinkedList<>(Arrays.asList(
            new Student(1, "John Doe"),
            new Student(2, "Barbara Palvin"),
            new Student(3, "Serhat Uyanmis")
    ));


    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ADMINTRAINEE')")
    public List<Student> getAllStudents() {
        return students;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('student:write')")
    public void registerNewStudent(@RequestBody Student student) {
        logger.info("Students : " + students);
        logger.info("Student: " + student);
        students.add(new Student(student.getStudentId(), student.getStudentName()));
    }

    @DeleteMapping("/{studentId}")
    @PreAuthorize("hasAnyAuthority('student:write')")
    public void deleteStudent(@PathVariable long studentId) {
        students = students.stream()
                .filter(i -> i.getStudentId() != studentId)
                .collect(Collectors.toList());

    }

    @PutMapping("/{studentId}")
    @PreAuthorize("hasAnyAuthority('student:write')")
    public void updateStudent(@PathVariable long studentId, @RequestBody Student student) {
        logger.info("Student id to update: " + studentId);
        List<Student> studentsFiltered = students.stream().filter(i -> i.getStudentId() != studentId).collect(Collectors.toList());
        studentsFiltered.add(student);
        students = studentsFiltered;
        logger.info("Students are updated: " + students);
    }


}
