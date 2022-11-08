package dtos;

public class ComboDTO {
    private final String pokemonId;
    private final String pokemonName;
    private final String pokemonImage;
    private final String randomFact;

    public ComboDTO(PokemonDTO pokemonDTO, RandomFactDTO randomFactDTO) {
        this.pokemonId = pokemonDTO.getId();
        this.pokemonName = pokemonDTO.getName();
        this.pokemonImage = pokemonDTO.getImageURL();
        this.randomFact = randomFactDTO.getFact();
    }
}
