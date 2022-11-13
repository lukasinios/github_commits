package com.lg.domain

import com.lg.domain.githublist.usecase.OwnerAndRepositoryNameValidator
import org.junit.Assert.*
import org.junit.Test

class OwnerAndRepositoryNameValidatorTest{

    @Test
    fun `owner and repository without slash input should return false`(){
        val ownerAndRepositoryNameValidator = OwnerAndRepositoryNameValidator()
        assertFalse(ownerAndRepositoryNameValidator.isValid("aa"))
    }

    @Test
    fun `owner and repository correct input should return true`(){
        val ownerAndRepositoryNameValidator = OwnerAndRepositoryNameValidator()
        assertTrue(ownerAndRepositoryNameValidator.isValid("a/a"))
    }

    @Test
    fun `owner without repository input should return false`(){
        val ownerAndRepositoryNameValidator = OwnerAndRepositoryNameValidator()
        assertFalse(ownerAndRepositoryNameValidator.isValid("a/"))
    }

    @Test
    fun `repository without owner input should return false`(){
        val ownerAndRepositoryNameValidator = OwnerAndRepositoryNameValidator()
        assertFalse(ownerAndRepositoryNameValidator.isValid("/a"))
    }

    @Test
    fun `just slash input should return false`(){
        val ownerAndRepositoryNameValidator = OwnerAndRepositoryNameValidator()
        assertFalse(ownerAndRepositoryNameValidator.isValid("/"))
    }

    @Test
    fun `digits input should return true`(){
        val ownerAndRepositoryNameValidator = OwnerAndRepositoryNameValidator()
        assertTrue(ownerAndRepositoryNameValidator.isValid("1/1"))
    }

    @Test
    fun `special characters input should return true`(){
        val ownerAndRepositoryNameValidator = OwnerAndRepositoryNameValidator()
        assertTrue(ownerAndRepositoryNameValidator.isValid("@/@"))
    }

    @Test
    fun `owner special characters and  repository input should return true`(){
        val ownerAndRepositoryNameValidator = OwnerAndRepositoryNameValidator()
        assertTrue(ownerAndRepositoryNameValidator.isValid("@/s"))
    }

    @Test
    fun `repository special characters and  owner input should return true`(){
        val ownerAndRepositoryNameValidator = OwnerAndRepositoryNameValidator()
        assertTrue(ownerAndRepositoryNameValidator.isValid("android/platform_build"))
    }
}