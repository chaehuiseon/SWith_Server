package com.swith.domain.register.service;


import com.swith.domain.register.entity.Register;
import com.swith.domain.register.repository.RegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegisterService {

    private final RegisterRepository registerRepository;

    @Autowired
    public RegisterService(RegisterRepository registerRepository) {
        this.registerRepository = registerRepository;
    }

    @Transactional
    public Register save(Register register){
        return registerRepository.save(register);

    }
}
