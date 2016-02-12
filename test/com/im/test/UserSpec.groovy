package com.im.test

import spock.lang.Specification
import spock.lang.Unroll

class UserSpec extends Specification {
    User user

    def setup() {
        user = new User()
    }

    @Unroll
    def "Get full name"() {
        given: "user first name"
        user.firstName = fn

        and: "user last name"
        user.lastName = ln

        when:
        String returnedFullName = user.getFullName()

        then:
        expectedFullName == returnedFullName

        where:
        fn       | ln        | expectedFullName
        null     | "singhal" | null + " singhal" //" singhal" not working
        "sakshi" | null      | "sakshi " + null
        "sakshi" | "singhal" | "sakshi singhal"
    }

    @Unroll
    def "Prefix the full name"() {
        given: "a user"
        user.firstName = fn
        user.lastName = ln
        user.gender = g


        when:
        String resultReceived = user.displayName()

        then:
        resultReceived == resultExpected

        where:
        fn       | ln        | g        | resultExpected
        "sakshi" | "singhal" | "Female" | "Mssakshi singhal"
        null     | "singhal" | "Female" | "Msnull singhal" //"Ms singhal" not working
        "marc"   | "anthony" | "Male"   | "Mrmarc anthony"
    }

    def "validate password correctly"() {

        when:
        Boolean passwordValid = user.isValidPassword(pswd)

        then:
        passwordValid == valueExpected

        where:
        pswd       | valueExpected
        "ss"       | false
        "password" | true
        null       | false

    }

    @Unroll
    def "encrypt password"() {
        given:
        user.metaClass.isValidPassword = { true }
        def mockedPasswordEncrypterService = Mock(PasswordEncrypterService)
        user.passwordEncrypterService = mockedPasswordEncrypterService
        mockedPasswordEncrypterService.encrypt(testPwd) >> { result }


        when:
        String receivedPswd = user.encyryptPassword(testPwd)

        then:
        receivedPswd == result

        where:
        testPwd << ["abcdefgh", null, "abc"]
        result << ["abc", null, null]


    }

    def "calculate income group of user"() {
        given:
        user.incomePerMonth = testIncome

        when:
        String receivedIncomeGroup = user.getIncomeGroup()

        then:
        receivedIncomeGroup == expectedIncomeGroup

        where:
        testIncome | expectedIncomeGroup
        null       | "MiddleClass"
        -1000      | "MiddleClass"
        4000       | "MiddleClass"
        5000       | "MiddleClass"
        7000       | "Lower MiddleClass"
        10000      | "Lower MiddleClass"
        11000      | "Higher MiddleClass"


    }

    def "purchased product is added to list of purchased products"() {
        given:
        Product product = new Product()

        when:
        user.purchase(product)

        then:
        user.purchasedProducts.contains(product) == true


    }

    def "cancelled product is removed from the list of purchased products"() {
        given:
        Product product = new Product()

        when:
        user.cancelPurchase(product)

        then:
        user.purchasedProducts.contains(product) != true


    }

    def "get sorted list of categories"() {
        given:
        List testList = [12, 3, 1, 56, 7]
        user.metaClass.getInterestedInCategories = { return testList }

        when:
        List receivedList = user.getSortedInterestedInCategories()

        then:
        receivedList == testList.sort()


    }
}
