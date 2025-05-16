package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.MascotaDTO;
import com.grupo8.petshop.entity.*;
import com.grupo8.petshop.repository.*;
import com.grupo8.petshop.service.IMascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MascotaService implements IMascotaService {
    private final IMascotaRepository mascotaRepository;

    @Autowired
    private IEspecieRepository especieRepository;
    @Autowired
    private IRazaRepository razaRepository;
    @Autowired
    private IUsuarioRepository usuarioRepository;


    public MascotaService(IMascotaRepository mascotaRepository) {
        this.mascotaRepository = mascotaRepository;
    }

    @Override
    public Mascota createMascota(MascotaDTO mascotaDTO) {

        Usuario usuario = usuarioRepository.findById(mascotaDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Especie especie = especieRepository.findById(mascotaDTO.getEspecieId())
                .orElseThrow(() -> new RuntimeException("Especie no encontrada"));
        Raza raza = razaRepository.findById(mascotaDTO.getRazaId())
                .orElseThrow(() -> new RuntimeException("Raza no encontrada"));
        Mascota mascota = new Mascota();
        mascota.setImagenUrl(mascotaDTO.getImagenUrl());
        mascota.setNombre(mascotaDTO.getNombre());
        mascota.setPeso(mascotaDTO.getPeso());
        mascota.setEspecie(especie);
        mascota.setRaza(raza);
        mascota.setSexo(mascotaDTO.getSexo());
        mascota.setFechaNacimiento(mascotaDTO.getFechaNacimiento());
        mascota.setUsuario(usuario);

        return mascotaRepository.save(mascota);

    }

    @Override
    public Optional<Mascota> searchForId(Long id) {
        return mascotaRepository.findById(id);
    }

    @Override
    public List<Mascota> searchAll() {
        return mascotaRepository.findAll();
    }

    @Override
    public void updateMascota(Long id, MascotaDTO mascotaDTO) {
        Optional<Mascota> mascotaOpt = mascotaRepository.findById(id);
        if (mascotaOpt.isEmpty()) {
            throw new RuntimeException("Mascota no encontrada");
        }

        Mascota mascota = mascotaOpt.get();

        if (mascotaDTO.getEspecieId() != null) {
            Especie especie = especieRepository.findById(mascotaDTO.getEspecieId())
                    .orElseThrow(() -> new RuntimeException("Especie no encontrada"));
            mascota.setEspecie(especie);
        }
        if (mascotaDTO.getRazaId() != null) {
            Raza raza = razaRepository.findById(mascotaDTO.getRazaId())
                    .orElseThrow(() -> new RuntimeException("Raza no encontrada"));
            mascota.setRaza(raza);
        }
        if (mascotaDTO.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(mascotaDTO.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            mascota.setUsuario(usuario);
        }
        if (mascotaDTO.getNombre() != null && !mascotaDTO.getNombre().isEmpty()) {
            mascota.setNombre(mascotaDTO.getNombre());
        }

        mascota.setPeso(mascotaDTO.getPeso());

        if (mascotaDTO.getSexo() != null && !mascotaDTO.getSexo().isEmpty()) {
            mascota.setSexo(mascotaDTO.getSexo());
        }
        if (mascotaDTO.getFechaNacimiento() != null) {
            mascota.setFechaNacimiento(mascotaDTO.getFechaNacimiento());
        }

        if (mascotaDTO.getImagenUrl() != null && !mascotaDTO.getImagenUrl().isEmpty()) {
            mascota.setImagenUrl(mascotaDTO.getImagenUrl());
        }



        mascotaRepository.save(mascota);
    }

    @Override
    public void deleteMascota(Long id) {
        // Buscar la categoría a eliminar
        Optional<Mascota> mascotaOpt = mascotaRepository.findById(id);
        if (mascotaOpt.isEmpty()) {
            throw new RuntimeException("Mascota no encontrada");
        }
        Mascota mascota = mascotaOpt.get();

        // Eliminar la categoría
        mascotaRepository.delete(mascota);
    }

    private MascotaDTO convertToDTO(Mascota mascota) {
        return new MascotaDTO(
                mascota.getMascotaId(),
                mascota.getNombre(),
                mascota.getFechaNacimiento(),
                mascota.getEspecie().getEspecieId(),
                mascota.getRaza().getRazaId(),
                mascota.getSexo(),
                mascota.getImagenUrl(),
                mascota.getPeso(),
                mascota.getUsuario().getUsuarioId()
        );
    }

    @Override
    public List<Mascota> obtenerMascotasDelUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuario = authentication.getName(); // el 'correo' fue puesto como subject en el token

        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return mascotaRepository.findByUsuario(usuario);
    }
}