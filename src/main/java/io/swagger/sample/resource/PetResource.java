/**
 *  Copyright 2016 SmartBear Software
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.swagger.sample.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.sample.data.PetData;
import io.swagger.sample.model.Pet;

import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/pet")
@Produces({"application/json", "application/xml"})
public class PetResource {
  static PetData petData = new PetData();

  @GET
  @Path("/{petId}")
  @Operation(summary = "Buscar mascota por ID",
    tags = {"pets"},
    description = "Devuelve una mascota cuando 0 <ID <= 10. ID> 10 o no enteros simularán las condiciones de error de la AP",
    operationId = "Buscar mascotas por Id",
    responses = {
            @ApiResponse(description = "La mascota", content = @Content(
                    schema = @Schema(implementation = Pet.class)
            )),
            @ApiResponse(responseCode = "400", description = "El ID suministrado es incorrecto"),
            @ApiResponse(responseCode = "404", description = "Mascota no enconntrada")
    })
  public Response getPetById(
      @Parameter(
              description = "Identificación de la mascota que necesita ser buscada",
              schema = @Schema(
                      type = "integer",
                      format = "int64",
                      description = "Parametro Identificador de la mascota que necesita ser buscada",
                      allowableValues = {"1","2","3"}
              ),
              required = true)
      @PathParam("petId") Long petId) throws io.swagger.sample.exception.NotFoundException {
    Pet pet = petData.getPetById(petId);
    if (null != pet) {
      return Response.ok().entity(pet).build();
    } else {
      throw new io.swagger.sample.exception.NotFoundException(404, "Mascota no encontrada");
    }
  }

  @POST
  @Consumes("application/json")
  @Operation(summary = "Agrega una nueva mascota al store",
    tags = {"pets"},
    operationId = "Agregar mascota a store",
    responses = {
          @ApiResponse(responseCode = "405", description = "Entrada invalida")
  })
  public Response addPet(
      @Parameter(description = "Objeto Pet que sera agregado al store", required = true) Pet pet) {
    petData.addPet(pet);
    return Response.ok().entity("SUCCESS").build();
  }

  @PUT
  @Operation(summary = "Actualiza una mascota existente",
          tags = {"pets"},
          operationId = "Actualizar mascota existente",
          responses = {
                  @ApiResponse(responseCode = "400", description = "El ID suministrado es incorrecto"),
                  @ApiResponse(responseCode = "404", description = "Mascota no encontrada"),
                  @ApiResponse(responseCode = "405", description = "Validation exception") })
  public Response updatePet(
      @Parameter(description = "Objeto mascota que será agregado al store", required = true) Pet pet) {
    petData.addPet(pet);
    return Response.ok().entity("SUCCESS").build();
  }

  @GET
  @Path("/findByStatus")
  @Operation(summary = "Buscar Mascotas por estado",
          tags = {"pets"},
    description = "Se pueden proporcionar valores de múltiples estados con cadenas separadas por comas",
    operationId = "Buscar mascotas por estado",
          responses = {
                  @ApiResponse(
                          content = @Content(mediaType = "application/json",
                                  schema = @Schema(implementation = Pet.class))),
                  @ApiResponse(
                          responseCode = "400", description = "estado invalido"
                  )}
          )
  public Response findPetsByStatus(
      @Parameter(
              description = "Valores de estados que deben ser considerados para el filtro",
              required = true,
              schema = @Schema(
                      allowableValues =  {"available","pending","sold"},
                      defaultValue = "available"
              )
      )
      @QueryParam("status") String status,
      @BeanParam QueryResultBean qr
){
    return Response.ok(petData.findPetByStatus(status)).build();
  }

  @GET
  @Path("/findByTags")
  @Operation(summary = "Buscar mascotas por tags",
          tags = {"pets"},
    description = "Muliples tags se pueden proporcionar en una cadena separada por comas. Utilice tag1, tag2, tag3 para probar.",
    responses = {
            @ApiResponse(description = "Criterios de búsqueda de mascotas",
              content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = Pet.class))
            ),
            @ApiResponse(description = "tag invalido", responseCode = "400")
    })
  @Deprecated
  public Response findPetsByTags(
      @Parameter(description = "tags para ser filtrados", required = true) @QueryParam("tags") String tags) {
    return Response.ok(petData.findPetByTags(tags)).build();
  }
}
