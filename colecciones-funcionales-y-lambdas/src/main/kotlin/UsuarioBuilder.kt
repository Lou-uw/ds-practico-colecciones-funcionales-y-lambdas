/**
 * Ejercicio 5: It y Scope Functions (run, apply, also, let)
 *
 * Implementa los métodos de esta clase para que pasen todos los tests
 * del archivo Ejercicio5ItScopeFunctionsTest.kt
 *
 * IMPORTANTE: No modifiques la firma de los métodos, solo implementa su lógica.
 * IMPORTANTE: Debes usar las scope functions indicadas en cada sección.
 */

data class Usuario(
    var id: Int = 0,
    var nombre: String = "",
    var email: String = "",
    var activo: Boolean = false,
    var roles: MutableList<String> = mutableListOf(),
    var configuracion: ConfiguracionUsuario? = ConfiguracionUsuario(),
)

data class ConfiguracionUsuario(
    var tema: String = "claro",
    var idioma: String = "es",
    var notificaciones: Boolean = true,
    var nivelPrivacidad: Int = 1,
)

data class Validacion(
    val campo: String,
    val valido: Boolean,
    val mensaje: String,
)

class UsuarioBuilder {
    // Parte A: Uso del parámetro implícito 'it'

    fun procesarNumeros(numeros: List<Int>): List<Int> {
        return numeros.filter{it % 2 == 0}
            .map {it * 10}
    }

    fun validarUsuarios(usuarios: List<Usuario>): List<List<Validacion>> {
        return usuarios.map {
            listOf(
                Validacion(campo = "nombre", mensaje = "El nombre no puede estar vacío", valido = it.nombre.isNotEmpty()),
                Validacion(campo = "email", mensaje = "El email debe contener @", valido = it.email.contains("@")),
                Validacion(campo = "roles", mensaje = "El usuario debe tener al menos un rol", valido = it.roles.isNotEmpty())
            )
        }
    }

    fun procesarTextos(textos: List<String>): List<String> {
        return textos.map { it.trim() }
            .map { it.lowercase() }
            .filter { it.isNotEmpty() }
    }

    // Parte B: Función run

    fun calcularNivelAcceso(usuario: Usuario): Int {
        return usuario.run {
            (if (activo) 10 else 0) + (roles.size * 5) + (if (email.contains("@empresa.com")) 5 else 0)
        }

    }

    fun crearUsuarioConTipo(tipo: String): Usuario {
        val usuarioNuevo = Usuario(
        roles = mutableListOf(),
        configuracion = ConfiguracionUsuario(notificaciones = false, nivelPrivacidad = 1)
    )
        return usuarioNuevo.run {
            when (tipo) {
                "ADMIN" -> {
                    roles = mutableListOf("ADMIN")
                    configuracion.notificaciones = true
                    configuracion.nivelPrivacidad = 3
                }

                "USER" -> {
                    roles = mutableListOf("USER")
                    configuracion.nivelPrivacidad = 1
                }
            }
            this
        }
    // Parte C: Función apply

    fun crearUsuarioCompleto(
        nombre: String,
        email: String,
        roles: List<String>,
    ): Usuario {
        return  Usuario(
            nombre =nombre,
            email =email,
            roles = roles.toMutableList(),
            activo = false
        ).apply {
          this.activo = true
        }
    }

    fun actualizarUsuario(
        usuario: Usuario,
        actualizacion: Usuario.() -> Unit,
    ): Usuario {
        return usuario.apply {
            actualizacion()
        }
    }

    // Parte D: Función also

    fun crearUsuarioConLog(
        nombre: String,
        email: String,
        onLog: (String) -> Unit,
    ): Usuario {
        return Usuario(
            nombre = nombre,
            roles = mutableListOf(),
            configuracion = ConfiguracionUsuario(notificaciones = false, nivelPrivacidad = 1)
        ).also { usuario ->
            onLog("Usuario creado: ${usuario.nombre}")
        }.also { usuario ->
            usuario.email = email
            onLog("Email asignado: ${usuario.email}")
        }.also { usuario ->
            usuario.activo = true
            onLog("Usuario activado")
        }
    }

    fun crearYValidar(
        nombre: String,
        email: String,
    ): Pair<Usuario, Boolean> {
        var esValido = false

        val usuario = Usuario(
            nombre = nombre,
            email = email,
            roles = mutableListOf(),
            configuracion = ConfiguracionUsuario(
                notificaciones = false,
                nivelPrivacidad = 1
            )
        ).also {
            esValido = nombre.isNotBlank() && email.contains("@")
        }

        return Pair(usuario, esValido)
    }
    }

    // Parte E: Función let

    fun procesarEmailOpcional(email: String?): String {
        return email?.let {
            "Usuario con email: $it"
        } ?: "Usuario sin email"
    }

    fun generarMensajesBienvenida(usuarios: List<Usuario>): List<String> {
        return usuarios.mapNotNull { usuario ->
            if (usuario.activo && usuario.email.isNotBlank()) {
                usuario.email.let {
                    "Bienvenido/a ${usuario.nombre} ($it)"
                }
            } else {
                null
            }
        }
    }

    // Parte F: Combinación de Scope Functions

    fun procesarUsuarioComplejo(datosBase: Map<String, String>): Usuario? {

        val nombre = datosBase["nombre"] ?: return null
        val email = datosBase["email"] ?: return null

        return Usuario(
            nombre = nombre,
            email = email,
            roles = mutableListOf(),
            configuracion = ConfiguracionUsuario(
                notificaciones = false,
                nivelPrivacidad = 1
            )
        ).run {

            apply {
                activo = true
            }

                .also {
                    if (datosBase["departamento"] == "IT") {
                        configuracion.temaOscuro = true
                        roles.add("IT_USER")
                    }
                }
        }
    }

    fun procesarLoteUsuarios(usuarios: List<Usuario>): List<Usuario> {

        return usuarios.map { usuario ->

            usuario
                .apply {
                    activo = true
                    configuracion.notificaciones = true
                }

                .also {
                    if (roles.isEmpty()) {
                        roles.add("USER")
                    }
                }

                .run {
                    if (nombre == "Admin") {
                        roles.add("ADMIN")
                        configuracion.nivelPrivacidad = 3
                    }

                    this
                }
        }
    }

    fun parsearYCrearUsuario(datosRaw: String): Usuario? {
        val partes = datosRaw.split("|")

        var nombre = ""
        var email = ""

        partes.forEach {
            val dato = it.split(":")
            if (dato[0] == "nombre") {
                nombre = dato[1]
            }
            if (dato[0] == "email")
            { email = dato[1]
                }
            }
            return Usuario(
                nombre = nombre,
                email = email,
                roles = mutableListOf(),
                configuracion = ConfiguracionUsuario(
                    notificaciones = false,
                    nivelPrivacidad = 1
                )
            ).apply {
                activo = true
            }
        }
    }