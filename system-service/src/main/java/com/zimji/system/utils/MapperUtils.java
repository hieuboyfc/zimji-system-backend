package com.zimji.system.utils;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.ObjectUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class MapperUtils {

    static ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public static void mapIgnoreNull(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    public static void mapIgnoreEquals(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getEqualProperties(src, target));
    }

    public static <D, T> D map(T entity, Class<D> outClass) {
        return modelMapper.map(entity, outClass);
    }

    public static <D, T> List<D> map(Collection<T> entityList, Class<D> outCLass) {
        if (ObjectUtils.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        return entityList.stream()
                .map(entity -> map(entity, outCLass))
                .collect(Collectors.toList());
    }

    public static <S, D> D map(S source, D destination) {
        modelMapper.map(source, destination);
        return destination;
    }

    public static <S, D> D copyWithoutAudit(S source, D destination) {
        return copy(source, destination, "createBy", "createDate", "version", "id");
    }

    public static <S, D> D copy(S source, D destination) {
        BeanUtils.copyProperties(source, destination);
        return destination;
    }

    public static <S, D> D copy(S source, D destination, String... ignore) {
        BeanUtils.copyProperties(source, destination, ignore);
        return destination;
    }

    public static boolean equal(Object source, Object target) {
        return Objects.equals(source, target);
    }

    public static List<Map<String, Object>> underscoreToCamelcase(List<Map<String, Object>> list, String... ignore) {
        return list.stream()
                .map(item -> underscoreToCamelcase(item, ignore))
                .collect(Collectors.toList());
    }

    public static Map<String, Object> underscoreToCamelcase(Map<String, Object> map, String... ignore) {
        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> {
                            String key = entry.getKey();
                            return Arrays.asList(ignore).contains(key) ? key : convertToCamelCase(key);
                        },
                        Map.Entry::getValue
                ));
    }

    private static String convertToCamelCase(String key) {
        return Pattern.compile("_([a-z])").matcher(key.toLowerCase()).replaceAll(m -> m.group(1).toUpperCase());
    }

    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        return Arrays.stream(src.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(name -> ObjectUtils.isEmpty(src.getPropertyValue(name)))
                .toArray(String[]::new);
    }

    private static String[] getEqualProperties(Object source, Object target) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        final BeanWrapper des = new BeanWrapperImpl(target);
        return Arrays.stream(src.getPropertyDescriptors())
                .filter(pd -> {
                    Object srcValue = src.getPropertyValue(pd.getName());
                    Object desValue = des.getPropertyValue(pd.getName());
                    return equal(srcValue, desValue);
                })
                .map(FeatureDescriptor::getName)
                .toArray(String[]::new);
    }

}