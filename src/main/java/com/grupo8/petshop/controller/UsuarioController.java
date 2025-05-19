package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.entidades.UsuarioDTO;
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
            System.out.println("controlador");
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
    @PostMapping("/favorito/{productoId}")
    public ResponseEntity<Usuario> addFavorite(@PathVariable Long productoId) {
        Optional<Usuario> updatedUser = usuarioService.addFavorite(productoId);
        if (updatedUser.isPresent()) {
            return ResponseEntity.ok(updatedUser.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Eliminar favorito
    @DeleteMapping("/favorito/{productoId}")
    public ResponseEntity<Usuario> removeFavorite(@PathVariable Long productoId) {
        Optional<Usuario> updatedUser = usuarioService.removeFavorite(productoId);
        if (updatedUser.isPresent()) {
            return ResponseEntity.ok(updatedUser.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Endpoint para ver el perfil del cliente autenticado
    @GetMapping("/perfil")
    public ResponseEntity<Usuario> obtenerPerfilCliente() {
        return usuarioService.getPerfilCliente()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para editar el perfil del cliente autenticado
    @PutMapping("/perfil")
    public ResponseEntity<Usuario> editarPerfilCliente(@RequestBody Usuario usuarioActualizado) {
        try {
            Usuario usuario = usuarioService.actualizarPerfilCliente(usuarioActualizado);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
