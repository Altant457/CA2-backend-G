package dtos;

public class PokemonDTO {
    private final String id;
    private final String name;
    private final String imageURL;

    public PokemonDTO(String id, String name, String imageURL) {
        this.id = id;
        this.name = name;
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "PokemonDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
