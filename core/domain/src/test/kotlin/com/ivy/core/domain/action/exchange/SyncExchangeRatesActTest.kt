package com.ivy.core.domain.action.exchange

import assertk.assertThat
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SyncExchangeRatesActTest {

    private lateinit var syncExchangeRatesAct: SyncExchangeRatesAct
    private lateinit var exchangeRateDaoFake: ExchangeRateDaoFake
    private lateinit var remoteExchangeProviderFake: RemoteExchangeProviderFake

    @BeforeEach
    fun setup(){
        remoteExchangeProviderFake = RemoteExchangeProviderFake()
        exchangeRateDaoFake = ExchangeRateDaoFake()
        syncExchangeRatesAct = SyncExchangeRatesAct(remoteExchangeProviderFake, exchangeRateDaoFake)
    }

    @Test
    fun `Sync exchange rates, negatives cases ignored `() = runBlocking {
        syncExchangeRatesAct("USD")
        val usdRates = exchangeRateDaoFake
            .findAllByBaseCurrency("USD")
            .first { it.isNotEmpty()  }

        val cadRate = usdRates.find { it.currency == "CAD" }

        assertThat(cadRate).isNull()
    }

    @Test
    fun `Sync exchange rates, valid values saved `() = runBlocking<Unit> {
        syncExchangeRatesAct("USD")
        val usdRates = exchangeRateDaoFake
            .findAllByBaseCurrency("USD")
            .first { it.isNotEmpty() }

        val eurRate = usdRates.find { it.currency == "EUR" }
        val audRate = usdRates.find { it.currency == "AUD" }

        assertThat(eurRate).isNotNull()
        assertThat(audRate).isNotNull()
    }
}