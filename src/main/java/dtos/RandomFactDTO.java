package dtos;

public class RandomFactDTO {
    private final String fact;

    public RandomFactDTO(String randomFact) {
        this.fact = randomFact;
    }

    public String getFact() {
        return fact;
    }
}
