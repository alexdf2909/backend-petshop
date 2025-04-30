package com.grupo8.petshop.service;

import com.grupo8.petshop.entity.Mascota;
import com.grupo8.petshop.entity.Producto;

import java.util.List;

public interface IRecomendacionService {


         List<Producto> recomendarProductosPorMascota(Long mascotaId);

         List<String> generarEtiquetasDesdeMascota(Mascota mascota);

}
