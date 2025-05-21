package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.entity.*;
import com.grupo8.petshop.repository.*;
import com.grupo8.petshop.service.IRecomendacionService;
import com.grupo8.petshop.util.TipoInteraccion;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecomendacionService implements IRecomendacionService {

    private final IMascotaRepository mascotaRepository;
    private final IProductoRepository productoRepository;
    private final IHistorialInteraccionRepository historialRepo;
    private final IRecomendacionRepository recomendacionRepo;
    @Autowired
    private IUsuarioRepository usuarioRepository;
    @Autowired
    private ILoteRepository loteRepository;

    public List<Producto> recomendarProductosPorMascota(Long mascotaId) {
        Mascota mascota = mascotaRepository.findById(mascotaId)
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        List<String> etiquetas = generarEtiquetasDesdeMascota(mascota);
        String especieNombre = mascota.getEspecie().getNombre().toLowerCase();

        List<Producto> productos = productoRepository.findByEspecieNombreConEtiquetas(especieNombre);

        return productos.stream()
                .sorted((p1, p2) -> {
                    long count1 = contarCoincidenciasParciales(p1, etiquetas);
                    long count2 = contarCoincidenciasParciales(p2, etiquetas);
                    return Long.compare(count2, count1); // descendente
                })
                .toList();
    }

    private long contarCoincidenciasParciales(Producto producto, List<String> etiquetas) {
        return producto.getEtiquetas().stream()
                .flatMap(et -> etiquetas.stream()
                        .filter(f -> et.getNombre().toLowerCase().contains(f.toLowerCase())))
                .count();
    }

    public List<String> generarEtiquetasDesdeMascota(Mascota mascota) {
        List<String> etiquetas = new ArrayList<>();

        String especieNombre = mascota.getEspecie().getNombre().toLowerCase();
        etiquetas.add(especieNombre); // ejemplo: "perro", "gato", etc.

        // Edad
        LocalDate fechaNacimiento = mascota.getFechaNacimiento();
        Integer edadCachorro = mascota.getEspecie().getEdadCachorro();
        Integer edadAdulto = mascota.getEspecie().getEdadAdulto();

        if (fechaNacimiento != null && edadCachorro != null && edadAdulto != null) {
            int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
            if (edad < edadCachorro) {
                etiquetas.add("cachorro");
            } else if (edad < edadAdulto) {
                etiquetas.add("adulto");
            } else {
                etiquetas.add("senior");
            }
        }

        // Peso
        Float peso = mascota.getPeso(); // Cambia a Float si originalmente era float para permitir null
        Float pesoPequeno = mascota.getEspecie().getPesoPequeno();
        Float pesoMediano = mascota.getEspecie().getPesoMediano();

        if (peso != null && pesoPequeno != null && pesoMediano != null) {
            if (peso < pesoPequeno) {
                etiquetas.add("pequeño");
                etiquetas.add("pequeña");
            } else if (peso < pesoMediano) {
                etiquetas.add("mediano");
                etiquetas.add("mediana");
            } else {
                etiquetas.add("grande");
            }
        }

        // Sexo
        String sexo = mascota.getSexo();
        if (sexo != null) {
            if (sexo.equalsIgnoreCase("hembra")) {
                etiquetas.add("hembra");
            } else {
                etiquetas.add("macho");
            }
        }

        System.out.println(etiquetas);

        return etiquetas;
    }

    public void generarRecomendacionesParaUsuario() {
        // Encontrar usuario por token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuario = authentication.getName();

        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Map<Producto, Float> puntajes = new HashMap<>();

        // Favoritos
        for (Producto favorito : usuario.getFavoritos()) {
            puntajes.put(favorito, puntajes.getOrDefault(favorito, 0f) + 1.5f);
        }

        // Historial
        List<HistorialInteraccion> historial = historialRepo.findByUsuario(usuario);

        for (HistorialInteraccion interaccion : historial) {
            Producto producto = interaccion.getProducto();
            float peso = switch (interaccion.getTipoInteraccion()) {
                case VISTA -> 0.3f;
                case CARRITO -> 0.7f;
                case COMPRA -> 1.2f;
            };

            puntajes.put(producto, puntajes.getOrDefault(producto, 0f) + peso);
        }

        // ===============================
        // 🔍 CONTENIDO: Perfil del usuario
        // ===============================
        Map<Marca, Integer> marcasFavoritas = new HashMap<>();
        Map<Categoria, Integer> categoriasFavoritas = new HashMap<>();
        Map<Especie, Integer> especiesFavoritas = new HashMap<>();
        Map<Etiqueta, Integer> etiquetasFavoritas = new HashMap<>();

        for (Producto p : puntajes.keySet()) {
            if (p.getMarca() != null)
                marcasFavoritas.merge(p.getMarca(), 1, Integer::sum);
            if (p.getCategoria() != null)
                categoriasFavoritas.merge(p.getCategoria(), 1, Integer::sum);
            if (p.getEspecie() != null)
                especiesFavoritas.merge(p.getEspecie(), 1, Integer::sum);
            for (Etiqueta etiqueta : p.getEtiquetas()) {
                etiquetasFavoritas.merge(etiqueta, 1, Integer::sum);
            }
        }

        // ============================================
        // 🧠 Añadir productos similares no interactuados
        // ============================================
        Set<Producto> productosYaVistos = puntajes.keySet();
        List<Producto> todos = productoRepository.findAll(); // si tienes filtro por activos, agrégalo aquí

        for (Producto p : todos) {
            if (productosYaVistos.contains(p) || p.isDeleted()) continue;

            float puntajeExtra = 0f;

            if (marcasFavoritas.containsKey(p.getMarca())) puntajeExtra += 0.3f;
            if (categoriasFavoritas.containsKey(p.getCategoria())) puntajeExtra += 0.4f;
            if (especiesFavoritas.containsKey(p.getEspecie())) puntajeExtra += 0.2f;

            for (Etiqueta etiqueta : p.getEtiquetas()) {
                if (etiquetasFavoritas.containsKey(etiqueta)) {
                    puntajeExtra += 0.1f;
                }
            }

            if (puntajeExtra > 0.4f) {
                puntajes.put(p, puntajeExtra);
            }
        }

        // ============================
        // 🧹 Eliminar recomendaciones viejas
        // ============================
        List<Recomendacion> recomendacionesAnteriores = recomendacionRepo.findByUsuario(usuario);
        for (Recomendacion recomendacion : recomendacionesAnteriores) {
            Producto producto = recomendacion.getProducto();
            boolean tieneInteraccionesRecientes = historialRepo.existsByUsuarioAndProductoAndFechaAfter(
                    usuario, producto, LocalDateTime.now().minusDays(90));
            if (!tieneInteraccionesRecientes) {
                recomendacionRepo.delete(recomendacion);
            }
        }

        // ================================
        // 💾 Guardar nuevas recomendaciones
        // ================================
        for (Map.Entry<Producto, Float> entry : puntajes.entrySet()) {
            Producto producto = entry.getKey();
            float puntaje = entry.getValue();

            if (puntaje < 0.5f || producto.isDeleted()) {
                continue;
            }

            Recomendacion rec = recomendacionRepo.findByUsuarioAndProducto(usuario, producto)
                    .orElse(new Recomendacion());

            rec.setUsuario(usuario);
            rec.setProducto(producto);
            rec.setPuntaje(puntaje);

            // Frecuencia recomendada si hay historial de compras
            List<HistorialInteraccion> compras = historialRepo
                    .findByUsuarioAndProductoAndTipoInteraccionOrderByFechaAsc(
                            usuario, producto, TipoInteraccion.COMPRA);

            if (compras.size() >= 2) {
                List<Long> dias = new ArrayList<>();
                for (int i = 1; i < compras.size(); i++) {
                    long diff = ChronoUnit.DAYS.between(
                            compras.get(i - 1).getFecha(), compras.get(i).getFecha());
                    dias.add(diff);
                }
                int promedio = (int) dias.stream().mapToLong(Long::longValue).average().orElse(30);
                rec.setFrecuenciaRecomendadaDias(promedio);
            }

            recomendacionRepo.save(rec);
        }
    }

    public List<Producto> obtenerTopRecomendadosParaUsuario(int top) {
        // Encontrar usuario por token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuario = authentication.getName();

        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        List<Recomendacion> recomendaciones = recomendacionRepo.findByUsuario(usuario);
        recomendaciones.sort(Comparator.comparing(Recomendacion::getPuntaje).reversed());

        return recomendaciones.stream()
                .map(Recomendacion::getProducto)
                .filter(producto -> {
                    Integer stock = loteRepository.obtenerStockTotalPorProducto(producto);
                    return stock != null && stock > 0;
                })
                .limit(top)
                .collect(Collectors.toList());
    }


    public List<Producto> obtenerProductosMasPopulares(int top) {
        List<HistorialInteraccion> historial = historialRepo.findAll();

        Map<Producto, Float> puntajes = new HashMap<>();

        for (HistorialInteraccion interaccion : historial) {
            Producto producto = interaccion.getProducto();
            float peso = switch (interaccion.getTipoInteraccion()) {
                case VISTA -> 0.3f;
                case CARRITO -> 0.7f;
                case COMPRA -> 1.2f;
            };

            puntajes.put(producto, puntajes.getOrDefault(producto, 0f) + peso);
        }

        return puntajes.entrySet().stream()
                .filter(entry -> {
                    Producto producto = entry.getKey();
                    Integer stock = loteRepository.obtenerStockTotalPorProducto(producto);
                    return stock != null && stock > 0;
                })
                .sorted(Map.Entry.<Producto, Float>comparingByValue().reversed())
                .limit(top)
                .map(Map.Entry::getKey)
                .toList();
    }


}
