package com.numbertheoryplayground.calculationsimpl.gcdandlcm;

import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.numbertheoryplayground.calculationsimpl.PrimeFactorization;

import static com.numbertheoryplayground.InputValidation.assertIsInRange;
import static com.numbertheoryplayground.calculationsimpl.PrimeFactorization.FactorAndPower;
import static com.numbertheoryplayground.calculationsimpl.gcdandlcm.GcdAndLcmAnswer.*;

/**
 * This class uses prime factorizations to find the greatest common divisor (GCD) and
 * least common multiple (LCM) of 2 ints.
 */
public final class PrimeFactorizationAnswer {
    private final List<FactorAndPower> input1Fps;
    
    private final List<FactorAndPower> input2Fps;
    
    /**
     * If the GCD of the inputs is 1, this is null since only integers > 1 have a prime factorization.
     */
    private final PrimeFactorization gcdPf;
    
    private final PrimeFactorization lcmPf;
    
    PrimeFactorizationAnswer(int input1, int input2) {
        assertIsInRange(input1, MIN_INPUT, MAX_INPUT);
        assertIsInRange(input2, MIN_INPUT, MAX_INPUT);
        
        var input1Pf = new PrimeFactorization(input1);
        var input2Pf = new PrimeFactorization(input2);
        input1Fps = input1Pf.getFps();
        input2Fps = input2Pf.getFps();
        var gcdFps = new ArrayList<FactorAndPower>();
        var lcmFps = new ArrayList<FactorAndPower>();
        
        for (FactorAndPower fp : input1Pf.getFps()) {
            int factor = fp.factor();
            int power1 = fp.power();
            
            input2Pf
            .findPowerOf(factor)
            .ifPresentOrElse(
                power2 -> {
                    gcdFps.add(new PrimeFactorization.FactorAndPower(factor, Math.min(power1, power2)));
                    lcmFps.add(new PrimeFactorization.FactorAndPower(factor, Math.max(power1, power2)));
                },
                () -> lcmFps.add(new PrimeFactorization.FactorAndPower(factor, power1))
            );
        }
        
        for (PrimeFactorization.FactorAndPower fp : input2Pf.getFps()) {
            if (!input1Pf.containsFactor(fp.factor())) {
                lcmFps.add(new PrimeFactorization.FactorAndPower(fp.factor(), fp.power()));
            }
        }
        
        gcdPf = gcdFps.isEmpty() ? null : new PrimeFactorization(gcdFps);
        lcmPf = new PrimeFactorization(lcmFps);
    }
    
    public List<PrimeFactorization.FactorAndPower> getInput1Fps() {
        return input1Fps;
    }
    
    public List<PrimeFactorization.FactorAndPower> getInput2Fps() {
        return input2Fps;
    }
    
    @JsonIgnore
    public Optional<PrimeFactorization> getGcdPf() {
        return Optional.ofNullable(gcdPf);
    }
    
    @JsonProperty("gcdPf")
    public Optional<PrimeFactorization.Dto> getGcdPfDto() {
        return getGcdPf().map(PrimeFactorization::toDto);
    }
    
    @JsonIgnore
    public PrimeFactorization getLcmPf() {
        return lcmPf;
    }
    
    @JsonProperty("lcmPf")
    public PrimeFactorization.Dto getLcmPfDto() {
        return lcmPf.toDto();
    }
}