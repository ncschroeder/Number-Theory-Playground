package com.numbertheoryplayground;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.numbertheoryplayground.CalculationsController.*;

@WebMvcTest(CalculationsController.class)
public class CalculationsControllerTests {
    @Autowired
    private MockMvc mockMvc;
    
    void testBadRequest(String endpoint, String queryParams) {
        String url = String.format("/calculate%s?%s", endpoint, queryParams);
        
        try {
            mockMvc
            .perform(get(url))
            .andDo(print())
            .andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
    @ParameterizedTest
    @FieldSource("singleInputEndpoints")
    void badRequestForSingleInputEndpoints(String endpoint) {
        testBadRequest(endpoint, "input=-1");
    }
    
    static final List<String> singleInputEndpoints =
        List.of(
            PRIMES_ENDPOINT, TWIN_PRIME_PAIR_STARTS_ENDPOINT, PRIME_FACTORIZATION_ENDPOINT,
            DIVISIBILITY_ANSWER_ENDPOINT, GOLDBACH_PRIME_PAIR_STARTS_ENDPOINT,
            PYTHAGOREAN_TRIPLES_ENDPOINT, TWO_SQUARE_THEOREM_ANSWER_ENDPOINT
        );
    
    
    @ParameterizedTest
    @FieldSource("doubleInputEndpoints")
    void badRequestForDoubleInputEndpoints(String endpoint) {
        testBadRequest(endpoint, "input1=-1&input2=10");
        testBadRequest(endpoint, "input1=10&input2=-1");
    }
    
    static final List<String> doubleInputEndpoints =
        List.of(
            GCD_AND_LCM_ANSWER_ENDPOINT, FIBO_LIKE_SEQUENCES_ANSWER_ENDPOINT,
            ANCIENT_MULT_ANSWER_ENDPOINT
        );
}
