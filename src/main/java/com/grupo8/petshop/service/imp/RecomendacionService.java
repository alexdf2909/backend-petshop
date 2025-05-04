package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.entity.Mascota;
import com.grupo8.petshop.entity.Producto;
import com.grupo8.petshop.repository.IMascotaRepository;
import com.grupo8.petshop.repository.IProductoRepository;
import com.grupo8.petshop.service.IRecomendacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecomendacionService implements IRecomendacionService {

    private final IMascotaRepository mascotaRepository;
    private final IProductoRepository productoRepository;

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

}
