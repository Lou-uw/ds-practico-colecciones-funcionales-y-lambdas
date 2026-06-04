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
            if (tipo == "ADMIN") {
                roles = mutableListOf("ADMIN")
                configuracion.notificaciones = true
                configuracion.nivelPrivacidad = 3
            } else if (tipo == "USER") {
                roles = mutableListOf("USER")
                configuracion.nivelPrivacidad = 1
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
        TODO(
            """
            Implementar usando 'also' para validación:
            - Crear usuario
            - Validar que nombre no esté vacío y email contenga '@'
            - Retornar par (usuario, esValido)
        """,
        )
    }

    // Parte E: Función let

    fun procesarEmailOpcional(email: String?): String {
        TODO(
            """
            Implementar usando 'let':
            - Si email no es null: "Usuario con email: [email]"
            - Si email es null: "Usuario sin email"
        """,
        )
    }

    fun generarMensajesBienvenida(usuarios: List<Usuario>): List<String> {
        TODO(
            """
            Implementar usando 'let':
            - Solo procesar usuarios activos con email no vacío
            - Generar mensaje "Bienvenido/a [nombre] ([email])"
        """,
        )
    }

    // Parte F: Combinación de Scope Functions

    fun procesarUsuarioComplejo(datosBase: Map<String, String>): Usuario? {
        TODO(
            """
            Implementar combinando scope functions:
            1. Verificar que existan 'nombre' y 'email' (si no, retornar null)
            2. Crear usuario con 'run'
            3. Configurar propiedades con 'apply'
            4. Si departamento es "IT", usar 'also' para configuración especial (tema oscuro, rol IT_USER)
            5. Retornar usuario configurado
        """,
        )
    }

    fun procesarLoteUsuarios(usuarios: List<Usuario>): List<Usuario> {
        TODO(
            """
            Implementar pipeline con scope functions:
            1. Activar todos los usuarios (apply)
            2. Asignar rol USER si no tienen roles (also)
            3. Configurar notificaciones = true (apply)
            4. Si nombre es "Admin", agregar rol ADMIN y nivelPrivacidad = 3 (run)
        """,
        )
    }

    fun parsearYCrearUsuario(datosRaw: String): Usuario? {
        TODO(
            """
            Implementar parsing completo:
            1. Parsear formato "clave:valor|clave:valor|..."
            2. Crear usuario con los datos parseados
            3. Usar scope functions apropiadas para cada transformación
            4. Retornar null si el formato es inválido
        """,
        )
    }
}

