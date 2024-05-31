package com.example.MultiVersion.Concurrency.Control_Pattern.mapper;

import com.example.MultiVersion.Concurrency.Control_Pattern.dto.ProductEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ProductMapper {
    @Select("SELECT * FROM public.products WHERE id = #{id}")
    ProductEntity selectById(int id);

    @Update("UPDATE public.products SET name = #{name}, price = #{price}, version = version + 1 WHERE id = #{id}")
    int updateIfVersionMatches(ProductEntity product);
}
