package gestionProductos.controller;

import gestionProductos.exception.BadRequestException;
import gestionProductos.exception.ResourceNotFoundException;
import gestionProductos.model.dto.ProductoDto;
import gestionProductos.model.dto.mapper.ProductoMapper;
import gestionProductos.model.entity.Producto;
import gestionProductos.model.payload.MensajeResponse;
import gestionProductos.service.IProducto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/producto")
public class ProductoController {

    @Autowired
    private IProducto productoService;

    @Autowired
    private ProductoMapper productoMapper;

    @GetMapping
    public ResponseEntity<?> getAll(){
        List<Producto> productos = productoService.findAll();
        if(productos == null || productos.isEmpty()){
            throw new ResourceNotFoundException("productos");
        }

        List<ProductoDto> productoDtos = productoMapper.toResponseDtoList(productos);

        return new ResponseEntity<>(MensajeResponse.builder().
                mensaje("")
                .object(productoDtos)
                .build(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id){
        Producto producto = productoService.findById(id);

        if(producto == null){
            throw new ResourceNotFoundException("producto", "id", id);
        }

        ProductoDto productoDto = productoMapper.toResponseDto(producto);

        return new ResponseEntity<>(MensajeResponse.builder()
                .mensaje("")
                .object(productoDto)
                .build(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create (@Valid @RequestBody ProductoDto productoDto){
        if (productoDto == null){
            throw new BadRequestException("El producto no puede ser nulo");
        }
        try {
            //DTO --> Entidad
            Producto producto = productoMapper.toEntity(productoDto);
            //Guardar en BD
            Producto productoSave = productoService.save(producto);
            //Entidad --> DTOResponse
            ProductoDto responseDTO = productoMapper.toResponseDto(productoSave);

            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Guardado correctamente")
                    .object(responseDTO)
                    .build(),HttpStatus.CREATED);
        }catch (DataAccessException exDt){
            throw new BadRequestException(exDt.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody ProductoDto productoDto, @PathVariable Long id){
        try {
            if(productoService.existById(id)){
                Producto productoExist = productoService.findById(id);

                //Actualizar con los campos del DTO
                productoMapper.updateEntityFromDto(productoExist, productoDto);
                productoExist.setId(id);

                //Guardar
                Producto productoUpdate = productoService.save(productoExist);

                //Entity --> DTOResponse
                ProductoDto responseDto = productoMapper.toResponseDto(productoUpdate);

                return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje("Actualizado correctamente")
                        .object(responseDto)
                        .build(),HttpStatus.OK);
            }else{
                throw new ResourceNotFoundException("producto", "id", id);
            }
        } catch (DataAccessException exDt) {
            throw new BadRequestException(exDt.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        try {
            Producto productoDelete = productoService.findById(id);
            if (productoDelete == null){
                throw new ResourceNotFoundException("producto", "id", id);
            }
            productoService.delete(productoDelete);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (DataAccessException exDt){
            throw new BadRequestException(exDt.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchUpdate(@PathVariable Long id, @RequestBody Map<String, Object> fields){
        Producto productoPatch = null;
        try {
            if(productoService.existById(id)){
                productoPatch = productoService.updateByMap(id, fields);

                //Entity --> DTOResponse
                ProductoDto responseDto = productoMapper.toResponseDto(productoPatch);

                return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje("Actualizado correctamente")
                        .object(responseDto)
                        .build(), HttpStatus.OK);
            } else {
                throw new ResourceNotFoundException("producto", "id", id);
            }
        } catch (DataAccessException exDt){
            throw new BadRequestException(exDt.getMessage());
        }
    }
}
