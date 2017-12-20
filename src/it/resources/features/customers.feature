Feature: the client can be retrieve
  Scenario: client asks for a customer that exists
    Given a system with one customer in the list
    When asking for customer 1
    Then the system responds correctly
    And  the client receives correct customer number 1 information

  Scenario: client asks for a customer that doesn't exists
    Given a system with one customer in the list
    When asking for customer 99
    Then the system responds that customer was not found
