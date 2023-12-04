Feature: Kata - Range Estimate

#  Scenario Outline: Calculate estimated range left [<wheel>/<distance>/<voltage>]
#    Given we are logged in
#    And we have travelled <distance> on the <wheel>
#    And the current voltage is <voltage>
#    When we ask for an estimate of the range left
#    Then we get the estimate details:
#      | estimated range remaining | <range>       |
#      | average consumption       | <consumption> |
#    Examples:
#      | wheel   | distance | voltage | range   | consumption |
#      | Sherman | 20 km    | 94V     | 41.2 km | 43.2 Wh/km  |
##      | Sherman | 20 km    | 95.5V   | 58.5 km | 33.7 Wh/km  |
##      | S18     | 20 km    | 72V     | 13.3 km | 22.5 Wh/km  |

#  Scenario Outline: Get wheel details [<name>]
#    Given we are logged in
#    When we ask for the <name>'s details
#    Then we get the wheel details:
#      | brand           | <brand>           |
#      | name            | <name>            |
#      | battery         | <battery>         |
#      | voltage max     | <voltage max>     |
#      | voltage min     | <voltage min>     |
#      | voltage reserve | <voltage reserve> |
#    Examples:
#      | brand    | name    | battery | voltage min | voltage max | voltage reserve |
#      | Veteran  | Sherman | 3200 Wh | 75.6V       | 100.8V      | 80V             |
#      | KingSong | S18     | 900 Wh  | 60V         | 84V         | 64V             |

#  Background:
#    Given we know about these wheels:
#      | brand    | name    | battery | voltage min | voltage max | voltage reserve |
#      | KingSong | S18     | 900 Wh  | 60V         | 84V         | 64V             |
#      | Veteran  | Sherman | 3200 Wh | 75.6V       | 100.8V      | 80V             |


#  Scenario Outline: <method> - ERROR - input error
#      | Adding a wheel                 | add a new wheel with 0V voltage                              |
#      | Adding a wheel                 | add a new wheel with invalid voltages                        |
#      | Adding a wheel                 | add a new wheel with invalid reserve voltage                 |
#      | Calculate estimated range left | ask for an estimate of the range left on an empty wheel      |
#      | Calculate estimated range left | ask for an estimate of the range left for 0km on the Sherman |
#      | Calculate estimated range left | ask for an estimate of the range left for 0V on the Sherman  |

#  Scenario Outline: <method> - ERROR - not logged in

#  Scenario Outline: <method> - ERROR - unknown wheel
