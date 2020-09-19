package com.udacity.project4.infrastructure.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class StringUtils {
    /**
     * Utility method to convert object to json string
     * @param object
     * @return json
     */
    public static String convertObjectToJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch(JsonProcessingException e) {
            // Ignore
        }
        return null;
    }
}
