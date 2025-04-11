package com.shin.gud_filters.common.util;

import com.shin.gud_filters.common.FilterCriteria;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.jpa.domain.Specification;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GudOldFiltrationSpecificationTest {

    @Mock
    private Root<TestEntity> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder cb;

    @Mock
    private Path<Object> path;

    @Mock
    private Predicate predicate;

    @Mock
    private Expression<Comparable> comparableExpression;

    @Mock
    private Expression<String> stringExpression;

    @Mock
    private Expression<LocalDate> dateExpression;

    private static class TestEntity {
        private String name;
        private Integer age;
        private LocalDate birthDate;
        private LocalDateTime createdDate;
        private Boolean active;
    }

    @BeforeEach
    void setUp() {
        // Setup basic path behavior
        when(root.get(anyString())).thenReturn(path);
        
        // Setup type-specific expressions
        when(path.as(String.class)).thenReturn(stringExpression);
        when(path.as(Comparable.class)).thenReturn(comparableExpression);
        when(path.as(LocalDate.class)).thenReturn(dateExpression);
        
        // Setup string operations
        when(cb.lower(any(Expression.class))).thenReturn(stringExpression);
        when(cb.trim(any(Expression.class))).thenReturn(stringExpression);
        
        // Setup date operations
        when(cb.function(eq("DATE"), eq(LocalDate.class), any())).thenReturn(dateExpression);
        
        // Setup default predicate returns with specific types
        doReturn(predicate).when(cb).equal(any(Expression.class), any());
        doReturn(predicate).when(cb).like(any(Expression.class), anyString());
        doReturn(predicate).when(cb).greaterThan(any(Expression.class), any(Comparable.class));
        doReturn(predicate).when(cb).lessThan(any(Expression.class), any(Comparable.class));
        doReturn(predicate).when(cb).greaterThanOrEqualTo(any(Expression.class), any(Comparable.class));
        doReturn(predicate).when(cb).lessThanOrEqualTo(any(Expression.class), any(Comparable.class));
        doReturn(predicate).when(cb).isNull(any(Expression.class));
        doReturn(predicate).when(cb).notEqual(any(Expression.class), any());
        doReturn(predicate).when(cb).and(any(Predicate[].class));
        doReturn(predicate).when(cb).or(any(Predicate[].class));
        doReturn(predicate).when(cb).conjunction();
    }

    @Test
    void testEqualsOperation() {
        // Given
        FilterCriteria criteria = new FilterCriteria("name", FilterCriteria.FilterOperations.EQUALS, "John");
        when(path.getJavaType()).thenReturn((Class) String.class);
        when(path.as(String.class)).thenReturn(stringExpression);
        when(cb.trim(any(Expression.class))).thenReturn(stringExpression);
        when(cb.lower(stringExpression)).thenReturn(stringExpression);

        // When
        Specification<TestEntity> spec = GudOldFiltrationSpecification.createSpecification(
                List.of(criteria), true);

        // Then
        Predicate result = spec.toPredicate(root, query, cb);
        assertThat(result).isNotNull();
        
        // Verify the sequence of operations for string equality
        verify(cb).function("DATE", LocalDate.class, path);
        verify(cb).trim(any(Expression.class));
        verify(cb).lower(stringExpression);
        verify(cb).equal(stringExpression, "john");
        verify(cb).and(any(Predicate[].class));
    }

    @Test
    void testContainsOperation() {
        // Given
        FilterCriteria criteria = new FilterCriteria("name", FilterCriteria.FilterOperations.CONTAINS, "ohn");
        when(path.getJavaType()).thenReturn((Class) String.class);
        when(path.as(String.class)).thenReturn(stringExpression);
        when(cb.like(any(Expression.class), anyString())).thenReturn(predicate);

        // When
        Specification<TestEntity> spec = GudOldFiltrationSpecification.createSpecification(
                List.of(criteria), true);

        // Then
        Predicate result = spec.toPredicate(root, query, cb);
        assertThat(result).isNotNull();
        
        // Verify the sequence of operations
        verify(cb).function("DATE", LocalDate.class, path);
        verify(cb).like(any(Expression.class), eq("%ohn%"));
        verify(cb).and(predicate);
    }

    @Test
    void testDateOperations() {
        // Given
        LocalDate testDate = LocalDate.now();
        FilterCriteria criteria = new FilterCriteria("birthDate", 
                FilterCriteria.FilterOperations.GREATER_THAN, testDate.toString());
        when(path.getJavaType()).thenReturn((Class) LocalDate.class);

        // When
        Specification<TestEntity> spec = GudOldFiltrationSpecification.createSpecification(
                List.of(criteria), true);

        // Then
        Predicate result = spec.toPredicate(root, query, cb);
        assertThat(result).isNotNull();
        
        // Verify the sequence of operations for date comparison
        verify(cb).function("DATE", LocalDate.class, path);
        verify(cb).greaterThan(dateExpression, testDate);
    }

    @Test
    void testNullValueHandling() {
        // Given
        FilterCriteria criteria = new FilterCriteria("name", FilterCriteria.FilterOperations.IS_NULL, null);
        when(path.getJavaType()).thenReturn((Class) String.class);
        when(cb.isNull(path)).thenReturn(predicate);

        // When
        Specification<TestEntity> spec = GudOldFiltrationSpecification.createSpecification(
                List.of(criteria), true);

        // Then
        Predicate result = spec.toPredicate(root, query, cb);
        assertThat(result).isNotNull();
        
        // Verify the sequence of operations
        verify(cb).function("DATE", LocalDate.class, path);
        verify(cb).isNull(path);
        verify(cb).and(predicate);
    }

    @Test
    void testEmptyCriteriaList() {
        // Given
        List<FilterCriteria> emptyList = List.of();

        // When
        Specification<TestEntity> spec = GudOldFiltrationSpecification.createSpecification(
                emptyList, true);

        // Then
        Predicate result = spec.toPredicate(root, query, cb);
        assertThat(result).isNotNull();
        verify(cb).conjunction();
    }

    @Test
    void testGreaterThanOperation() {
        // Given
        FilterCriteria criteria = new FilterCriteria("age", FilterCriteria.FilterOperations.GREATER_THAN, "25");
        when(path.getJavaType()).thenReturn((Class) Integer.class);
        when(path.as(Comparable.class)).thenReturn(comparableExpression);
        when(cb.greaterThan(any(Expression.class), any(Comparable.class))).thenReturn(predicate);

        // When
        Specification<TestEntity> spec = GudOldFiltrationSpecification.createSpecification(
                List.of(criteria), true);

        // Then
        Predicate result = spec.toPredicate(root, query, cb);
        assertThat(result).isNotNull();
        
        // Verify the sequence of operations
        verify(cb).function("DATE", LocalDate.class, path);
        verify(cb).greaterThan(any(Expression.class), eq(25));
        verify(cb).and(predicate);
    }

    @Test
    void testLessThanOperation() {
        // Given
        FilterCriteria criteria = new FilterCriteria("age", FilterCriteria.FilterOperations.LESS_THAN, "30");
        when(path.getJavaType()).thenReturn((Class) Integer.class);
        when(path.as(Comparable.class)).thenReturn(comparableExpression);
        when(cb.lessThan(any(Expression.class), any(Comparable.class))).thenReturn(predicate);

        // When
        Specification<TestEntity> spec = GudOldFiltrationSpecification.createSpecification(
                List.of(criteria), true);

        // Then
        Predicate result = spec.toPredicate(root, query, cb);
        assertThat(result).isNotNull();
        
        // Verify the sequence of operations
        verify(cb).function("DATE", LocalDate.class, path);
        verify(cb).lessThan(any(Expression.class), eq(30));
        verify(cb).and(predicate);
    }

    @Test
    void testMultipleCriteria() {
        // Given
        FilterCriteria nameCriteria = new FilterCriteria("name", FilterCriteria.FilterOperations.CONTAINS, "ohn");
        FilterCriteria ageCriteria = new FilterCriteria("age", FilterCriteria.FilterOperations.GREATER_THAN, "25");
        
        // Setup name path
        when(root.get("name")).thenReturn(path);
        when(path.getJavaType()).thenReturn((Class) String.class);
        when(path.as(String.class)).thenReturn(stringExpression);
        when(cb.like(any(Expression.class), anyString())).thenReturn(predicate);
        
        // Setup age path
        Path<Object> agePath = mock(Path.class);
        when(root.get("age")).thenReturn(agePath);
        when(agePath.getJavaType()).thenReturn((Class) Integer.class);
        when(agePath.as(Comparable.class)).thenReturn(comparableExpression);
        when(cb.greaterThan(any(Expression.class), any(Comparable.class))).thenReturn(predicate);

        // When
        Specification<TestEntity> spec = GudOldFiltrationSpecification.createSpecification(
                List.of(nameCriteria, ageCriteria), true);

        // Then
        Predicate result = spec.toPredicate(root, query, cb);
        assertThat(result).isNotNull();
        
        // Verify the sequence of operations
        verify(cb).function("DATE", LocalDate.class, path);
        verify(cb).function("DATE", LocalDate.class, agePath);
        verify(cb).like(any(Expression.class), eq("%ohn%"));
        verify(cb).greaterThan(any(Expression.class), eq(25));
        verify(cb).and(any(Predicate[].class));
    }

    @Test
    void testBooleanOperation() {
        // Given
        FilterCriteria criteria = new FilterCriteria("active", FilterCriteria.FilterOperations.EQUALS, "true");
        when(path.getJavaType()).thenReturn((Class) Boolean.class);
        when(cb.equal(path, true)).thenReturn(predicate);

        // When
        Specification<TestEntity> spec = GudOldFiltrationSpecification.createSpecification(
                List.of(criteria), true);

        // Then
        Predicate result = spec.toPredicate(root, query, cb);
        assertThat(result).isNotNull();
        
        // Verify the sequence of operations
        verify(cb).function("DATE", LocalDate.class, path);
        verify(cb).equal(path, true);
        verify(cb).and(any(Predicate[].class));
    }

    @Test
    void testDateTimeOperation() {
        // Given
        LocalDateTime testDateTime = LocalDateTime.now();
        FilterCriteria criteria = new FilterCriteria("createdDate", 
                FilterCriteria.FilterOperations.GREATER_THAN, testDateTime.toString());
        when(path.getJavaType()).thenReturn((Class) LocalDateTime.class);
        when(path.as(Comparable.class)).thenReturn(comparableExpression);
        when(cb.greaterThan(any(Expression.class), any(Comparable.class))).thenReturn(predicate);

        // When
        Specification<TestEntity> spec = GudOldFiltrationSpecification.createSpecification(
                List.of(criteria), true);

        // Then
        Predicate result = spec.toPredicate(root, query, cb);
        assertThat(result).isNotNull();

        // Verify the sequence of operations
        verify(cb).function("DATE", LocalDate.class, path);
        verify(cb).greaterThan(any(Expression.class), eq(testDateTime));
        verify(cb).and(predicate);
    }
} 