@Service
Feature: Backend demo

  Background:
    Given we know about these wheels:
      | brand    | name    |
      | KingSong | S18     |
      | Veteran  | Sherman |

#  Scenario: Calculate battery percentage
#    Given we are logged in
#    When we ask for the percentage for 92.5V on the Sherman
#    Then we get 67.1%

  Scenario Outline: Get wheel details [<wheel>]
    Given we are logged in
    When we ask for the <wheel>'s details
    Then we get the wheel details:
      | Brand | <brand> |
      | Name  | <wheel> |
#      | Voltage Max | <max_voltage> |
#      | Voltage Min | <min_voltage> |
    Examples:
      | wheel   | brand    |
      | Sherman | Veteran  |
      | S18     | KingSong |
#      | wheel   | brand    | max_voltage | min_voltage |
#      | Sherman | Veteran  | 75.6V       | 100.8V      |
#      | S18     | KingSong | 60.0V       | 84.0V       |

  Scenario: Get wheel details - ERROR - not logged in
    Given we are not logged in
    When we ask for the Sherman's details
    Then we should get a 401 error

  Scenario: Get wheel details - ERROR - unknown wheel
    Given we are logged in
    When we ask for the Segway's details
    Then we should get a 404 error
