package gestionProductos.service;

import gestionProductos.model.entity.Producto;

import java.util.List;
import java.util.Map;

public interface IProducto {
    Producto save(Producto producto);
    Producto findById(Long id);
    List<Producto> findAll();
    void delete(Producto producto);
    boolean existById(Long id);
    Producto updateByMap(Long id, Map<String, Object> fields);
}
