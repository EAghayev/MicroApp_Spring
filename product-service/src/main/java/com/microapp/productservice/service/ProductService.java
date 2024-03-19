package com.microapp.productservice.service;

import com.microapp.productservice.dto.InventoryCreateRequestDto;
import com.microapp.productservice.dto.ProductCreateDto;
import com.microapp.productservice.dto.ProductGetDto;
import com.microapp.productservice.exception.ResponseMessage;
import com.microapp.productservice.exception.RestException;
import com.microapp.productservice.model.Product;
import com.microapp.productservice.repository.ProductJpaRepository;
import com.microapp.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {


    private final ProductJpaRepository productJpaRepository;
    private final ProductRepository productRepository;
    private final WebClient webClient;

    public ProductGetDto create(ProductCreateDto createDto){

        if(productJpaRepository.existsByName(createDto.getName())){
            throw new RestException(HttpStatus.BAD_REQUEST,"name",ResponseMessage.ERROR_PRODUCT_EXISTS_BY_NAME);
        }

        Product entity = Product.builder()
                        .name(createDto.getName())
                        .description(createDto.getDesc())
                        .salePrice(createDto.getSalePrice())
                        .costPrice(createDto.getCostPrice())
                        .skuCode(generateSkuCode(createDto.getName()))
                        .build();


        productJpaRepository.save(entity);

        InventoryCreateRequestDto inventoryRequest = InventoryCreateRequestDto
                .builder()
                .skuCode(entity.getSkuCode())
                .quantity(createDto.getQuantity()).build();

        var response = webClient.post()
                .uri("http://localhost:8080/inventories")
                .body(Mono.just(inventoryRequest),InventoryCreateRequestDto.class)
                .retrieve().bodyToMono(Object.class).block();

        return mapToDto(entity);
    }

    public Product update(Long id,ProductCreateDto updateDto){

        Product entity = productJpaRepository.findById(id).orElseThrow(()->new RestException(HttpStatus.NOT_FOUND,ResponseMessage.ERROR_PRODUCT_NOT_FOUND_BY_ID));
        if(productRepository.existsByNameExceptId(updateDto.getName(),id)){
            throw new RestException(HttpStatus.BAD_REQUEST,"name", ResponseMessage.ERROR_PRODUCT_EXISTS_BY_NAME);
        }

        entity.setName(updateDto.getName());
        entity.setDescription(updateDto.getDesc());
        entity.setSalePrice(updateDto.getSalePrice());
        entity.setCostPrice(updateDto.getCostPrice());

        productJpaRepository.save(entity);
        return entity;
    }

    public List<ProductGetDto> getAll(){
        var entities = productJpaRepository.findAll();
        List<ProductGetDto> productDtos = entities.stream().map(this::mapToDto).toList();

        return productDtos;
    }

    public List<ProductGetDto> getAll(List<Long> ids){
        var entities = productJpaRepository.findAllByIdIn(ids);
        List<ProductGetDto> productDtos = entities.stream().map(this::mapToDto).toList();

        return productDtos;
    }

    public ProductGetDto getById(Long id){
        Product entity = productJpaRepository.findById(id).orElseThrow(()->new RestException(HttpStatus.NOT_FOUND,ResponseMessage.ERROR_PRODUCT_NOT_FOUND_BY_ID));

        return mapToDto(entity);
    }

    private String generateSkuCode(String name){
        return name.toLowerCase().trim().replace(" ","_");
    }

    private ProductGetDto mapToDto(Product entity){
        return ProductGetDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .costPrice(entity.getCostPrice())
                .salePrice(entity.getSalePrice())
                .skuCode(entity.getSkuCode())
                .build();
    }

}
