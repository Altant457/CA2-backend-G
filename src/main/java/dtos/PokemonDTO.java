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

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }
}
