package gestionProductos.service.Impl;

import gestionProductos.model.dao.ProductoDao;
import gestionProductos.model.entity.Producto;
import gestionProductos.service.IProducto;
import org.apache.el.util.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductoImpl implements IProducto {

    @Autowired
    private ProductoDao productoDao;

    @Transactional
    @Override
    public Producto save(Producto producto) {
        return productoDao.save(producto);
    }

    @Transactional(readOnly = true)
    @Override
    public Producto findById(Long id) {
        return productoDao.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Producto> findAll() {
        return productoDao.findAll();
    }

    @Transactional
    @Override
    public void delete(Producto producto) {
        productoDao.delete(producto);
    }

    @Override
    public boolean existById(Long id) {
        return productoDao.existsById(id);
    }

    @Transactional
    @Override
    public Producto updateByMap(Long id, Map<String, Object> fields) {
        Optional<Producto> productoExistente = productoDao.findById(id);

        if (productoExistente.isPresent()){
            Producto producto = productoExistente.get();

            fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Producto.class, key);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, producto, value);
            }
        });
            return productoDao.save(producto);
        }
        return null;
    }
}
