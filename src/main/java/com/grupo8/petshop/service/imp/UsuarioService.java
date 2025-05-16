package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.entidades.ProductoDTO;
import com.grupo8.petshop.dto.entidades.UsuarioDTO;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

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
        usuario.setImagenPerfil(usuarioDTO.getImagenPerfil());

        if (usuarioDTO.getFavoritos() != null) {
            usuarioDTO.getFavoritos().forEach(productoDTO -> {
                Optional<Producto> productoOpt = productoRepository.findById(productoDTO.getProductoId());
                productoOpt.ifPresent(usuario.getFavoritos()::add);
            });
        }

        authService.enviarCorreo(usuario, codigo);

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
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        if (usuarioDTO.getNombre() != null && !usuarioDTO.getNombre().isEmpty()) {
            usuario.setNombre(usuarioDTO.getNombre());
        }

        if (usuarioDTO.getCorreo() != null && !usuarioDTO.getCorreo().isEmpty()) {
            usuario.setCorreo(usuarioDTO.getCorreo());
        }

        if (usuarioDTO.getRol() != null) {
            usuario.setRol(usuarioDTO.getRol());
        }

        usuario.setDeleted(usuarioDTO.isDeleted());

        if (usuarioDTO.getImagenPerfil() != null && !usuarioDTO.getImagenPerfil().isEmpty()) {
            usuario.setImagenPerfil(usuarioDTO.getImagenPerfil());
        }

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
                usuario.isDeleted(),
                usuario.getImagenPerfil()
        );
    }
    // Obtener el correo del usuario autenticado
    private String getCorreoUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // El 'correo' fue puesto como subject en el JWT
    }

    // Obtener el usuario autenticado completo
    private Usuario getUsuarioAutenticado() {
        String correo = getCorreoUsuarioAutenticado();
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // Obtener el perfil del cliente autenticado
    @Override
    public Optional<Usuario> getPerfilCliente() {
        Usuario usuario = getUsuarioAutenticado();
        return Optional.of(usuario);
    }

    // Actualizar el perfil del cliente autenticado
    @Override
    public Usuario actualizarPerfilCliente(Usuario usuarioActualizado) {
        Usuario usuarioAutenticado = getUsuarioAutenticado();

        if (usuarioActualizado.getNombre() != null && !usuarioActualizado.getNombre().isEmpty()) {
            usuarioAutenticado.setNombre(usuarioActualizado.getNombre());
        }
        if (usuarioActualizado.getImagenPerfil() != null && !usuarioActualizado.getImagenPerfil().isEmpty()) {
            usuarioAutenticado.setImagenPerfil(usuarioActualizado.getImagenPerfil());
        }

        // Guardar el usuario actualizado
        return usuarioRepository.save(usuarioAutenticado);
    }
}
