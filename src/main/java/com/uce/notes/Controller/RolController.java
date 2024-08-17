package com.uce.notes.Controller;

import com.uce.notes.Model.Rol;
import com.uce.notes.Services.RolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/roles")
public class RolController {
    @Autowired
    private RolService rolService;

    @PostMapping
    public ResponseEntity<?> createRol(@RequestBody Rol rol){
        return ResponseEntity.ok(rolService.createRol(rol));
    }

    @GetMapping
    public ResponseEntity<?> getAllRole(){
        return ResponseEntity.ok(rolService.getAllRol());
    }
}
