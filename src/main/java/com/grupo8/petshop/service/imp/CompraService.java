package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.entidades.CompraDTO;
import com.grupo8.petshop.entity.Compra;
import com.grupo8.petshop.entity.Lote;
import com.grupo8.petshop.entity.Usuario;
import com.grupo8.petshop.repository.ICompraRepository;
import com.grupo8.petshop.repository.ILoteRepository;
import com.grupo8.petshop.repository.IUsuarioRepository;
import com.grupo8.petshop.service.ICompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompraService implements ICompraService {
    private final ICompraRepository compraRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private ILoteRepository loteRepository;

    public CompraService(ICompraRepository compraRepository) {
        this.compraRepository = compraRepository;
    }

    @Override
    public Compra createCompra(CompraDTO compraDTO) {
        Usuario usuario = usuarioRepository.findById(compraDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Compra compra = new Compra();
        compra.setFechaCompra(compraDTO.getFechaCompra());
        compra.setUsuario(usuario);
        compra.setCodigoComprobante(compraDTO.getCodigoComprobante());
        compra.setFechaRegistro(compraDTO.getFechaRegistro());
        compra.setUrlImagenComprobante(compraDTO.getUrlImagenComprobante());
        compra.setDeleted(false);

        return compraRepository.save(compra);
    }

    @Override
    public Optional<Compra> searchForId(Long id) {
        return compraRepository.findById(id);
    }

    @Override
    public List<Compra> searchAll() {
        return compraRepository.findAll();
    }

    @Override
    public void updateCompra(Long id, CompraDTO compraDTO) {
        Optional<Compra> compraOpt = compraRepository.findById(id);
        if (compraOpt.isEmpty()) {
            throw new RuntimeException("Compra no encontrada");
        }

        Compra compra = compraOpt.get();

        if (compraDTO.getFechaCompra() != null) {
            compra.setFechaCompra(compraDTO.getFechaCompra());
        }
        if (compraDTO.getFechaRegistro() != null) {
            compra.setFechaRegistro(compraDTO.getFechaRegistro());
        }
        if (compraDTO.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(compraDTO.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            compra.setUsuario(usuario);
        }
        if (compraDTO.getCodigoComprobante() != null && !compraDTO.getCodigoComprobante().isEmpty()) {
            compra.setCodigoComprobante(compraDTO.getCodigoComprobante());
        }

        if (compraDTO.getUrlImagenComprobante() != null && !compraDTO.getUrlImagenComprobante().isEmpty()) {
            compra.setUrlImagenComprobante(compraDTO.getUrlImagenComprobante());
        }

        if(compraDTO.isDeleted() && !compra.isDeleted()){
            deleteCompra(compraDTO.getCompraId());
        }

        compraRepository.save(compra);
    }

    @Override
    public void deleteCompra(Long id) {
        // Buscar la compra a eliminar
        Optional<Compra> compraOpt = compraRepository.findById(id);
        if (compraOpt.isEmpty()) {
            throw new RuntimeException("Compra no encontrada");
        }
        Compra compra = compraOpt.get();
        // Buscar todos los lotes asociadas con esta compra
        List<Lote> lotesWithCompra = loteRepository.findByCompra(compra);
        // Marcar todas las lotes asociadas como eliminadas lógicamente
        for (Lote lote : lotesWithCompra) {
            lote.setDeleted(true);
        }
        // Guardar los cambios en las prendas
        loteRepository.saveAll(lotesWithCompra);
        // Eliminar la categoría
        compra.setDeleted(true);
    }

    private CompraDTO convertToDTO(Compra compra) {
        return new CompraDTO(
                compra.getCompraId(),
                compra.getCodigoComprobante(),
                compra.getUrlImagenComprobante(),
                compra.getFechaCompra(),
                compra.getFechaRegistro(),
                compra.getUsuario().getUsuarioId(),
                compra.isDeleted()
        );
    }
}
