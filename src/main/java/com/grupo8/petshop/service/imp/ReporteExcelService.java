package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.utils.ConversionProductoDTO;
import com.grupo8.petshop.dto.utils.ProductoMasVendidoDTO;
import com.grupo8.petshop.dto.utils.ReporteVentasDTO;
import com.grupo8.petshop.dto.utils.VentasProductoDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Service
public class ReporteExcelService {

    public void exportarReporteVentasAExcel(ReporteVentasDTO reporte, OutputStream outputStream) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet resumen = workbook.createSheet("Resumen");

            int rowNum = 0;
            Row row = resumen.createRow(rowNum++);
            row.createCell(0).setCellValue("Ventas del día");
            row.createCell(1).setCellValue(reporte.getVentasHoy());

            row = resumen.createRow(rowNum++);
            row.createCell(0).setCellValue("Monto total ventas");
            row.createCell(1).setCellValue(reporte.getMontoTotal());

            rowNum++;

            // Pedidos por estado
            Row headerEstado = resumen.createRow(rowNum++);
            headerEstado.createCell(0).setCellValue("Estado");
            headerEstado.createCell(1).setCellValue("Cantidad");

            for (Map.Entry<String, Long> entry : reporte.getPedidosPorEstado().entrySet()) {
                Row estadoRow = resumen.createRow(rowNum++);
                estadoRow.createCell(0).setCellValue(entry.getKey());
                estadoRow.createCell(1).setCellValue(entry.getValue());
            }

            // Productos más vendidos
            Sheet productosTop = workbook.createSheet("Productos Top");
            Row headerTop = productosTop.createRow(0);
            headerTop.createCell(0).setCellValue("Producto");
            headerTop.createCell(1).setCellValue("Categoría");
            headerTop.createCell(2).setCellValue("Cantidad Vendida");

            int rowTop = 1;
            for (ProductoMasVendidoDTO prod : reporte.getProductosTop()) {
                Row r = productosTop.createRow(rowTop++);
                r.createCell(0).setCellValue(prod.getNombreProducto());
                r.createCell(1).setCellValue(prod.getCategoria());
                r.createCell(2).setCellValue(prod.getCantidadVendida());
            }

            // Conversiones
            Sheet conversiones = workbook.createSheet("Conversiones");
            Row headerConv = conversiones.createRow(0);
            headerConv.createCell(0).setCellValue("Producto");
            headerConv.createCell(1).setCellValue("Vistas");
            headerConv.createCell(2).setCellValue("Agregado al carrito");
            headerConv.createCell(3).setCellValue("Compras");

            int rowConv = 1;
            for (ConversionProductoDTO conv : reporte.getConversionesPorProducto()) {
                Row r = conversiones.createRow(rowConv++);
                r.createCell(0).setCellValue(conv.getNombreProducto());
                r.createCell(1).setCellValue(conv.getVistas());
                r.createCell(2).setCellValue(conv.getAgregadosAlCarrito());
                r.createCell(3).setCellValue(conv.getCompras());
            }

            // Ventas por producto
            Sheet ventasProducto = workbook.createSheet("Ventas por Producto");
            Row headerVentas = ventasProducto.createRow(0);
            headerVentas.createCell(0).setCellValue("Producto");
            headerVentas.createCell(1).setCellValue("Categoría");
            headerVentas.createCell(2).setCellValue("Cantidad");
            headerVentas.createCell(3).setCellValue("Total");

            int rowVentas = 1;
            for (VentasProductoDTO v : reporte.getVentasPorProducto()) {
                Row r = ventasProducto.createRow(rowVentas++);
                r.createCell(0).setCellValue(v.getNombreProducto());
                r.createCell(1).setCellValue(v.getCategoria());
                r.createCell(2).setCellValue(v.getCantidadVendida());
                r.createCell(3).setCellValue(v.getMontoTotal());
            }

            workbook.write(outputStream);
        }
    }
}

