Feature: Kata - Filter By Query

#  Scenario Outline: Get wheel details [<name>]
#    Given we are logged in
#    When we ask for the <name>'s details
#    Then we get the wheel details:
#      | brand   | <brand>   |
#      | name    | <name>    |
#      | battery | <battery> |
#    Examples:
#      | brand    | name    | battery |
#      | Veteran  | Sherman | 3200 Wh |
#      | KingSong | S18     | 900 Wh  |

#  Background:
#    Given we know about these wheels:
#      | brand    | name    | battery |
#      | KingSong | S18     | 900 Wh  |
#      | Veteran  | Sherman | 3200 Wh |

#  Scenario Outline: Get wheels by battery [<minimum capacity>]
#    Given we are logged in
#    When we ask for the list of wheels with a battery larger than <minimum capacity>
#    Then we get these wheels: <result>
#    Examples:
#      | minimum capacity | result                          |
#      | 0 Wh             | S18 (900 Wh), Sherman (3200 Wh) |
#      | 1000 Wh          | Sherman (3200 Wh)               |
#      | 4000 Wh          |                                 |

#  Scenario Outline: <method> - ERROR - input error
#| Adding a wheel        | add a new wheel with a 0 Wh battery                         |
#| Get wheels by battery | ask for the list of wheels with a battery larger than -1 Wh |

#  Scenario Outline: <method> - ERROR - not logged in
#| Get wheels by battery  | ask for the list of wheels with a battery larger than 900 Wh |
