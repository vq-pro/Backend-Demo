@Service
Feature: Backend demo

#  FIXME 2 Get rid of this
  Scenario: Get greeting
    Given we are logged in
    When we ask for a greeting for "Toto" [PUT "/v2/greetings/{name}"]
    Then we get a greeting message
      """
        {
          "content": "Hello Toto!"
        }
      """

#  FIXME 2 Get rid of this
  Scenario: Get greeting when not logged in
    Given we are not logged in
    When we ask for a greeting for "Toto" [PUT "/v2/greetings/{name}"]
    Then we should get a 401 error

  Scenario Outline: Get wheel details [<wheel>]
    Given we are logged in
    And we know about these wheels:
      | brand    | name    |
      | Veteran  | Sherman |
      | KingSong | S18     |
    When we ask for details for "<wheel>"
    Then we get the "<message>"
    Examples:
      | wheel   | message                |
      | Sherman | Hello Veteran Sherman! |
      | S18     | Hello KingSong S18!    |

  Scenario: Get wheel details for unknown wheel
    Given we are logged in
    And we know about these wheels:
      | brand    | name    |
      | Veteran  | Sherman |
      | KingSong | S18     |
    When we ask for details for "Segway"
    Then we should get a 404 error

  Scenario: Get wheel details when not logged in
    Given we are not logged in
    When we ask for details for "Sherman"
    Then we should get a 401 error
