package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.entidades.HistorialInteraccionDTO;
import com.grupo8.petshop.entity.HistorialInteraccion;
import com.grupo8.petshop.entity.Producto;
import com.grupo8.petshop.entity.Usuario;
import com.grupo8.petshop.repository.IHistorialInteraccionRepository;
import com.grupo8.petshop.repository.IProductoRepository;
import com.grupo8.petshop.repository.IUsuarioRepository;
import com.grupo8.petshop.service.IHistorialInteraccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HistorialInteraccionService implements IHistorialInteraccionService {
    private final IHistorialInteraccionRepository historialInteraccionRepository;
    @Autowired
    private IUsuarioRepository usuarioRepository;
    @Autowired
    private IProductoRepository productoRepository;

    public HistorialInteraccionService(IHistorialInteraccionRepository historialInteraccionRepository) {
        this.historialInteraccionRepository = historialInteraccionRepository;
    }

    @Override
    public HistorialInteraccion createHistorialInteraccion(HistorialInteraccionDTO historialInteraccionDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuario = authentication.getName();

        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Producto producto = productoRepository.findById(historialInteraccionDTO.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        HistorialInteraccion historialInteraccion = new HistorialInteraccion();
        historialInteraccion.setTipoInteraccion(historialInteraccionDTO.getTipoInteraccion());
        historialInteraccion.setFecha(LocalDateTime.now());
        historialInteraccion.setUsuario(usuario);
        historialInteraccion.setProducto(producto);

        return historialInteraccionRepository.save(historialInteraccion);
    }

    @Override
    public Optional<HistorialInteraccion> searchForId(Long id) {
        return historialInteraccionRepository.findById(id);
    }

    @Override
    public List<HistorialInteraccion> searchAll() {
        return historialInteraccionRepository.findAll();
    }

    @Override
    public List<HistorialInteraccion> searchByUsuario(Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        return historialInteraccionRepository.findByUsuario(usuario);
    }

    @Override
    public List<HistorialInteraccion> obtenerHistorialInteraccionsDelUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuario = authentication.getName();

        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return historialInteraccionRepository.findByUsuario(usuario);
    }
}
