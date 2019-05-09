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
import io.swagger.sample.data.UserData;
import io.swagger.sample.exception.ApiException;
import io.swagger.sample.model.User;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

@Path("/user")
@Produces({"application/json", "application/xml"})
public class UserResource {
  static UserData userData = new UserData();

  @POST
  @Operation(summary = "Creación de usuario",
    operationId = "Crear usuario",
    tags = {"users"},
    description = "Esto solo puede hacerlo el usuario que ha iniciado sesión")
  public Response createUser(
      @Parameter(description = "Objeto de usuario creado", required = true) User user) {
    userData.addUser(user);
    return Response.ok().entity("").build();
  }

  @POST
  @Path("/createWithArray")
  @Operation(summary = "Crear lista de usuarios dado un arreglo",
          tags = {"users"},
		  operationId = "Crear lista de usuarios"
		  )
  public Response createUsersWithArrayInput(@Parameter(description = "Lista de objetos tipo User", required = true) User[] users) {
      for (User user : users) {
          userData.addUser(user);
      }
      return Response.ok().entity("").build();
  }

  @POST
  @Path("/createWithList")
  @Operation(summary = "Crear lista de usuarios dada una lista",
          tags = {"users"},
          operationId = "Crear usuarios de una lista"
		  )
  public Response createUsersWithListInput(@Parameter(description = "List of user object", required = true) java.util.List<User> users) {
      for (User user : users) {
          userData.addUser(user);
      }
      return Response.ok().entity("").build();
  }

  @PUT
  @Path("/{username}")
  @Operation(summary = "Usuario actualizado",
    description = "Esta operación solo la puede hacer un usuario logueado",
          tags = {"users"},
          operationId = "Actualizar usuario",
    responses = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
            @ApiResponse(responseCode = "400", description = "Usuario invalido"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado") })
  public Response updateUser(
      @Parameter(description = "nombre de usuario a actualizar", required = true) @PathParam("username") String username,
      @Parameter(description = "Objeto de usuario para actualizar", required = true) User user) {
    userData.addUser(user);
    return Response.ok().entity("").build();
  }

  @DELETE
  @Path("/{username}")
  @Operation(summary = "Usuario eliminado",
    description = "Esta operación solo la puede hacer un usuario logueado.",
          tags = {"users"},
    responses = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado"),
            @ApiResponse(responseCode = "400", description = "Username incorrecto"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontado") })
  public Response deleteUser(
      @Parameter(description = "El nombre de usuario a eliminar", required = true) @PathParam("username") String username) {
    if (userData.removeUser(username)) {
      return Response.ok().entity("").build();
    } else {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
  }

  @GET
  @Path("/{username}")
  @Operation(summary = "Obtener usuario dado su username",
          tags = {"users"},
          operationId = "Obtener usuario por username",
          description = "Dado el username se debe buscar el usuario que coincida",
    responses = {
            @ApiResponse(description = "The user",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "username invalido"),
            @ApiResponse(responseCode = "400", description = "Usuario no encontardo") })
  public Response getUserByName(
      @Parameter(description = "El nombre de usuario a buscar. Use user1 para la prueba. ", required = true) @PathParam("username") String username)
    throws ApiException {
    User user = userData.findUserByName(username);
    if (null != user) {
      return Response.ok().entity(user).build();
    } else {
      throw new io.swagger.sample.exception.NotFoundException(404, "Usuario no encontrado");
    }
  }

  @GET
  @Path("/login")
  @Operation(summary = "Autentica el usuario en el sistema",
          tags = {"users"},
          operationId = "Autenticar un usuario",
          description = "Autenticación de un usuario",
    responses = {
            @ApiResponse(description = "El usuario se autentica satisfactoriamente",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Usuario o contraseña invalidos") })
  public Response loginUser(
      @Parameter(description = "el username correspondiente al usuario", required = true) @QueryParam("username") String username,
      @Parameter(description = "La contraseña para iniciar sesión en texto claro", required = true) @QueryParam("password") String password) {
    return Response.ok()
        .entity("sesión iniciada sesión de usuarion:" + System.currentTimeMillis())
        .build();
  }

  @GET
  @Path("/logout")
  @Operation(summary = "Se desconecta la sesión actual iniciada en el usuario.",
          operationId = "cerrar sesión de un usuario",
          description = "Se cierra la sesión iniciada por el usuario",
          tags = {"users"})
  public Response logoutUser() {
    return Response.ok().entity("").build();
  }
}
