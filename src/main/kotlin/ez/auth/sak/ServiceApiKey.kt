package ez.auth.sak

import org.slf4j.LoggerFactory
import org.springframework.util.DigestUtils

@Suppress("MemberVisibilityCanBePrivate", "unused")
class ServiceApiKey {
  companion object {
    private val logger = LoggerFactory.getLogger(ServiceApiKey::class.java)
  }

  /**
   * SAK http header name
   */
  var name = "X-SAK"

  /**
   * SAK secret key
   */
  var value = ""

  /**
   * SAK salt, default: random md5 hex(32 chars, lower case), regenerate every time app starts
   */
  var salt = DigestUtils.md5DigestAsHex(Math.random().toString().toByteArray())

  /**
   * @return if both sak [name] and [value] are not empty - true; else -false
   */
  private fun isConfigValid() = name.isNotEmpty() && value.isNotEmpty()

  /**
   * encode SAK. format: MD5(value+salt).toHex() + salt (hex: 32 chars, lower case)
   * @return encoded SAK
   */
  fun encode(value: String, salt: String): String {
    val hex = DigestUtils.md5DigestAsHex((value + salt).toByteArray())
    return hex + salt
  }

  /**
   * encode local SAK(for sending to other services)
   * @return if [isConfigValid] return true - encoded local SAK; else - null
   */
  fun encodeLocal(): String? = if (isConfigValid()) encode(value, salt) else null

  /**
   * check if the remote SAK use the same key as local config
   */
  fun checkRemote(remoteSAK: String?): Boolean {
    if (remoteSAK == null) return !isConfigValid()
    val remoteSalt = remoteSAK.substring(32)
    val result = encode(value, remoteSalt) == remoteSAK
    if (!result) logger.warn("SAK not match! remoteSAK: {}", remoteSAK)
    return result
  }
}