@Service
Feature: Backend demo

  Background:
    Given we know about these wheels:
      | brand    | name    |
      | KingSong | S18     |
      | Veteran  | Sherman |

  Scenario Outline: Get wheel details [<wheel>]
    Given we are logged in
    When we ask for the <wheel>'s details
    Then we get the wheel details:
      | Brand | <brand> |
      | Name  | <wheel> |
    Examples:
      | wheel   | brand    |
      | Sherman | Veteran  |
      | S18     | KingSong |

  Scenario: Get wheel details - ERROR - not logged in
    Given we are not logged in
    When we ask for the Sherman's details
    Then we should get a 401 error

  Scenario: Get wheel details - ERROR - unknown wheel
    Given we are logged in
    When we ask for the Segway's details
    Then we should get a 404 error
