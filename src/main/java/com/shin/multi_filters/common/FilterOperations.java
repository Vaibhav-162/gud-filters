package com.shin.multi_filters.common;

public class FilterOperations {
    public static final String EQUALS = "equals";
    public static final String CONTAINS = "contains";
    public static final String STARTS_WITH = "startsWith";
    public static final String ENDS_WITH = "endsWith";
    public static final String GREATER_THAN = "greaterThan";
    public static final String GREATER_THAN_OR_EQUAL_TO = "greaterThanOrEqualTo";
    public static final String LESS_THAN = "lessThan";
    public static final String LESS_THAN_OR_EQUAL_TO = "lessThanOrEqualTo";
    public static final String IS_NULL = "isNull";
    public static final String IS_NOT_NULL = "isNotNull";

    public static String getOperation(String operation) {
        return switch (operation.toLowerCase()) {
            case "equals", "eq" -> EQUALS;
            case "contains", "ct" -> CONTAINS;
            case "startswith", "sw" -> STARTS_WITH;
            case "endswith", "ew" -> ENDS_WITH;
            case "greaterthan", "gt" -> GREATER_THAN;
            case "greaterthanorequalto", "gte" -> GREATER_THAN_OR_EQUAL_TO;
            case "lessthan", "lt" -> LESS_THAN;
            case "lessthanorequalto", "lte" -> LESS_THAN_OR_EQUAL_TO;
            case "isnull", "null" -> IS_NULL;
            case "isnotnull", "notnull" -> IS_NOT_NULL;
            default -> throw new IllegalArgumentException("Invalid operation: " + operation);
        };
    }
}
