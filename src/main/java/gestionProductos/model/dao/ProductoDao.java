package gestionProductos.model.dao;

import gestionProductos.model.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoDao extends JpaRepository<Producto, Long> {
}
