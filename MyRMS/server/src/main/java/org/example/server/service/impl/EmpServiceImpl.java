package org.example.server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.pojo.entity.Emp;
import org.example.server.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.server.mapper.EmpMapper;

@Slf4j
@Service
public class EmpServiceImpl implements EmpService {
    private final EmpMapper empMapper;

    @Autowired
    public EmpServiceImpl(EmpMapper empMapper) {
        // フィールドにインターセプターを設定
        this.empMapper = empMapper;
    }

    @Override
    public Emp login(Emp emp) {
        return empMapper.has_employee(emp);
    }
}


