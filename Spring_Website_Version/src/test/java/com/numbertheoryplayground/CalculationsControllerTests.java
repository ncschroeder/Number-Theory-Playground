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
class CalculationsControllerTests {
    @Autowired
    private MockMvc mockMvc;
    
    void testBadRequest(String endpointEnd, String queryString) {
        String endpoint = String.format("/calculate/%s?%s", endpointEnd, queryString);
        
        try {
            mockMvc
            .perform(get(endpoint))
            .andDo(print())
            .andExpect(status().isBadRequest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
    @ParameterizedTest
    @FieldSource("singleInputEndpointEnds")
    void badRequestForSingleInputEndpoints(String endpointEnd) {
        testBadRequest(endpointEnd, "input=-1");
    }
    
    static final List<String> singleInputEndpointEnds =
        List.of(
            PRIMES_ENDPOINT_END,
            SEMIPRIMES_DATA_ENDPOINT_END,
            TWIN_PRIME_PAIR_STARTS_ENDPOINT_END,
            PF_ENDPOINT_END,
            DIVIS_ANSWER_ENDPOINT_END,
            GOLDBACH_PRIME_PAIR_STARTS_ENDPOINT_END,
            PYTHAG_TRIPLES_ENDPOINT_END,
            TWO_SQUARE_THEOREM_ANSWER_ENDPOINT_END
        );
    
    
    @ParameterizedTest
    @FieldSource("doubleInputEndpointEnds")
    void badRequestForDoubleInputEndpoints(String endpointEnd) {
        testBadRequest(endpointEnd, "input1=-1&input2=10");
        testBadRequest(endpointEnd, "input1=10&input2=-1");
    }
    
    static final List<String> doubleInputEndpointEnds =
        List.of(
            GCD_AND_LCM_ANSWER_ENDPOINT_END,
            FIBO_LIKE_SEQUENCES_ANSWER_ENDPOINT_END,
            ANCIENT_MULT_ANSWER_ENDPOINT_END
        );
}