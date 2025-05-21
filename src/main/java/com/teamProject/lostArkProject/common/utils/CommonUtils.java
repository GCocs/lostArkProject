package com.teamProject.lostArkProject.common.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommonUtils {
    /**
     * List 객체를 Map 객체로 변환하는 메서드
     * @param list - 원본 List
     * @param keyExtractor - 키 추출 함수
     * @return 변환된 Map
     */
    public static<T, K> Map<K, T> listToMap(List<T> list, Function<T, K> keyExtractor) {
        return list.stream().collect(Collectors.toMap(
                keyExtractor,  // 키 추출 함수 (List::getKey)
                Function.identity(),  // 원본 객체
                (a, b) -> a)  // 키 충돌 전략 (키 충돌 시 oldValue, newValue 중 oldValue 선택)
        );
    }

    /**
     * List 객체를 Map 객체로 변환하고, 중복되는 키에 숫자를 추가하는 메서드
     * @param list - 원본 List
     * @param keyExtractor - 키 추출 함수
     * @return 변환된 Map
     */
    public static <T,K> Map<String, T> listToNumberedKeyMap(List<T> list, Function<T, K> keyExtractor) {
        Map<String, T> result = new HashMap<>();          // List를 변환한 Map
        Map<String, Integer> keyCount = new HashMap<>();  // 각 키마다 카운트를 저장하는 Map

        for (T t : list) {
            K rawKey = keyExtractor.apply(t);    // 키 추출 함수로 실제 키 추출
            String baseKey = rawKey.toString();  // 카운트 Map에 저장하는 원본 키
            String key = baseKey;  // 반환 Map에 저장되는 키 (key + count)

            if (result.containsKey(key)) {  // 키가 중복되는 경우
                int count = keyCount.getOrDefault(baseKey, 1);
                while (result.containsKey(key)) {
                    count++;
                    key = baseKey + count;
                }
                keyCount.put(baseKey, count);
            } else {  // 키가 중복되지 않는 경우
                keyCount.put(baseKey, 1);
            }

            result.put(key, t);
        }

        return result;
    }

}
