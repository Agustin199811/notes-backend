package com.uce.notes.Services;

import com.uce.notes.Model.Rol;

import java.util.List;

public interface RolService {
    Rol createRol(Rol rol);

    List<Rol> getAllRol();
}
