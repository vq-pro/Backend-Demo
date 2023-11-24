Feature: Kata - Percentage

#  Scenario Outline: Calculate battery percentage [<wheel> @ <voltage>]
#    Given we are logged in
#    When we ask for the percentage for <voltage> on the <wheel>
#    Then we get <percentage>
#    Examples:
#      | wheel   | voltage | percentage |
#      | Sherman | 75.6V   | 0.0%       |
##      | Sherman | 92.5V   | 67.1%      |
##      | S18     | 73.0V   | 54.2%      |

#  Scenario Outline: Get wheel details [<name>]
#    Given we are logged in
#    When we ask for the <name>'s details
#    Then we get the wheel details:
#      | brand       | <brand>       |
#      | name        | <name>        |
#      | voltage max | <voltage max> |
#      | voltage min | <voltage min> |
#    Examples:
#      | brand    | name    | voltage min | voltage max |
#      | Veteran  | Sherman | 75.6V       | 100.8V      |
#      | KingSong | S18     | 60.0V       | 84.0V       |

#  Background:
#    Given we know about these wheels:
#      | brand    | name    | voltage min | voltage max |
#      | KingSong | S18     | 60.0V       | 84.0V       |
#      | Veteran  | Sherman | 75.6V       | 100.8V      |

#  Scenario Outline: <method> - ERROR - input error
#      | Adding a wheel               | add a new wheel with 0V voltage              |
#      | Calculate battery percentage | ask for the percentage on an empty wheel     |
#      | Calculate battery percentage | ask for the percentage for 0V on the Sherman |

#  Scenario Outline: <method> - ERROR - not logged in

#  Scenario Outline: <method> - ERROR - unknown wheel
