package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.utils.ReporteVentasDTO;
import com.grupo8.petshop.service.imp.ReporteExcelService;
import com.grupo8.petshop.service.imp.ReporteVentasService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteVentasController {

    private final ReporteVentasService reporteVentasService;
    private final ReporteExcelService reporteExcelService;

    @GetMapping("/ventas")
    public ResponseEntity<ReporteVentasDTO> obtenerReporteVentas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin
    ) {
        ReporteVentasDTO reporte = reporteVentasService.generarReporte(inicio, fin);
        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/ventas/excel")
    public void descargarExcel(
            HttpServletResponse response,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin
    ) throws IOException {
        ReporteVentasDTO reporte = reporteVentasService.generarReporte(inicio, fin);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String nombreArchivo = "reporte_ventas.xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + nombreArchivo);

        reporteExcelService.exportarReporteVentasAExcel(reporte, response.getOutputStream());
    }
}

