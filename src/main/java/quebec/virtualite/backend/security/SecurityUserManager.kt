package quebec.virtualite.backend.security

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.stereotype.Service

@Service
class SecurityUserManager(val jdbcTemplate: JdbcTemplate)
{
    val passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()

    fun defineUser(username: String, password: String?)
    {
        if (doesUserExist(username))
            return

        jdbcTemplate.update(
            "INSERT INTO users (username, password, enabled)"
                + " VALUES (?, ?, TRUE)",
            username,
            passwordEncoder.encode(password)
        )

        jdbcTemplate.update(
            "INSERT INTO authorities (username, authority)"
                + " VALUES (?, 'ROLE_USER')",
            username
        )
    }

    private fun doesUserExist(username: String): Boolean
    {
        return jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM users WHERE username = ?",
            Int::class.java,
            username
        ) == 1
    }
}
