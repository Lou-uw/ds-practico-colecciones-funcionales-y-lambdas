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
    var configuracion: ConfiguracionUsuario = ConfiguracionUsuario(),
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
        return Usuario().run {
            when (tipo.uppercase()) {
                "ADMIN" -> {
                    roles = mutableListOf("ADMIN")
                    configuracion.nivelPrivacidad = 3
                    configuracion.notificaciones = true
                }
                "USER" -> {
                    roles = mutableListOf("USER")
                    configuracion.nivelPrivacidad = 1
                    configuracion.notificaciones = false
                }
                else -> {

                }
            }
            this
        }
    }

    // Parte C: Función apply

    fun crearUsuarioCompleto(
        nombre: String,
        email: String,
        roles: List<String>,
    ): Usuario {
        return Usuario().apply {
            this.nombre = nombre
            this.email = email
            this.roles = roles.toMutableList()
            this.activo = true
            this.configuracion = ConfiguracionUsuario()
        }
    }

    fun actualizarUsuario(
        usuario: Usuario,
        actualizacion: Usuario.() -> Unit,
    ): Usuario {
        return usuario.apply(actualizacion)
    }

    // Parte D: Función also

    fun crearUsuarioConLog(
        nombre: String,
        email: String,
        onLog: (String) -> Unit,
    ): Usuario {
        return Usuario(nombre = nombre).also {
            onLog("Usuario creado: ${it.nombre}")
        }.also {
            it.email = email
            onLog("Email asignado: ${it.email}")
        }.also {
            it.activo = true
            onLog("Usuario activado")
        }
    }

    fun crearYValidar(
        nombre: String,
        email: String,
    ): Pair<Usuario, Boolean> {
        var esValido = false
        val usuario = Usuario(nombre = nombre, email = email).also {
            esValido = it.nombre.isNotEmpty() && it.email.contains("@")
        }
        return Pair(usuario, esValido)
    }

    // Parte E: Función let

    fun procesarEmailOpcional(email: String?): String {
        return email?.let { "Usuario con email: $it" } ?: "Usuario sin email"
    }

    fun generarMensajesBienvenida(usuarios: List<Usuario>): List<String> {
        return usuarios.filter { it.activo && it.email.isNotEmpty() }
            .map { u ->
                u.email.let { "Bienvenido/a ${u.nombre} ($it)" }
            }
    }

    // Parte F: Combinación de Scope Functions

    fun procesarUsuarioComplejo(datosBase: Map<String, String>): Usuario? {
        val nombre = datosBase["nombre"]
        val email = datosBase["email"]

        if (nombre == null || email == null) return null

        return Usuario().run {
            this.nombre = nombre
            this.email = email
            this
        }.apply {
            configuracion = ConfiguracionUsuario()
        }.also {
            if (datosBase["departamento"] == "IT") {
                it.configuracion?.tema = "oscuro"
                it.roles.add("IT_USER")
            }
        }
    }

    fun procesarLoteUsuarios(usuarios: List<Usuario>): List<Usuario> {
        return usuarios.map { u ->
            u.apply {
                activo = true
                configuracion?.notificaciones = true
            }.also {
                if (it.roles.isEmpty()) it.roles.add("USER")
            }.run {
                if (nombre == "Admin") {
                    roles.add("ADMIN")
                    configuracion?.nivelPrivacidad = 3
                }
                this
            }
        }
    }


    fun parsearYCrearUsuario(datosRaw: String): Usuario? {
        val partes = datosRaw.split("|").map { it.trim() }
        if (partes.isEmpty()) return null

        val mapa = partes.mapNotNull {
            val kv = it.split(":").map { s -> s.trim() }
            if (kv.size == 2 && kv[0].isNotEmpty()) kv[0] to kv[1] else null
        }.toMap()

        val id = mapa["id"]?.toIntOrNull()
        val nombre = mapa["nombre"]
        val email = mapa["email"]

        if (id == null || nombre.isNullOrEmpty() || email.isNullOrEmpty()) return null

        return Usuario().apply {
            this.id = id
            this.nombre = nombre
            this.email = email
            this.activo = mapa["activo"]?.lowercase() == "true"
            this.roles = mapa["roles"]?.split(",")?.map { it.trim() }?.toMutableList() ?: mutableListOf("USER")
            this.configuracion = ConfiguracionUsuario().apply {
                tema = mapa["tema"] ?: "claro"
                idioma = mapa["idioma"] ?: "es"
            }
        }
    }
}