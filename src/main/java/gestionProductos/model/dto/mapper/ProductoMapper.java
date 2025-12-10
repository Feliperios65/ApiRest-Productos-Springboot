package gestionProductos.model.dto.mapper;

import gestionProductos.model.dto.ProductoDto;
import gestionProductos.model.entity.Producto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductoMapper {
//    ProductoDtoRequest --> ProductoEntityResponse
    public Producto toEntity(ProductoDto dto){
        if (dto == null){
            return null;
        }

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setCategoria(dto.getCategoria());
        producto.setPrecio(dto.getPrecio());
        producto.setCantidad(dto.getCantidad());

        return producto;
    }

//    ProductoEntity --> ProductoDtoResponse

    public ProductoDto toResponseDto (Producto producto){
        if (producto == null){
            return null;
        }

        return ProductoDto.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .categoria(producto.getCategoria())
                .precio(producto.getPrecio())
                .cantidad(producto.getCantidad())
                .build();
    }

//     ListProductosEntity --> ListProductosDTO
    public List<ProductoDto> toResponseDtoList(List<Producto> productos){
        if (productos == null){
            return null;
        }

        return productos.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

//    Actualiza Producto existente con datos del DTO
    public void updateEntityFromDto(Producto producto, ProductoDto dto){
        if (producto == null || dto == null){
            return;
        }

        producto.setNombre(dto.getNombre());
        producto.setCategoria(dto.getCategoria());
        producto.setPrecio(dto.getPrecio());
        producto.setCantidad(dto.getCantidad());
    }
}
