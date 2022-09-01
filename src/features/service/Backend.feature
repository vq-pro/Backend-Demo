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

#  Scenario Outline: Calculate battery percentage [<wheel> @ <voltage>]
#    Given we are logged in
#    When we ask for the percentage for <voltage> on the <wheel>
#    Then we get <percentage>
#    Examples:
#      | wheel   | voltage | percentage |
#      | Sherman | 75.6V   | 0.0%       |
##      | Sherman | 92.5V   | 67.1%      |
##      | S18     | 75.0V   | 62.5%       |

#  Scenario Outline: Get wheel details [<wheel>]
#    Given we are logged in
#    When we ask for the <wheel>'s details
#    Then we get the wheel details:
#      | Brand       | <brand>       |
#      | Name        | <wheel>       |
#      | Voltage Max | <voltage max> |
#      | Voltage Min | <voltage min> |
#    Examples:
#      | wheel   | brand    | voltage min | voltage max |
#      | Sherman | Veteran  | 75.6V       | 100.8V      |
#      | S18     | KingSong | 60.0V       | 84.0V       |

#  Background:
#    Given we know about these wheels:
#      | brand    | name    | voltage min | voltage max |
#      | KingSong | S18     | 60.0V       | 84.0V       |
#      | Veteran  | Sherman | 75.6V       | 100.8V      |

#  Scenario Outline: <method> - ERROR - not logged in

#  Scenario Outline: <method> - ERROR - unknown wheel
