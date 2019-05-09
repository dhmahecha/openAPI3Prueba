package io.swagger.sample.resource;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "Primer ejemplo de API", version = "1.0", 
		description = "Con este ejemplo se busca probar las herramientas usadas en el ciclo de vida de Gobierno de APIs"),
        servers = @Server(url = "/api"))
public class Metadata {
}
