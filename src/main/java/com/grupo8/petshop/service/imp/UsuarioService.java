package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.ProductoDTO;
import com.grupo8.petshop.dto.UsuarioDTO;
import com.grupo8.petshop.entity.Producto;
import com.grupo8.petshop.entity.Usuario;
import com.grupo8.petshop.repository.IProductoRepository;
import com.grupo8.petshop.repository.IUsuarioRepository;
import com.grupo8.petshop.service.IAuthService;
import com.grupo8.petshop.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements IUsuarioService {
    private final IUsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IProductoRepository productoRepository;
    @Autowired
    private IAuthService authService;

    public UsuarioService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario createUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByCorreo(usuarioDTO.getCorreo()))
            throw new RuntimeException("Correo ya registrado");

        String codigo = String.format("%06d", new Random().nextInt(999999));

        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setContrasena(passwordEncoder.encode(usuarioDTO.getContrasena()));
        usuario.setRol(usuarioDTO.getRol());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setCodigoVerificacion(codigo);
        usuario.setVerificado(false);
        usuario.setDeleted(false);

        if (usuarioDTO.getFavoritos() != null) {
            usuarioDTO.getFavoritos().forEach(productoDTO -> {
                Optional<Producto> productoOpt = productoRepository.findById(productoDTO.getProductoId());
                productoOpt.ifPresent(usuario.getFavoritos()::add);
            });
        }

        authService.enviarCorreo(usuario.getCorreo(), codigo);

        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> searchForId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public List<Usuario> searchAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public void updateUsuario(Long id, UsuarioDTO usuarioDTO) {
        System.out.println("servicio");
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            System.out.println("No se encontró usuario con ID: " + id);
            throw new RuntimeException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        if (usuarioDTO.getNombre() != null && !usuarioDTO.getNombre().isEmpty()) {
            usuario.setNombre(usuarioDTO.getNombre());
            System.out.println("nombre");
        }

        if (usuarioDTO.getCorreo() != null && !usuarioDTO.getCorreo().isEmpty()) {
            usuario.setCorreo(usuarioDTO.getCorreo());
        }

        if (usuarioDTO.getRol() != null) {
            usuario.setRol(usuarioDTO.getRol());
            System.out.println("rol");
        }

        usuario.setDeleted(usuarioDTO.isDeleted());
        System.out.println(usuarioDTO.isDeleted());
        System.out.println(usuario.isDeleted());

        System.out.println(usuario);

        usuarioRepository.save(usuario);
    }

    @Override
    public void deleteUsuario(Long id) {
        // Buscar la usuario a eliminar
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }
        Usuario usuario = usuarioOpt.get();

        usuario.setDeleted(true);

    }

    // Agregar favorito
    @Override
    public Optional<Usuario> addFavorite(Long usuarioId, Long productoId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        Optional<Producto> productoOpt = productoRepository.findById(productoId);

        if (usuarioOpt.isPresent() && productoOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            Producto producto = productoOpt.get();

            if (!usuario.getFavoritos().contains(producto)) {
                usuario.getFavoritos().add(producto);
                usuarioRepository.save(usuario);
                return Optional.of(usuario);
            }
        }
        return Optional.empty();
    }

    // Eliminar favorito
    @Override
    public Optional<Usuario> removeFavorite(Long usuarioId, Long productoId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        Optional<Producto> productoOpt = productoRepository.findById(productoId);

        if (usuarioOpt.isPresent() && productoOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            Producto producto = productoOpt.get();

            if (usuario.getFavoritos().contains(producto)) {
                usuario.getFavoritos().remove(producto);
                usuarioRepository.save(usuario);
                return Optional.of(usuario);
            }
        }
        return Optional.empty();
    }

    private UsuarioDTO convertToDTO(Usuario usuario) {
        Set<ProductoDTO> favoritoDTO = usuario.getFavoritos().stream()
                .map(producto -> new ProductoDTO(
                        producto.getProductoId(),
                        producto.getNombre(),
                        producto.getDescripcion(),
                        producto.getEspecie().getEspecieId(),
                        producto.getMarca().getMarcaId(),
                        producto.getCategoria().getCategoriaId(),
                        producto.getEtiquetas().stream()
                                .map(etiqueta -> etiqueta.getEtiquetaId())
                                .collect(Collectors.toSet()),
                        producto.isDeleted()
                ))
                .collect(Collectors.toSet());
        return new UsuarioDTO(
                usuario.getUsuarioId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getContrasena(),
                usuario.getRol(),
                usuario.getCodigoVerificacion(),
                usuario.getVerificado(),
                favoritoDTO,
                usuario.isDeleted()
        );
    }
}
