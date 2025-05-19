package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.entidades.PedidoDTO;
import com.grupo8.petshop.entity.Mascota;
import com.grupo8.petshop.entity.Pedido;
import com.grupo8.petshop.entity.Usuario;
import com.grupo8.petshop.repository.IPedidoRepository;
import com.grupo8.petshop.repository.IUsuarioRepository;
import com.grupo8.petshop.service.IPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class PedidoService implements IPedidoService {
    private final IPedidoRepository pedidoRepository;

    @Autowired
    private IUsuarioRepository usuarioRepository;


    public PedidoService(IPedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public Optional<Pedido> searchForId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public List<Pedido> searchAll() {
        return pedidoRepository.findAll();
    }



    @Override
    public void updatePedido(Long id, PedidoDTO pedidoDTO) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isEmpty()) {
            throw new RuntimeException("Pedido no encontrado");
        }

        Pedido pedido = pedidoOpt.get();

        if (pedidoDTO.getEstadoPedido() != null) {
            pedido.setEstadoPedido(pedidoDTO.getEstadoPedido());
        }

        if (pedidoDTO.getProgresoEntrega() != null) {
            pedido.setProgresoEntrega(pedidoDTO.getProgresoEntrega());
        }
        if (pedidoDTO.getTiempoEstimadoEntrega() != null && !pedidoDTO.getTiempoEstimadoEntrega().isEmpty()) {
            pedido.setTiempoEstimadoEntrega(pedidoDTO.getTiempoEstimadoEntrega());
        }

        pedidoRepository.save(pedido);
    }


    @Override
    public List<Pedido> searchByUsuario(Long usuarioId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Usuario usuario = usuarioOpt.get();
        return pedidoRepository.findByUsuario(usuario);
    }

    @Override
    public List<Pedido> obtenerPedidosDelUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuario = authentication.getName(); // el 'correo' fue puesto como subject en el token

        Usuario usuario = usuarioRepository.findByCorreo(correoUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return pedidoRepository.findByUsuario(usuario);
    }
}