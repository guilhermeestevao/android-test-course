package com.ivy.core.domain.algorithm.calc

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.ivy.core.domain.algorithm.calc.data.RawStats
import com.ivy.core.persistence.algorithm.calc.CalcTrn
import com.ivy.data.CurrencyCode
import com.ivy.data.transaction.TransactionType
import org.junit.jupiter.api.Test
import java.time.Instant

internal class RawStatsTest {

    @Test
    fun `Creating raw stats from transactions`() {

        val tenSecondsAgo = Instant.now().minusSeconds(10)
        val fiveSecondsAgo = Instant.now().minusSeconds(5)
        val threeSecondsAgo = Instant.now().minusSeconds(3)

        val terns = listOf<CalcTrn>(
            CalcTrn(
                amount = 1.0,
                currency = "EUR",
                type = TransactionType.Income,
                time = tenSecondsAgo
            ),
            CalcTrn(
                amount = 2.0,
                currency = "USD",
                type = TransactionType.Income,
                time = fiveSecondsAgo
            ),
            CalcTrn(
                amount = -3.0,
                currency = "BRL",
                type = TransactionType.Expense,
                time = threeSecondsAgo
            )
        )

        val expected = RawStats(
            incomes = mapOf(
                "EUR" to 1.0,
                "USD" to 2.0
            ),
            expenses = mapOf(
                "BRL" to -3.0
            ),
            incomesCount = 2,
            expensesCount = 1,
            newestTrnTime = threeSecondsAgo
        )

        val actual = rawStats(terns)

        assertThat(actual).isEqualTo(expected)
        
    }

}