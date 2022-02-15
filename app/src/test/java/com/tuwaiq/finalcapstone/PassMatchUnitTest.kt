package com.tuwaiq.finalcapstone

import com.tuwaiq.finalcapstone.presentation.registerFragment.RegisterFragment
import com.google.common.truth.Truth.assertThat
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class PassMatchUnitTest {

    private lateinit var registerFragment: RegisterFragment
    private var validResult = false

    @Before
    fun setUp(){
        registerFragment = Mockito.mock(RegisterFragment::class.java)
        validResult = registerFragment.validateRegister("123456","123456")
    }

    @Test
    fun matchPass() {
        assertEquals(validResult, true)
    }

}