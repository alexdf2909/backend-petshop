package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.UsuarioDTO;
import com.grupo8.petshop.entity.Usuario;
import com.grupo8.petshop.service.IUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private final IUsuarioService usuarioService;

    public UsuarioController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuarioARetornar = usuarioService.createUsuario(usuarioDTO);
        if (usuarioARetornar == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioARetornar);
        }
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> traerTodos() {
        return ResponseEntity.ok(usuarioService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.searchForId(id);
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) {
        try {
            usuarioService.updateUsuario(id, usuarioDTO);
            return ResponseEntity.ok("{\"message\": \"Usuario modificada\"}");
        } catch (RuntimeException e) {
            return new ResponseEntity<>("{\"message\": \"" + e.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarUsuario(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = usuarioService.searchForId(id);
        if (usuarioOptional.isPresent()) {
            usuarioService.deleteUsuario(id);
            return ResponseEntity.ok("{\"message\": \"usuario eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"usuario no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }

    // Agregar favorito
    @PostMapping("/{usuarioId}/favorito/{productoId}")
    public ResponseEntity<Usuario> addFavorite(@PathVariable Long usuarioId, @PathVariable Long productoId) {
        Optional<Usuario> updatedUser = usuarioService.addFavorite(usuarioId, productoId);
        if (updatedUser.isPresent()) {
            return ResponseEntity.ok(updatedUser.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Eliminar favorito
    @DeleteMapping("/{usuarioId}/favorito/{productoId}")
    public ResponseEntity<Usuario> removeFavorite(@PathVariable Long usuarioId, @PathVariable Long productoId) {
        Optional<Usuario> updatedUser = usuarioService.removeFavorite(usuarioId, productoId);
        if (updatedUser.isPresent()) {
            return ResponseEntity.ok(updatedUser.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
