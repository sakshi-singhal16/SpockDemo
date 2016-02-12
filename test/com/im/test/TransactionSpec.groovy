package com.im.test

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class TransactionSpec extends Specification {

    Transaction transaction=new Transaction()

    void "Sell subtracts the balance of a user by the price of the product"() {
        setup:
        Product product = new Product(price: 100)
        User user = new User(balance: 200)

        when:
        transaction.sell(product, user)

        then:
        user.balance == 100.toBigDecimal()
    }


    @Unroll("An exception is thrown when #description")
    void "An exception is thrown if user's balance is not enough"() {
        given:
        Product product = new Product(price: 100)

        and:
        User user = new User(balance: balance)

        when:
        transaction.sell(product, user)

        then:
        def e = thrown(SaleException)
        e.message == "Not enough account balance"

        where:
        balance << [50, 0]

        description = balance == 50 ? 'less than product cost' : 'user has no balance'
    }


}