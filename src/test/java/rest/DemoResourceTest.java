package rest;

import entities.Role;
import entities.User;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.core.IsEqual.equalTo;

class DemoResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();

        httpServer.shutdownNow();
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();

            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            User user = new User("user", "test1");
            user.addRole(userRole);
            User admin = new User("admin", "test2");
            admin.addRole(adminRole);
            User both = new User("user_admin", "test3");
            both.addRole(userRole);
            both.addRole(adminRole);
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.persist(both);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    private static String securityToken;

    private static void login(String username, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", username, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                .when().post("/login")
                .then()
                .extract().path("token");
    }

    private void logOut() {
        securityToken = null;
    }

    @Test
    void getPokeInfoAsUser() {
        String json = "{query: \"133\"}";
        login("user", "test1");
        given()
                .contentType("application/json")
                .accept("application/json")
                .header("x-access-token", securityToken)
                .body(json)
                .when().post("/info/pokemon")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo("133"))
                .body("name", equalTo("eevee"));
    }

    @Test
    void getPokeInfoAsAdmin() {
        String json = "{query: \"eevee\"}";
        login("admin", "test2");
        given()
                .contentType("application/json")
                .accept("application/json")
                .header("x-access-token", securityToken)
                .body(json)
                .when().post("/info/pokemon")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo("133"))
                .body("name", equalTo("eevee"));
    }

    @Test
    void createUser() {
        String json = "{userName: test, userPass: 1234}";
        given()
                .contentType("application/json")
                .accept("application/json")
                .body(json)
                .when().post("/info/signup")
                .then()
                .assertThat()
                .statusCode(200)
                .body("userName", equalTo("test"))
                .body("roleList", hasItems(hasEntry("roleName", "user")));
    }
}