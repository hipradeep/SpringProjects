package com.hipradeep.code.services.serviceImple;

import com.hipradeep.code.entities.Product;
import com.hipradeep.code.payloads.ProductDto;
import com.hipradeep.code.repositories.ProductRepo;
import com.hipradeep.code.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepo productRepo;

    @Autowired(required = true)
    private ModelMapper modelMapper;

    @Override
    public ProductDto createProduct(ProductDto productDto) {

        Product product=this.productRepo.save(this.modelMapper.map(productDto, Product.class));

        return this.modelMapper.map(product, ProductDto.class);
    }
}
