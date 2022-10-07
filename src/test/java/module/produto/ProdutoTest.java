package module.produto;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

@DisplayName("Testes Rest de API para Produto")
public class ProdutoTest {

    private String token;

    @BeforeEach
    public void beforeEach(){
        // 1° - Variavel URL da API REST
        baseURI = "http://165.227.93.41/";
        //port = 8080;
        basePath = "/lojinha";
        //2° - Get Token  do Usuario
         this.token = given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"usuarioLogin\": \"teste1234\",\n" +
                        "  \"usuarioSenha\": \"03081999\"\n" +
                        "}")
                .when().post("/v2/login")
                .then()
                .extract() //diz que podemos agora retira os dados do objedo do body (não é obrigatorio)
                .path("data.token"); //Valor do objeto do body de login

        System.out.println(token);
    }

    @Test
    @DisplayName("Validar valores limites para Produto igual a 0")
    public void testValidarLimitesZeradoProibidosdeProduto() {


            //3° - Outras operações (Cria produto)
            given()
                    .contentType(ContentType.JSON)
                    .header("token", this.token)
                    .body("{\n" +
                            "  \"produtoNome\": \"Tv led\",\n" +
                            "  \"produtoValor\": 0,\n" +
                            "  \"produtoCores\": [\n" +
                            "    \"azul\"\n" +
                            "  ],\n" +
                            "  \"produtoUrlMock\": \"string\",\n" +
                            "  \"componentes\": [\n" +
                            "    {\n" +
                            "      \"componenteNome\": \"string\",\n" +
                            "      \"componenteQuantidade\": 0\n" +
                            "    }\n" +
                            "  ]\n" +
                            "}")
                    .when()
                    .post("/v2/produtos")
                    .then().assertThat()
                    .body("error",equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                    .statusCode(422);

        }

    @Test
    @DisplayName("Validar valores limites para Produto igual a 7.000,01")
    public void testValidarLimiteSetemilEUmProibidosdeProduto() {

        //3° - Outras operações (Cria produto)
        given()
                .contentType(ContentType.JSON)
                .header("token", this.token)
                .body("{\n" +
                        "  \"produtoNome\": \"Tv led\",\n" +
                        "  \"produtoValor\": 7000.01,\n" +
                        "  \"produtoCores\": [\n" +
                        "    \"azul\"\n" +
                        "  ],\n" +
                        "  \"produtoUrlMock\": \"string\",\n" +
                        "  \"componentes\": [\n" +
                        "    {\n" +
                        "      \"componenteNome\": \"string\",\n" +
                        "      \"componenteQuantidade\": 0\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}")
                .when()
                .post("/v2/produtos")
                .then().assertThat()
                .body("error",equalTo("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00"))
                .statusCode(422);


    }


}
