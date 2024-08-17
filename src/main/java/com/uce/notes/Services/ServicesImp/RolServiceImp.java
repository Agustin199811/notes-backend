package com.uce.notes.Services.ServicesImp;

import com.uce.notes.Model.Rol;
import com.uce.notes.Repository.RolRepository;
import com.uce.notes.Services.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolServiceImp implements RolService {
    @Autowired
    private RolRepository rolRepository;

    @Override
    public Rol createRol(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public List<Rol> getAllRol() {
        return rolRepository.findAll();
    }
}