package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.LoteDTO;
import com.grupo8.petshop.entity.Compra;
import com.grupo8.petshop.entity.Lote;
import com.grupo8.petshop.entity.Variante;
import com.grupo8.petshop.repository.ICompraRepository;
import com.grupo8.petshop.repository.ILoteRepository;
import com.grupo8.petshop.repository.IVarianteRepository;
import com.grupo8.petshop.service.ILoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LoteService implements ILoteService {
    private final ILoteRepository loteRepository;

    @Autowired
    private ICompraRepository compraRepository;

    @Autowired
    private IVarianteRepository varianteRepository;

    public LoteService(ILoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    @Override
    public LoteDTO createLote(LoteDTO loteDTO) {
        Compra compra = compraRepository.findById(loteDTO.getCompraId())
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

        Variante variante = varianteRepository.findById(loteDTO.getVarianteId())
                .orElseThrow(() -> new RuntimeException("Variante no encontrada"));

        Lote lote = new Lote();
        lote.setDeleted(loteDTO.isDeleted());
        lote.setCompra(compra);
        lote.setVariante(variante);
        lote.setFechaFabricacion(loteDTO.getFechaFabricacion());
        lote.setStock(loteDTO.getStock());
        lote.setFechaVencimiento(loteDTO.getFechaVencimiento());

        Lote savedLote = loteRepository.save(lote);

        return convertToDTO(savedLote);
    }

    @Override
    public Optional<LoteDTO> searchForId(Long id) {
        return loteRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public List<LoteDTO> searchAll() {
        return loteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateLote(Long id, LoteDTO loteDTO) {
        Optional<Lote> loteOpt = loteRepository.findById(id);
        if (loteOpt.isEmpty()) {
            throw new RuntimeException("Lote no encontrado");
        }

        Lote lote = loteOpt.get();

        if (loteDTO.getStock() < 0) {
            lote.setStock(loteDTO.getStock());
        }
        if (loteDTO.getVarianteId() != null) {
            Variante variante = varianteRepository.findById(loteDTO.getVarianteId())
                    .orElseThrow(() -> new RuntimeException("Variante no encontrada"));
            lote.setVariante(variante);
        }
        if (loteDTO.getCompraId() != null) {
            Compra compra = compraRepository.findById(loteDTO.getCompraId())
                    .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

            lote.setCompra(compra);
        }
        if (loteDTO.getFechaVencimiento() != null) {
            lote.setFechaVencimiento(loteDTO.getFechaVencimiento());
        }
        if (loteDTO.getFechaFabricacion() != null) {
            lote.setFechaFabricacion(loteDTO.getFechaFabricacion());
        }

        loteRepository.save(lote);
    }

    @Override
    public void deleteLote(Long id) {
        // Buscar la categoría a eliminar
        Optional<Lote> loteOpt = loteRepository.findById(id);
        if (loteOpt.isEmpty()) {
            throw new RuntimeException("Lote no encontrado");
        }
        Lote lote = loteOpt.get();
        lote.setDeleted(true);
    }

    private LoteDTO convertToDTO(Lote lote) {
        return new LoteDTO(
                lote.getLoteId(),
                lote.getCompra().getCompraId(),
                lote.getVariante().getVarianteId(),
                lote.getFechaVencimiento(),
                lote.getFechaFabricacion(),
                lote.getStock(),
                lote.isDeleted()
        );
    }
}
