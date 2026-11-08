package com.felix.demo.controllers;

import com.felix.demo.dto.ProductoDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MvcController {

    // Leyendo propiedad externa desde application.properties
    @Value("${app.nombre.estudiante}")
    private String nombreEstudiante;

    @Value("${app.nombre.proyecto}")
    private String nombreProyecto;

    // Lista simulada de productos para la vista MVC
    private List<ProductoDTO> obtenerProductosMock() {
        List<ProductoDTO> lista = new ArrayList<>();
        lista.add(new ProductoDTO("Laptop HP", 12500.00, "Computo"));
        lista.add(new ProductoDTO("Teclado Mecánico", 850.50, "Accesorios"));
        lista.add(new ProductoDTO("Mouse Inalámbrico", 450.00, "Accesorios"));
        lista.add(new ProductoDTO("Monitor 24 pulgadas", 3200.00, "Computo"));
        return lista;
    }

    // a) Vista que muestra la lista de elementos usando el objeto Model y th:each
    // Opcionalmente usa @RequestParam para filtrar por categoría
    @GetMapping("/productos")
    public String listarProductos(@RequestParam(required = false) String categoria, Model model) {
        List<ProductoDTO> productos = obtenerProductosMock();

        if (categoria != null && !categoria.isEmpty()) {
            productos = productos.stream()
                    .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
                    .toList();
        }

        model.addAttribute("productos", productos);
        model.addAttribute("categoriaSeleccionada", categoria);
        model.addAttribute("estudiante", nombreEstudiante);
        model.addAttribute("proyecto", nombreProyecto);
        return "productos"; // Nombre de la vista HTML (productos.html)
    }

    // b) Vista con formulario usando @ModelAttribute para recibir datos
    @GetMapping("/productos/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("productoForm", new ProductoDTO());
        model.addAttribute("estudiante", nombreEstudiante);
        return "formulario-producto"; // Nombre de la vista HTML (formulario-producto.html)
    }

    @PostMapping("/productos/guardar")
    public String guardarProducto(@ModelAttribute ProductoDTO productoForm, Model model) {
        model.addAttribute("productoGuardado", productoForm);
        model.addAttribute("estudiante", nombreEstudiante);
        return "resultado-producto"; // Vista para mostrar el resultado procesado
    }

    // Endpoint usando @PathVariable para ver el detalle de un elemento
    @GetMapping("/productos/detalle/{nombre}")
    public String verDetalleProducto(@PathVariable String nombre, Model model) {
        ProductoDTO encontrado = obtenerProductosMock().stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElse(new ProductoDTO(nombre, 0.0, "Desconocida"));

        model.addAttribute("producto", encontrado);
        model.addAttribute("estudiante", nombreEstudiante);
        return "detalle-producto"; // Vista HTML de detalle
    }

    // Endpoint POST de tipo REST (regresa JSON) para recibir un objeto DTO
    @PostMapping("/api/productos")
    @ResponseBody
    public ProductoDTO crearProductoRest(@RequestBody ProductoDTO nuevoProducto) {
        // Aquí procesas el DTO recibido en formato JSON desde Postman o Bruno
        return nuevoProducto;
    }
}