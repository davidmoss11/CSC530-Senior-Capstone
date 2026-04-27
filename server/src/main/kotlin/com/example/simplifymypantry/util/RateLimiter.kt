package com.example.simplifymypantry.util

import java.util.concurrent.ConcurrentHashMap

object LoginRateLimiter {
    private val attempts = ConcurrentHashMap<String, Int>()
    private val lockoutTime = ConcurrentHashMap<String, Long>()

    private val maxAttempts = 5
    private val lockoutDurationMs = 15 * 60 * 1000L // 15 minutes

    fun isLockedOut(identifier: String): Boolean {
        val lockedUntil = lockoutTime[identifier] ?: return false
        if (System.currentTimeMillis() > lockedUntil) {
            // lockout expired, clear it
            attempts.remove(identifier)
            lockoutTime.remove(identifier)
            return false
        }
        return true
    }

    fun recordFailedAttempt(identifier: String) {
        val current = attempts.merge(identifier, 1, Int::plus) ?: 1
        if (current >= maxAttempts) {
            lockoutTime[identifier] = System.currentTimeMillis() + lockoutDurationMs
        }
    }

    fun clearAttempts(identifier: String) {
        attempts.remove(identifier)
        lockoutTime.remove(identifier)
    }

    fun remainingLockoutMs(identifier: String): Long {
        val lockedUntil = lockoutTime[identifier] ?: return 0
        return maxOf(0, lockedUntil - System.currentTimeMillis())
    }
}
