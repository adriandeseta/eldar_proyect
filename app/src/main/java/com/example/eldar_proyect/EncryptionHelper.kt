import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class EncryptionHelper {

    private val KEY_ALIAS = "myKeyAlias"
    private val ANDROID_KEYSTORE = "AndroidKeyStore"
    private val TRANSFORMATION = "AES/GCM/NoPadding"
    private val AES_KEY_SIZE = 256

    init {
        generateKeyIfNecessary()
    }

    // Generar la clave si no existe en el Keystore
    private fun generateKeyIfNecessary() {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)

        // Verifica si la clave ya existe
        if (!keyStore.containsAlias(KEY_ALIAS)) {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
            val keyGenParameterSpec = KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(AES_KEY_SIZE)
                .build()

            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }
    }

    // Obtener la clave desde el Keystore
    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE)
        keyStore.load(null)
        return keyStore.getKey(KEY_ALIAS, null) as SecretKey
    }

    // Encriptar los datos (Número de tarjeta)
    fun encrypt(data: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())

        val iv = cipher.iv // Inicializar vector de inicialización (IV)
        val encryption = cipher.doFinal(data.toByteArray())

        // Concatenar IV y datos encriptados para poder guardarlos juntos
        val encryptedData = iv + encryption
        return Base64.encodeToString(encryptedData, Base64.DEFAULT)
    }

    // Desencriptar los datos
    fun decrypt(encryptedData: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val decodedData = Base64.decode(encryptedData, Base64.DEFAULT)

        // Extraer el IV (primeros 12 bytes) y los datos encriptados
        val iv = decodedData.copyOfRange(0, 12)
        val encryptedBytes = decodedData.copyOfRange(12, decodedData.size)

        val spec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), spec)

        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }
}
