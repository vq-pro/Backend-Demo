@Service
Feature: Backend demo

  Background:
    Given we know about these wheels:
      | brand    | name    |
      | Veteran  | Sherman |
      | KingSong | S18     |

  Scenario Outline: Get wheel details [<wheel>]
    Given we are logged in
    When we ask for details for "<wheel>"
    Then we get the "<message>"
    Examples:
      | wheel   | message                |
      | Sherman | Hello Veteran Sherman! |
      | S18     | Hello KingSong S18!    |

  Scenario: Get wheel details for unknown wheel
    Given we are logged in
    When we ask for details for "Segway"
    Then we should get a 404 error

  Scenario: Get wheel details when not logged in
    Given we are not logged in
    When we ask for details for "Sherman"
    Then we should get a 401 error
