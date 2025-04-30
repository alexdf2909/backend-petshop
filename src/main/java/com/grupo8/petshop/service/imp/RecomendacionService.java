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

        return productoRepository.findRecomendadosPorEspecieYEtiquetas(especieNombre, etiquetas);
    }

    public List<String> generarEtiquetasDesdeMascota(Mascota mascota) {
        List<String> etiquetas = new ArrayList<>();

        String especieNombre = mascota.getEspecie().getNombre().toLowerCase();
        etiquetas.add(especieNombre); // ejemplo: "perro", "gato", etc.

        // Edad
        int edad = Period.between(mascota.getFechaNacimiento(), LocalDate.now()).getYears();
        Integer edadCachorro = mascota.getEspecie().getEdadCachorro();
        Integer edadAdulto = mascota.getEspecie().getEdadAdulto();

        if (edadCachorro != null && edad < edadCachorro) {
            etiquetas.add("cachorro");
        } else if (edadAdulto != null && edad < edadAdulto) {
            etiquetas.add("adulto");
        } else {
            etiquetas.add("senior");
        }

        // Peso
        float peso = mascota.getPeso();
        Float pesoPequeno = mascota.getEspecie().getPesoPequeno();
        Float pesoMediano = mascota.getEspecie().getPesoMediano();

        if (pesoPequeno != null && peso < pesoPequeno) {
            etiquetas.add("pequeño");
        } else if (pesoMediano != null && peso < pesoMediano) {
            etiquetas.add("mediano");
        } else {
            etiquetas.add("grande");
        }

        // Sexo
        if (mascota.getSexo().equalsIgnoreCase("hembra")) {
            etiquetas.add("hembra");
        } else {
            etiquetas.add("macho");
        }


        System.out.println(etiquetas);

        return etiquetas;
    }
}
