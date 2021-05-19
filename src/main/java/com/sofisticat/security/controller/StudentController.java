package com.sofisticat.security.controller;

import com.sofisticat.security.model.Student;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {

    private static final List<Student> students = Arrays.asList(
            new Student(1, "John Doe"),
            new Student(2, "Barbara Palvin"),
            new Student(3, "Serhat Uyanmis")
    );

    @RequestMapping("/{studentId}")
    public Student getStudent(@PathVariable long studentId) {
        return students.stream()
                .filter(s -> s.getStudentId() == studentId)
                .findFirst().orElseThrow(RuntimeException::new);
    }
}
