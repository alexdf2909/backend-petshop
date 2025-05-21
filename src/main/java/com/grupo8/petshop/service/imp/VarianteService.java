package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.utils.UsuarioFrecuenteDTO;
import com.grupo8.petshop.dto.utils.VarianteConDemandaDTO;
import com.grupo8.petshop.dto.utils.VarianteConStockDTO;
import com.grupo8.petshop.dto.entidades.VarianteDTO;
import com.grupo8.petshop.entity.*;
import com.grupo8.petshop.repository.*;
import com.grupo8.petshop.service.IVarianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VarianteService implements IVarianteService {
    private final IVarianteRepository varianteRepository;
    @Autowired
    private IProductoRepository productoRepository;
    @Autowired
    private ILoteRepository loteRepository;
    @Autowired
    private IColorRepository colorRepository;
    @Autowired
    private ITallaRepository tallaRepository;
    @Autowired
    private IPesoRepository pesoRepository;
    @Autowired
    private IDetallePedidoRepository detallePedidoRepository;

    public VarianteService(IVarianteRepository varianteRepository) {
        this.varianteRepository = varianteRepository;
    }

    @Override
    public Variante createVariante(VarianteDTO varianteDTO) {

        Variante variante = new Variante();
        variante.setPeso(null);
        variante.setTalla(null);
        variante.setColor(null);
        if (varianteDTO.getPesoId() != null) {
            Peso peso = pesoRepository.findById(varianteDTO.getPesoId())
                    .orElseThrow(() -> new RuntimeException("Peso no encontrada"));
            variante.setPeso(peso);
        }
        if (varianteDTO.getTallaId() != null) {
            Talla talla = tallaRepository.findById(varianteDTO.getTallaId())
                    .orElseThrow(() -> new RuntimeException("Talla no encontrada"));
            variante.setTalla(talla);
        }
        if (varianteDTO.getColorId() != null) {
            Color color = colorRepository.findById(varianteDTO.getColorId())
                    .orElseThrow(() -> new RuntimeException("Color no encontrada"));
            variante.setColor(color);
        }
        Producto producto = productoRepository.findById(varianteDTO.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrada"));


        variante.setDeleted(varianteDTO.isDeleted());
        variante.setProducto(producto);
        variante.setPrecioOferta(varianteDTO.getPrecioOferta());
        variante.setPrecioOriginal(varianteDTO.getPrecioOriginal());

        variante.setStockMinimo(varianteDTO.getStockMinimo());
        // Agregar imágenes
        if (varianteDTO.getImagenes() != null) {
            varianteDTO.getImagenes().forEach(imageUrl -> {
                Imagen image = new Imagen();
                image.setImagenUrl(imageUrl);
                variante.getImagenes().add(image);
            });
        }

        return varianteRepository.save(variante);
    }

    @Override
    public Optional<Variante> searchForId(Long id) {
        return varianteRepository.findById(id);
    }

    @Override
    public List<Variante> searchAll() {
        return varianteRepository.findAll();
    }

    @Override
    public List<Variante> searchByProducto(Long productoId) {
        Optional<Producto> productoOpt = productoRepository.findById(productoId);
        if (productoOpt.isEmpty()) {
            throw new RuntimeException("Producto no encontrado");
        }

        Producto producto = productoOpt.get();
        return varianteRepository.findByProducto(producto);
    }

    @Override
    public void updateVariante(Long id, VarianteDTO varianteDTO) {
        Optional<Variante> varianteOpt = varianteRepository.findById(id);
        if (varianteOpt.isEmpty()) {
            throw new RuntimeException("Variante no encontrada");
        }

        Variante variante = varianteOpt.get();

        if (varianteDTO.getProductoId() != null) {
            Producto producto = productoRepository.findById(varianteDTO.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrada"));
            variante.setProducto(producto);
        }
        if (varianteDTO.getPesoId() != null) {
            Peso peso = pesoRepository.findById(varianteDTO.getPesoId())
                    .orElseThrow(() -> new RuntimeException("Peso no encontrada"));
            variante.setPeso(peso);
        }else{
            variante.setPeso(null);
        }
        if (varianteDTO.getTallaId() != null) {
            Talla talla = tallaRepository.findById(varianteDTO.getTallaId())
                    .orElseThrow(() -> new RuntimeException("Talla no encontrada"));
            variante.setTalla(talla);
        }else{
            variante.setTalla(null);
        }
        if (varianteDTO.getColorId() != null) {
            Color color = colorRepository.findById(varianteDTO.getColorId())
                    .orElseThrow(() -> new RuntimeException("Color no encontrada"));
            variante.setColor(color);
        }else{
            variante.setColor(null);
        }
        if (varianteDTO.getPrecioOferta() > 0.0) {
            variante.setPrecioOferta(varianteDTO.getPrecioOferta());
        }
        if (varianteDTO.getPrecioOriginal() > 0.0) {
            variante.setPrecioOriginal(varianteDTO.getPrecioOriginal());
        }
        if(varianteDTO.getStockMinimo() > 0){
            variante.setStockMinimo(varianteDTO.getStockMinimo());
        }
        if(varianteDTO.isDeleted() && !variante.isDeleted()){
            deleteVariante(varianteDTO.getVarianteId());
        }else{
            variante.setDeleted(varianteDTO.isDeleted());
        }

        if (varianteDTO.getImagenes() != null) {
            // Mantener las imágenes previas que no han cambiado
            List<String> existingImageUrls = variante.getImagenes().stream()
                    .map(Imagen::getImagenUrl)
                    .collect(Collectors.toList());

            variante.getImagenes().clear();  // Limpiar las imágenes previas para agregar las nuevas

            varianteDTO.getImagenes().forEach(imageUrl -> {
                // Verificar si la imagen ya está registrada
                if (!existingImageUrls.contains(imageUrl)) {
                    Imagen image = new Imagen();
                    image.setImagenUrl(imageUrl);
                    variante.getImagenes().add(image);
                }
            });
        }

        varianteRepository.save(variante);
    }

    @Override
    public void deleteVariante(Long id) {
        // Buscar la categoría a eliminar
        Optional<Variante> varianteOpt = varianteRepository.findById(id);
        if (varianteOpt.isEmpty()) {
            throw new RuntimeException("Variante no encontrada");
        }
        Variante variante = varianteOpt.get();
        // Buscar todas las prendas asociadas con esta categoría
        List<Variante> varianteWithProducto = varianteRepository.findByProducto(variante.getProducto());
        // Varianter todas las prendas asociadas como eliminadas lógicamente
        variante.setDeleted(true);
        boolean tieneOtraVarianteActiva = false;
        for (Variante v : varianteWithProducto) {
            if (!v.isDeleted()) {
                tieneOtraVarianteActiva = true;
                break;
            }
        }
        if(!tieneOtraVarianteActiva) {
            Producto producto = variante.getProducto();
            producto.setDeleted(true);
            productoRepository.save(producto);
        }
    }

    private VarianteDTO convertToDTO(Variante variante) {
        return new VarianteDTO(
                variante.getVarianteId(),
                variante.getProducto().getProductoId(),
                variante.getColor().getColorId(),
                variante.getTalla().getTallaId(),
                variante.getPeso().getPesoId(),
                variante.getPrecioOriginal(),
                variante.getPrecioOferta(),
                variante.getStockMinimo(),
                variante.getImagenes().stream()
                        .map(Imagen::getImagenUrl)
                        .collect(Collectors.toSet()),
                variante.isDeleted()
        );
    }


    @Override
    public List<VarianteConStockDTO> obtenerVariantesConBajoStock() {
        List<Variante> variantes = varianteRepository.findAll();
        LocalDate hoy = LocalDate.now();

        return variantes.stream().map(v -> {
                    List<Lote> lotes = loteRepository.findByVarianteAndIsDeletedFalse(v);

                    int stockUtil = lotes.stream()
                            .filter(l -> l.getStock() > 0)
                            .filter(l -> l.getFechaVencimiento() == null || l.getFechaVencimiento().isAfter(hoy))
                            .mapToInt(Lote::getStock)
                            .sum();

                    int stockVencido = lotes.stream()
                            .filter(l -> l.getFechaVencimiento() != null && l.getFechaVencimiento().isBefore(hoy))
                            .mapToInt(Lote::getStock)
                            .sum();

                    String nombre = v.getProducto().getNombre()
                            + (v.getTalla() != null ? " " + v.getTalla().getValor() : "")
                            + (v.getPeso() != null ? " " + v.getPeso().getValor() : "")
                            + (v.getColor() != null ? " " + v.getColor().getValor() : "");

                    return new VarianteConStockDTO(
                            v.getVarianteId(),
                            nombre,
                            stockUtil,
                            stockVencido,
                            v.getStockMinimo(),
                            lotes.isEmpty()
                    );
                }).filter(dto -> dto.getStockUtil() < dto.getStockMinimo())
                .collect(Collectors.toList());
    }

    @Override
    public List<VarianteConDemandaDTO> obtenerVariantesConBajoStockYDemanda() {
        List<Variante> variantes = varianteRepository.findAll();
        LocalDate hoy = LocalDate.now();
        LocalDateTime hace30Dias = hoy.minusDays(30).atStartOfDay();
        final int DIAS_UMBRAL = 7; // Próxima recompras dentro de 7 días son consideradas urgentes

        return variantes.stream().map(v -> {
            List<Lote> lotes = loteRepository.findByVarianteAndIsDeletedFalse(v);

            int stockUtil = lotes.stream()
                    .filter(l -> l.getStock() > 0)
                    .filter(l -> l.getFechaVencimiento() == null || l.getFechaVencimiento().isAfter(hoy))
                    .mapToInt(Lote::getStock)
                    .sum();

            int stockVencido = lotes.stream()
                    .filter(l -> l.getFechaVencimiento() != null && l.getFechaVencimiento().isBefore(hoy))
                    .mapToInt(Lote::getStock)
                    .sum();

            String nombre = v.getProducto().getNombre()
                    + (v.getTalla() != null ? " " + v.getTalla().getValor() : "")
                    + (v.getPeso() != null ? " " + v.getPeso().getValor() : "")
                    + (v.getColor() != null ? " " + v.getColor().getValor() : "");

            List<DetallePedido> detalles = detallePedidoRepository.findByVarianteAndPedido_FechaRegistroAfter(v, hace30Dias);

            Map<Usuario, List<DetallePedido>> porUsuario = detalles.stream()
                    .collect(Collectors.groupingBy(dp -> dp.getPedido().getUsuario()));

            List<UsuarioFrecuenteDTO> usuariosFrecuentes = porUsuario.entrySet().stream()
                    .map(entry -> {
                        Usuario usuario = entry.getKey();
                        List<DetallePedido> pedidos = entry.getValue();

                        int cantidadTotal = pedidos.stream().mapToInt(DetallePedido::getCantidad).sum();
                        int cantidadPromedio = cantidadTotal / pedidos.size();

                        List<LocalDate> fechas = pedidos.stream()
                                .map(dp -> dp.getPedido().getFechaRegistro().toLocalDate())
                                .sorted()
                                .toList();

                        LocalDate fechaUltima = fechas.get(fechas.size() - 1);

                        LocalDate fechaProximaRecompra = null;
                        if (fechas.size() > 1) {
                            List<Long> diferencias = new ArrayList<>();
                            for (int i = 1; i < fechas.size(); i++) {
                                diferencias.add(ChronoUnit.DAYS.between(fechas.get(i - 1), fechas.get(i)));
                            }
                            OptionalDouble promedioDias = diferencias.stream().mapToLong(Long::longValue).average();
                            if (promedioDias.isPresent()) {
                                long dias = (long) promedioDias.getAsDouble();
                                if (dias < 1) dias = 1;
                                fechaProximaRecompra = fechaUltima.plusDays(dias);
                            }
                        }

                        return new UsuarioFrecuenteDTO(
                                usuario.getUsuarioId(),
                                usuario.getNombre(),
                                cantidadPromedio,
                                fechaUltima,
                                fechaProximaRecompra
                        );
                    })
                    .filter(uf -> uf.getFechaProximaRecompra() != null)
                    .toList();

            // Nueva lógica: ¿hay algún usuario con próxima recompra urgente y cantidad promedio > stock actual?
            boolean demandaUrgenteNoCubierta = usuariosFrecuentes.stream().anyMatch(uf ->
                    ChronoUnit.DAYS.between(hoy, uf.getFechaProximaRecompra()) <= DIAS_UMBRAL &&
                            uf.getCantidadPromedio() > stockUtil
            );

            // Fecha mínima de próxima recompra
            LocalDate fechaProxima = usuariosFrecuentes.stream()
                    .map(UsuarioFrecuenteDTO::getFechaProximaRecompra)
                    .min(LocalDate::compareTo)
                    .orElse(null);

            // Cantidad total estimada (suma de promedios)
            int cantidadRecurrenteSugerida = usuariosFrecuentes.stream()
                    .mapToInt(UsuarioFrecuenteDTO::getCantidadPromedio)
                    .sum();

            if (stockUtil < v.getStockMinimo() || demandaUrgenteNoCubierta) {
                return new VarianteConDemandaDTO(
                        v.getVarianteId(),
                        nombre,
                        stockUtil,
                        stockVencido,
                        v.getStockMinimo(),
                        lotes.isEmpty(),
                        cantidadRecurrenteSugerida,
                        fechaProxima,
                        usuariosFrecuentes
                );
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }



}
