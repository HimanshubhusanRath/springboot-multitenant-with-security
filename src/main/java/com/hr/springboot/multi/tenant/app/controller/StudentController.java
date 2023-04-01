package com.hr.springboot.multi.tenant.app.controller;

import com.hr.springboot.multi.tenant.app.domain.Student;
import com.hr.springboot.multi.tenant.app.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentRepository repository;

    @PostMapping(path = "/add")
    public ResponseEntity<?> addStudent()
    {
        final Student student = new Student();
        student.setName("Himanshu");
        student.setCity("Puri");
        repository.save(student);
        return ResponseEntity.ok(student);
    }
}
